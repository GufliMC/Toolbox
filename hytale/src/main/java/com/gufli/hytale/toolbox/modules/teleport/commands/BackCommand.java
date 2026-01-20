package com.gufli.hytale.toolbox.modules.teleport.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.warmup.WarmupModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

public class BackCommand {

    private final TeleportModule module;

    public BackCommand(@NotNull TeleportModule module) {
        this.module = module;
    }

    @Command("back")
    @Permission("gufli.toolbox.command.back")
    @CommandHelp(description = "cmd.back.help.description")
    public void back(@Source PlayerRef sender) {
        var teleport = module.previousTeleport(sender).orElse(null);
        if ( teleport == null ) {
            module.plugin().localizer().send(sender, "cmd.back.error.no-teleport-known");
            return;
        }

        World world = Universe.get().getWorld(teleport.from().worldId());
        if ( world == null ) {
            module.plugin().localizer().send(sender, "cmd.back.error.no-teleport-valid");
            return;
        }

        WarmupModule warmup = this.module.plugin().module(WarmupModule.class);
        warmup.teleport(sender, () -> {
            module.teleport(sender, teleport.from());
            module.plugin().localizer().send(sender, "cmd.back.teleported");
        });
    }

}
