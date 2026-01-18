package com.gufli.hytale.toolbox.modules.movement;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.movement.commands.*;
import com.gufli.hytale.toolbox.modules.movement.data.MovementSession;
import com.gufli.hytale.toolbox.modules.movement.data.Position;
import com.gufli.hytale.toolbox.modules.movement.data.TeleportRequest;
import com.hypixel.hytale.builtin.teleport.components.TeleportHistory;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class MovementModule extends AbstractModule {

    private final static Duration TELEPORT_REQUEST_TIMEOUT = Duration.ofMinutes(2);

    private final Map<UUID, MovementSession> sessions = new ConcurrentHashMap<>();
    private final Set<TeleportRequest> teleportRequests = new CopyOnWriteArraySet<>();

    public MovementModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);
        this.plugin().scheduler().asyncRepeating(this::update, 200, TimeUnit.MICROSECONDS);

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            sessions.remove(event.getPlayerRef().getUuid());

            teleportRequests.removeIf(r -> r.requester().equals(event.getPlayerRef().getUuid()));
            teleportRequests.removeIf(r -> r.requestee().equals(event.getPlayerRef().getUuid()));
        });

        registerCommands(new BackCommand(this));
        registerCommands(new TeleportRandomCommand(this));
        registerCommands(new TeleportRequestCommand(this));
        registerCommands(new TeleportAcceptCommand(this));
        registerCommands(new TeleportDenyCommand(this));
        registerCommands(new TeleportHereRequestCommand(this));
        registerCommands(new TeleportCancelCommand(this));
    }

    public MovementConfig config() {
        return this.plugin().config().movement;
    }

    private void update() {
        Universe.get().getPlayers().forEach(p -> {
            if ( p.getWorldUuid() == null ) {
                return;
            }

            var session = this.sessions.computeIfAbsent(p.getUuid(), _ -> new MovementSession());
            session.add(p.getWorldUuid(), p.getTransform());
        });
    }

    //

    public Optional<com.gufli.hytale.toolbox.modules.movement.data.Teleport> previousTeleport(@NotNull PlayerRef ref) {
        MovementSession session = this.sessions.get(ref.getUuid());
        if (session == null) {
            return Optional.empty();
        }
        return session.previousTeleport();
    }

    //

    public void teleport(@NotNull PlayerRef player, @NotNull Position position) {
        World world = Universe.get().getWorld(position.worldId());
        if ( world == null ) {
            throw new IllegalStateException("World does not exist: " + position.worldId());
        }

        teleport(player, world, position.transform());
    }

    public void teleport(@NotNull PlayerRef player, @NotNull World world, @NotNull Transform transform) {
        var ref = player.getReference();
        if ( ref == null ) {
            throw new IllegalStateException("PlayerRef has no reference!");
        }

        var store = ref.getStore();

        var pos = transform.getPosition();
        var rot = transform.getRotation();

        TeleportHistory teleportHistoryComponent = store.ensureAndGetComponent(ref, TeleportHistory.getComponentType());
        teleportHistoryComponent.append(world, pos, rot, String.format("Teleport to (%s, %s, %s)", pos.getX(), pos.getY(), pos.getZ()));

        store.addComponent(ref, Teleport.getComponentType(), new Teleport(world, pos, rot));
    }

    //

    public void teleportRequest(@NotNull PlayerRef requester, @NotNull PlayerRef requestee, @NotNull TeleportRequest.TeleportRequestTarget target) {
        this.teleportRequests.add(new TeleportRequest(requester.getUuid(), requestee.getUuid(), target, Instant.now()));
    }

    public void teleportRequestCancel(@NotNull PlayerRef requester, @NotNull PlayerRef requestee) {
        this.teleportRequests.removeIf(r -> r.requester().equals(requester.getUuid()) && r.requestee().equals(requestee.getUuid()));
    }

    public Optional<TeleportRequest> findTeleportRequest(@NotNull PlayerRef requester, @NotNull PlayerRef requestee) {
        return this.teleportRequests.stream()
                .filter(r -> r.requester().equals(requester.getUuid()) && r.requestee().equals(requestee.getUuid()))
                .filter(r -> r.timestamp().until(Instant.now()).compareTo(TELEPORT_REQUEST_TIMEOUT) <= 0)
                .findFirst();
    }

    public Collection<TeleportRequest> findTeleportRequestsByRequestee(@NotNull PlayerRef requestee) {
        return this.teleportRequests.stream()
                .filter(r -> r.requestee().equals(requestee.getUuid()))
                .filter(r -> r.timestamp().until(Instant.now()).compareTo(TELEPORT_REQUEST_TIMEOUT) <= 0)
                .toList();
    }

    public Collection<TeleportRequest> findTeleportRequestsByRequester(@NotNull PlayerRef requester) {
        return this.teleportRequests.stream()
                .filter(r -> r.requester().equals(requester.getUuid()))
                .filter(r -> r.timestamp().until(Instant.now()).compareTo(TELEPORT_REQUEST_TIMEOUT) <= 0)
                .toList();
    }


}
