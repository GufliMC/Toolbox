package com.gufli.hytale.toolbox.modules.warps.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EWarp;
import com.gufli.hytale.toolbox.modules.warps.WarpsModule;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class WarpCommand {

    private final WarpsModule module;

    public WarpCommand(@NotNull WarpsModule module) {
        this.module = module;
    }

    @Command("warp")
    @CommandHelp(description = "cmd.warp.help.description")
    @Permission("gufli.toolbox.command.warp")
    public void warp(@Source PlayerRef sender,
                     @Parameter
                     @ParameterHelp(description = "cmd.warp.help.param.warp.description", type = "cmd.warp.help.param.warp.type")
                     EWarp warp) {
        if ( !PermissionsModule.get().hasPermission(sender.getUuid(), "gufli.toolbox.command.warp." + warp.name()) ) {
            module.plugin().localizer().send(sender, "cmderr.no-permission");
            return;
        }

        module.teleport(sender, warp);
        module.plugin().localizer().send(sender, "cmd.warp.teleport", warp.name());
    }
}
