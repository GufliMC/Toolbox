package com.gufli.hytale.toolbox.modules.movement;

import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.modules.movement.commands.BackCommand;
import com.hypixel.hytale.builtin.teleport.components.TeleportHistory;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MovementModule extends AbstractModule {

    private final Map<UUID, MovementSession> sessions = new ConcurrentHashMap<>();

    public MovementModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);
        this.plugin().scheduler().asyncRepeating(this::update, 1, TimeUnit.SECONDS);

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            sessions.remove(event.getPlayerRef().getUuid());
        });

        registerCommands(new BackCommand(this));
    }

    private void update() {
        Universe.get().getPlayers().forEach(p -> {
            var session = this.sessions.computeIfAbsent(p.getUuid(), _ -> new MovementSession());
            session.add(p.getWorldUuid(), p.getTransform());
        });
    }

    public Optional<Position> lastTeleport(@NotNull PlayerRef ref) {
        MovementSession session = this.sessions.get(ref.getUuid());
        if (session == null) {
            return Optional.empty();
        }
        return session.lastTeleport();
    }

    public Position lastPosition(@NotNull PlayerRef ref) {
        MovementSession session = this.sessions.get(ref.getUuid());
        if (session == null) {
            return null;
        }
        return session.lastPosition();
    }

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

        store.addComponent(ref, Teleport.getComponentType(), new Teleport(world, transform));
    }
}
