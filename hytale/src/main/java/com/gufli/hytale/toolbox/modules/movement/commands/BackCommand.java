package com.gufli.hytale.toolbox.modules.movement.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

public class BackCommand {

    private final MovementModule module;

    public BackCommand(@NotNull MovementModule module) {
        this.module = module;
    }

    @Command("back")
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

        module.teleport(sender, teleport.from());
        module.plugin().localizer().send(sender, "cmd.back.teleported");
    }

}
