package com.gufli.hytale.toolbox.modules.warps.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.modules.warps.WarpsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class WarpSetCommand {

    private final WarpsModule module;

    public WarpSetCommand(@NotNull WarpsModule module) {
        this.module = module;
    }

    @Command("warp set")
    @Command("setwarp")
    @CommandHelp(description = "cmd.warp.set.help.description")
    @Permission("gufli.toolbox.command.warp.set")
    public void setwarp(@Source PlayerRef sender, @Parameter String name) {
        if ( sender.getWorldUuid() == null ) {
            return;
        }

        module.addWarp(name, sender.getWorldUuid(), sender.getTransform());
        module.plugin().localizer().send(sender, "cmd.warp.set", name);
    }
}
