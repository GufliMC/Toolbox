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

public class HomeRemoveCommand {

    private final HomesModule module;

    public HomeRemoveCommand(@NotNull HomesModule module) {
        this.module = module;
    }

    @Command("home remove")
    @Command("delhome")
    @CommandHelp(description = "cmd.home.remove.help.description")
    @Permission("gufli.toolbox.command.home.remove")
    public void delhome(@Source PlayerRef sender,
                        @Parameter
                        @ParameterHelp(description = "cmd.home.remove.help.param.home.description", type = "cmd.home.remove.help.param.home.type")
                        EHome home) {
        module.removeHome(home);
        module.plugin().localizer().send(sender, "cmd.home.remove", home.name());
    }

    @Command("home remove")
    @Command("delhome")
    @CommandHelp(description = "cmd.home.remove.help.description")
    @Permission("gufli.toolbox.command.home.remove")
    public void delhome(@Source PlayerRef sender) {
        var home = module.home(sender.getUuid(), HomesModule.DEFAULT_HOME_NAME).orElse(null);
        if (home == null) {
            module.plugin().localizer().send(sender, "cmd.home.error.home-not-exist", HomesModule.DEFAULT_HOME_NAME);
            return;
        }

        module.removeHome(home);
    }
}