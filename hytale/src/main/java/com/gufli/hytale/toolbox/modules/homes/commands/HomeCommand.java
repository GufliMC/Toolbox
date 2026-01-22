package com.gufli.hytale.toolbox.modules.homes.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EHome;
import com.gufli.hytale.toolbox.modules.homes.HomesModule;
import com.gufli.hytale.toolbox.modules.warmup.WarmupModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HomeCommand {

    private final HomesModule module;

    public HomeCommand(@NotNull HomesModule module) {
        this.module = module;
    }

    @Command("home")
    @CommandHelp(description = "cmd.home.help.description")
    @Permission("gufli.toolbox.command.home")
    public void home(@Source PlayerRef sender) {
        var home = module.home(sender.getUuid(), HomesModule.DEFAULT_HOME_NAME).orElse(null);
        if (home == null) {
            module.plugin().localizer().send(sender, "cmd.home.error.home-not-exist", HomesModule.DEFAULT_HOME_NAME);
            return;
        }

        var warmup = module.plugin().module(WarmupModule.class);
        warmup.teleport(sender, () -> {
            module.teleport(sender, home);
            module.plugin().localizer().send(sender, "cmd.home.teleport", home.name());
        });
    }

    @Command("home")
    @CommandHelp(description = "cmd.home.help.description")
    @Permission("gufli.toolbox.command.home")
    public void home(@Source PlayerRef sender,
                     @Parameter
                     @ParameterHelp(description = "cmd.home.help.param.home.description", type = "cmd.home.help.param.home.type")
                     EHome home) {
        var warmup = module.plugin().module(WarmupModule.class);
        warmup.teleport(sender, () -> {
            module.teleport(sender, home);
            module.plugin().localizer().send(sender, "cmd.home.teleport", home.name());
        });
    }

    @Command("home")
    @CommandHelp(description = "cmd.home.other.help.description")
    @Permission("gufli.toolbox.command.home.other")
    public void home(@Source PlayerRef sender,
                     @Parameter
                     @ParameterHelp(description = "cmd.home.other.help.param.player.description", type = "cmd.home.other.help.param.player.type")
                     PlayerRef target,
                     @Parameter
                     @ParameterHelp(description = "cmd.home.other.help.param.home.description", type = "cmd.home.other.help.param.home.type")
                     String homeName) {
        var home = module.home(target.getUuid(), homeName).orElse(null);
        if (home == null) {
            module.plugin().localizer().send(sender, "cmd.home.error.player-home-not-exist", target.getUsername(), homeName);
            return;
        }

        var warmup = module.plugin().module(WarmupModule.class);
        warmup.teleport(sender, () -> {
            module.teleport(sender, home);
            module.plugin().localizer().send(sender, "cmd.home.other.teleport", target.getUsername(), home.name());
        });
    }
}
