package com.gufli.hytale.toolbox.modules.warmup;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.teleport.data.Position;
import com.gufli.hytale.toolbox.modules.warmup.data.Warmup;
import com.gufli.hytale.toolbox.modules.warmup.data.WarmupCancellationReason;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class WarmupModule extends AbstractModule {

    private static final double MOVEMENT_THRESHOLD = 0.3D;

    private final Map<UUID, Warmup> warmups = new ConcurrentHashMap<>();

    public WarmupModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            warmups.values().removeIf(w -> w.player().equals(event.getPlayerRef().getUuid()));
        });
    }

    //

    public WarmupConfig config() {
        return plugin().config().teleport.warmup;
    }

    //

    public void cancel(@NotNull UUID id, @NotNull WarmupCancellationReason reason) {
        Warmup warmup = this.warmups.remove(id);
        if ( warmup != null ) {
            warmup.cancelled().accept(reason);
            warmup.countdown().stop();
        }
    }

    public void cancel(@NotNull UUID id) {
        this.cancel(id, WarmupCancellationReason.CUSTOM);
    }

    private void execute(@NotNull UUID id) {
        Warmup warmup = this.warmups.remove(id);
        if ( warmup != null ) {
            warmup.executor().run();
        }
    }

    //

    public void teleport(@NotNull PlayerRef player, @NotNull Runnable executor) {
        Duration duration = null;

        for ( Map.Entry<String, WarmupConfig.TeleportWarmupGroup> group : config().groups.entrySet() ) {
            if ( !group.getKey().equals("default") && !PermissionsModule.get().hasPermission(player.getUuid(), "gufli.toolbox.warmup.teleport.group." + group.getKey()) ) {
                continue;
            }

            Duration d = Duration.ofSeconds(group.getValue().delay);
            if ( duration == null || d.compareTo(duration) < 0 ) {
                duration = d;
            }
        }

        if ( duration == null || duration.isZero() ) {
            executor.run();
            return;
        }

        start(player,
                executor,
                (reason) -> {
                    plugin().localizer().send(player, "warmup.teleport.cancel." + reason.name().toLowerCase().replace("_", "-"));
                },
                d -> {
                    plugin().localizer().send(player, "warmup.teleport.countdown", d.getSeconds());
                },
                duration,
                WarmupCancellationReason.PLAYER_MOVED,
                WarmupCancellationReason.PLAYER_DAMAGED);
    }

    public void start(
            @NotNull PlayerRef player,
            @NotNull Runnable executor,
            @NotNull Consumer<WarmupCancellationReason> cancelled,
            @NotNull Consumer<Duration> countdownHandler,
            @NotNull Duration duration,
            @NotNull WarmupCancellationReason... cancellationReasons
    ) {
        warmups.entrySet().stream()
                .filter(entry -> entry.getValue().player().equals(player.getUuid()))
                .map(Map.Entry::getKey)
                .findFirst().ifPresent(existing -> cancel(existing, WarmupCancellationReason.CUSTOM));

        UUID id = UUID.randomUUID();

        var countdown = this.plugin().scheduler().countdown(duration)
                .handler(seconds -> {
                    if (seconds == 0) {
                        return;
                    }
                    if (seconds > 5 && seconds % 5 != 0) {
                        return;
                    }
                    countdownHandler.accept(Duration.ofSeconds(seconds));
                })
                .milestone((_) -> execute(id), 0)
                .build();
        countdown.start();

        var warmup = new Warmup(player.getUuid(), executor, countdown, cancelled, cancellationReasons);
        this.warmups.put(id, warmup);

        Set<WarmupCancellationReason> reasons = Set.of(cancellationReasons);
        if (reasons.contains(WarmupCancellationReason.PLAYER_MOVED)) {
            checkPlayerMoved(id, player);
        }

        if (reasons.contains(WarmupCancellationReason.PLAYER_DAMAGED)) {
            checkPlayerDamaged(id, player);
        }
    }

    //

    private void checkPlayerMoved(@NotNull UUID id, @NotNull PlayerRef player) {
        assert player.getWorldUuid() != null;
        var pos = new Position(player.getWorldUuid(), player.getTransform().clone());
        this.plugin().scheduler().asyncRepeating(
                () -> {
                    if (Math.sqrt(pos.transform().getPosition().distanceSquaredTo(player.getTransform().getPosition())) >= MOVEMENT_THRESHOLD) {
                        cancel(id, WarmupCancellationReason.PLAYER_MOVED);
                    }
                },
                () -> warmups.containsKey(id),
                200, TimeUnit.MILLISECONDS);
    }

    private void checkPlayerDamaged(@NotNull UUID id, @NotNull PlayerRef player) {
        assert player.getWorldUuid() != null;
        var ref = player.getReference();
        assert ref != null;
        var statMap = ref.getStore().getComponent(ref, EntityStatMap.getComponentType());
        assert statMap != null;
        int healthStatIndex = DefaultEntityStatTypes.getHealth();
        var statValue = statMap.get(healthStatIndex);
        assert statValue != null;

        AtomicReference<Float> startHealth = new AtomicReference<>(statValue.get());
        this.plugin().scheduler().asyncRepeating(
                () -> {
                    float health = statValue.get();
                    if (health < startHealth.get()) {
                        cancel(id, WarmupCancellationReason.PLAYER_DAMAGED);
                        return;
                    }
                    startHealth.set(health);
                },
                () -> warmups.containsKey(id),
                200, TimeUnit.MILLISECONDS);
    }


}
