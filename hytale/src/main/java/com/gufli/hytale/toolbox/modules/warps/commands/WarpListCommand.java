package com.gufli.hytale.toolbox.modules.warps.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.database.entity.EWarp;
import com.gufli.hytale.toolbox.modules.warps.WarpsModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class WarpListCommand {

    private final WarpsModule module;

    public WarpListCommand(@NotNull WarpsModule module) {
        this.module = module;
    }

    @Command("warp list")
    @Command("warps")
    @CommandHelp(description = "cmd.warp.list.help.description")
    @Permission("gufli.toolbox.command.warp.list")
    public void warp(@Source CommandSender sender) {
        String warps = module.warps().stream().map(EWarp::name).collect(Collectors.joining(", "));
        module.plugin().localizer().send(sender, "cmd.warp.list", module.warps().size(), warps);
    }
}
