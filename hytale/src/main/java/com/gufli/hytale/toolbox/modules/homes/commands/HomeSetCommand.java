package com.gufli.hytale.toolbox.modules.homes.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.modules.homes.HomesModule;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HomeSetCommand {

    private final HomesModule module;

    public HomeSetCommand(@NotNull HomesModule module) {
        this.module = module;
    }

    @Command("home set")
    @Command("sethome")
    @CommandHelp(description = "cmd.home.set.help.description")
    @Permission("gufli.toolbox.command.home.set")
    public void sethome(@Source PlayerRef sender, @Parameter String name) {
        if (sender.getWorldUuid() == null) {
            return;
        }

        int maxHomes = module.getMaxHomes(sender.getUuid());

        int currentHomes = module.homes(sender.getUuid()).size();
        if (currentHomes >= maxHomes && module.home(sender.getUuid(), name).isEmpty()) {
            module.plugin().localizer().send(sender, "cmd.home.set.max-homes", maxHomes);
            return;
        }

        module.addHome(sender.getUuid(), name, sender.getWorldUuid(), sender.getTransform());
        module.plugin().localizer().send(sender, "cmd.home.set", name);
    }

    @Command("home set")
    @Command("sethome")
    @CommandHelp(description = "cmd.home.set.help.description")
    @Permission("gufli.toolbox.command.home.set")
    public void sethome(@Source PlayerRef sender) {
        sethome(sender, HomesModule.DEFAULT_HOME_NAME);
    }
}
