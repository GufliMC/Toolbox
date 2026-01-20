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
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WarmupModule extends AbstractModule {

    private static final double MOVEMENT_THRESHOLD = 0.3D;

    private final Collection<Warmup> warmups = new CopyOnWriteArraySet<>();

    public WarmupModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            warmups.removeIf(w -> w.player().equals(event.getPlayerRef().getUuid()));
        });
    }

    //

    public WarmupConfig config() {
        return plugin().config().teleport.warmup;
    }

    //

    public Collection<Warmup> warmups() {
        return Collections.unmodifiableCollection(warmups);
    }

    public Collection<Warmup> warmups(@NotNull PlayerRef player) {
        return warmups.stream()
                .filter(w -> w.player().equals(player.getUuid()))
                .collect(Collectors.toUnmodifiableSet());
    }

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
            @NotNull Consumer<Duration> countdown,
            @NotNull Duration duration,
            @NotNull WarmupCancellationReason... cancellationReasons
    ) {
        var c = this.plugin().scheduler().countdown(duration)
                .handler(seconds -> {
                    if (seconds == 0) {
                        return;
                    }
                    if (seconds > 5 && seconds % 5 != 0) {
                        return;
                    }
                    countdown.accept(Duration.ofSeconds(seconds));
                })
                .milestone((_) -> {
                    executor.run();
                }, 0)
                .build();
        c.start();

        var warmup = new Warmup(player.getUuid(), executor, c, cancelled, cancellationReasons);
        this.warmups.add(warmup);

        Set<WarmupCancellationReason> reasons = Set.of(cancellationReasons);
        if (reasons.contains(WarmupCancellationReason.PLAYER_MOVED)) {
            assert player.getWorldUuid() != null;
            var pos = new Position(player.getWorldUuid(), player.getTransform().clone());
            this.plugin().scheduler().asyncRepeating(
                    () -> {
                        if (Math.sqrt(pos.transform().getPosition().distanceSquaredTo(player.getTransform().getPosition())) >= MOVEMENT_THRESHOLD) {
                            cancel(warmup, WarmupCancellationReason.PLAYER_MOVED);
                        }
                    },
                    () -> warmups.contains(warmup),
                    200, TimeUnit.MILLISECONDS);
        }

        if (reasons.contains(WarmupCancellationReason.PLAYER_DAMAGED)) {
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
                            cancel(warmup, WarmupCancellationReason.PLAYER_DAMAGED);
                            return;
                        }
                        startHealth.set(health);
                    },
                    () -> warmups.contains(warmup),
                    200, TimeUnit.MILLISECONDS);
        }
    }

    public void cancel(@NotNull Warmup warmup, @NotNull WarmupCancellationReason reason) {
        if ( this.warmups.remove(warmup) ) {
            warmup.cancelled().accept(reason);
            warmup.countdown().stop();
        }
    }

    public void cancel(@NotNull Warmup warmup) {
        this.cancel(warmup, WarmupCancellationReason.CUSTOM);
    }


}
