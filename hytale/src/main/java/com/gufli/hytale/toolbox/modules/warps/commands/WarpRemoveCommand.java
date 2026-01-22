package com.gufli.hytale.toolbox.modules.warps.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EWarp;
import com.gufli.hytale.toolbox.modules.warps.WarpsModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WarpRemoveCommand {

    private final WarpsModule module;

    public WarpRemoveCommand(@NotNull WarpsModule module) {
        this.module = module;
    }

    @Command("warp remove")
    @Command("delwarp")
    @CommandHelp(description = "cmd.warp.remove.help.description")
    @Permission("gufli.toolbox.command.warp.remove")
    public void setwarp(@Source CommandSender sender,
                        @Parameter
                        @ParameterHelp(description = "cmd.warp.remove.help.param.warp.description", type = "cmd.warp.remove.help.param.warp.type")
                        EWarp warp) {
        module.removeWarp(warp);
        module.plugin().localizer().send(sender, "cmd.warp.remove", warp.name());
    }
}
