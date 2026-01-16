package com.gufli.hytale.toolbox.modules.chat.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.modules.chat.ChatModule;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerListCommand {

    private final ChatModule module;

    public PlayerListCommand(@NotNull ChatModule module) {
        this.module = module;
    }

    @Command("playerlist")
    @Command("players")
    @Command("list")
    @Permission("gufli.toolbox.command.playerlist")
    @CommandHelp(description = "cmd.playerlist.help.description")
    public void playerlist(@Source CommandSender sender) {
        List<PlayerRef> players = Universe.get().getPlayers();
        module.plugin().localizer().send(sender, "cmd.playerlist.list", players.size(), players.stream().map(PlayerRef::getUsername).collect(Collectors.joining(", ")));
    }

}
