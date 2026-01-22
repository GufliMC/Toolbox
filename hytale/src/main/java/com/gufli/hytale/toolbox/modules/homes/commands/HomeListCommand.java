package com.gufli.hytale.toolbox.modules.homes.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EHome;
import com.gufli.hytale.toolbox.modules.homes.HomesModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class HomeListCommand {

    private final HomesModule module;

    public HomeListCommand(@NotNull HomesModule module) {
        this.module = module;
    }

    @Command("home list")
    @Command("homes")
    @CommandHelp(description = "cmd.home.list.help.description")
    @Permission("gufli.toolbox.command.home.list")
    public void homes(@Source PlayerRef sender) {
        var playerHomes = module.homes(sender.getUuid());
        String homes = playerHomes.stream().map(EHome::name).collect(Collectors.joining(", "));
        module.plugin().localizer().send(sender, "cmd.home.list", playerHomes.size(), homes);
    }

    @Command("home list")
    @Command("homes")
    @CommandHelp(description = "cmd.home.list.other.help.description")
    @Permission("gufli.toolbox.command.home.list.other")
    public void homes(@Source PlayerRef sender,
                          @Parameter
                          @ParameterHelp(description = "cmd.home.list.other.help.param.player.description", type = "cmd.home.list.other.help.param.player.type")
                          PlayerRef target) {
        var playerHomes = module.homes(target.getUuid());
        String homes = playerHomes.stream().map(EHome::name).collect(Collectors.joining(", "));
        module.plugin().localizer().send(sender, "cmd.home.list.other", target.getUsername(), playerHomes.size(), homes);
    }
}
