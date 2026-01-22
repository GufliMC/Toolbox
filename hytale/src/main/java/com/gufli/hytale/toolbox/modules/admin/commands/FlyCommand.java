package com.gufli.hytale.toolbox.modules.admin.commands;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.SavedMovementStates;
import com.hypixel.hytale.protocol.packets.player.SetMovementStates;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.modules.entity.component.Invulnerable;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class FlyCommand {

    private final HytaleLocalizer localizer;

    public FlyCommand(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    @Command("fly")
    @CommandHelp(description = "cmd.fly.help.description")
    @Permission("gufli.toolbox.command.fly")
    public void fly(@Source PlayerRef sender) {
        boolean enabled = toggle(sender);
        if ( enabled ) {
            localizer.send(sender, "cmd.fly.enabled");
        } else {
            localizer.send(sender, "cmd.fly.disabled");
        }
    }

    @Command("fly")
    @CommandHelp(description = "cmd.fly.help.description")
    @Permission("gufli.toolbox.command.fly.other")
    public void fly(@Source
                    CommandSender sender,
                    @Parameter
                    @ParameterHelp(description = "cmd.fly.other.help.param.player.description", type = "cmd.fly.other.help.param.player.type")
                    PlayerRef target) {
        boolean enabled = toggle(target);
        if (enabled) {
            localizer.send(target, "cmd.fly.enabled");
            localizer.send(sender, "cmd.fly.other.enabled", target.getUsername());
        } else {
            localizer.send(target, "cmd.fly.disabled");
            localizer.send(sender, "cmd.fly.other.disabled", target.getUsername());
        }
    }

    private boolean toggle(@NotNull PlayerRef player) {
        var ref = player.getReference();
        if (ref == null) {
            return false;
        }

        var store = ref.getStore();

        MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
        if (movementManager == null) {
            return false;
        }

        MovementSettings settings = movementManager.getSettings();
        settings.canFly = !settings.canFly;
        movementManager.update(player.getPacketHandler());

        if ( !settings.canFly ) {
            player.getPacketHandler().writeNoCache(new SetMovementStates(new SavedMovementStates(false)));
        }

        return settings.canFly;
    }

}
