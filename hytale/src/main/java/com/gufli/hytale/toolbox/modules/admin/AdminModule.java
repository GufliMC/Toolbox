package com.gufli.hytale.toolbox.modules.admin;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.admin.commands.FreecamCommand;
import com.gufli.hytale.toolbox.modules.admin.commands.GodCommand;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class AdminModule extends AbstractModule {

    private final Set<UUID> freecamPlayers = new CopyOnWriteArraySet<>();

    public AdminModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        registerCommands(new GodCommand(plugin.localizer()));
        registerCommands(new FreecamCommand(this));

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            freecamPlayers.remove(event.getPlayerRef().getUuid());
        });
    }

    public boolean isFreecam(@NotNull PlayerRef player) {
        return freecamPlayers.contains(player.getUuid());
    }

    public void setFreecam(@NotNull PlayerRef player, boolean enabled) {
        if (enabled) {
            freecamPlayers.add(player.getUuid());
        } else {
            freecamPlayers.remove(player.getUuid());
        }
    }

}
