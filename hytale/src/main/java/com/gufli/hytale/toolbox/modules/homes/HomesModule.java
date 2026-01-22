package com.gufli.hytale.toolbox.modules.homes;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.database.entity.EHome;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.homes.arguments.HomeArguments;
import com.gufli.hytale.toolbox.modules.homes.commands.HomeCommand;
import com.gufli.hytale.toolbox.modules.homes.commands.HomeListCommand;
import com.gufli.hytale.toolbox.modules.homes.commands.HomeRemoveCommand;
import com.gufli.hytale.toolbox.modules.homes.commands.HomeSetCommand;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.teleport.data.Position;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class HomesModule extends AbstractModule {

    public static final String DEFAULT_HOME_NAME = "default";

    private final Set<EHome> homes = new CopyOnWriteArraySet<>();

    public HomesModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        registerCommands(new HomeArguments(this));
        registerCommands(new HomeCommand(this));
        registerCommands(new HomeSetCommand(this));
        registerCommands(new HomeRemoveCommand(this));
        registerCommands(new HomeListCommand(this));

        plugin.database().findAllAsync(EHome.class).thenAccept(this.homes::addAll);

        plugin.getEventRegistry().register(PlayerConnectEvent.class, event -> {
            plugin.database().findAllWhereAsync(EHome.class, "playerId", event.getPlayerRef().getUuid())
                    .thenAccept(this.homes::addAll);
        });

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            homes.removeIf(ep -> ep.playerId().equals(event.getPlayerRef().getUuid()));
        });
    }

    //

    public HomesConfig config() {
        return plugin().config().homes;
    }

    public int getMaxHomes(@NotNull UUID playerId) {
        int maxHomes = 0;

        for (Map.Entry<String, HomesConfig.HomeLimitGroup> group : config().groups.entrySet()) {
            if (!group.getKey().equals("default") && !PermissionsModule.get().hasPermission(playerId, "gufli.toolbox.homes.group." + group.getKey())) {
                continue;
            }

            int limit = group.getValue().limit;
            if (maxHomes == 0 || limit > maxHomes) {
                maxHomes = limit;
            }
        }

        return maxHomes;
    }

    public void teleport(@NotNull PlayerRef player, @NotNull EHome home) {
        plugin().module(TeleportModule.class).teleport(player, new Position(home.worldId(), home.position()));
    }

    public Collection<EHome> homes() {
        return Collections.unmodifiableSet(homes);
    }

    public Collection<EHome> homes(@NotNull UUID playerId) {
        return homes.stream()
                .filter(home -> home.playerId().equals(playerId))
                .collect(Collectors.toUnmodifiableSet());
    }

    public Optional<EHome> home(@NotNull UUID playerId, @NotNull String name) {
        return homes.stream()
                .filter(home -> home.playerId().equals(playerId) && home.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public EHome addHome(@NotNull UUID playerId, @NotNull String name, @NotNull UUID worldId, @NotNull Transform position) {
        EHome home = home(playerId, name).orElse(null);
        if (home == null) {
            home = new EHome(playerId, name, worldId, position.clone());
            homes.add(home);
        } else {
            home.setLocation(worldId, position.clone());
        }
        plugin().database().persistAsync(home);
        return home;
    }

    public void removeHome(EHome home) {
        homes.remove(home);
        plugin().database().removeAsync(home);
    }
}
