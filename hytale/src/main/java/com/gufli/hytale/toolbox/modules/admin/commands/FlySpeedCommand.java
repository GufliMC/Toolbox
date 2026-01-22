package com.gufli.hytale.toolbox.modules.admin.commands;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlySpeedCommand {

    private final HytaleLocalizer localizer;

    public FlySpeedCommand(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    @Command("flyspeed")
    @CommandHelp(description = "cmd.flyspeed.help.description")
    @Permission("gufli.toolbox.command.flyspeed")
    public void flyspeed(@Source
                         PlayerRef sender,
                         @Parameter
                         @ParameterHelp(description = "cmd.flyspeed.help.param.player.description", type = "cmd.flyspeed.help.param.player.type")
                         float speed) {
        if (speed < 0.0F || speed > 100.0F) {
            localizer.send(sender, "cmd.flyspeed.error.invalid_speed", speed);
            return;
        }

        update(sender, speed);
        localizer.send(sender, "cmd.flyspeed.changed", speed);
    }

    @Command("flyspeed")
    @CommandHelp(description = "cmd.flyspeed.help.description")
    @Permission("gufli.toolbox.command.flyspeed")
    public void flyspeed(@Source
                         PlayerRef sender) {
        update(sender, null);
        localizer.send(sender, "cmd.flyspeed.reset");
    }

    @Command("flyspeed")
    @CommandHelp(description = "cmd.flyspeed.help.description")
    @Permission("gufli.toolbox.command.flyspeed.other")
    public void flyspeed(@Source
                    CommandSender sender,
                    @Parameter
                    @ParameterHelp(description = "cmd.fly.other.help.param.player.description", type = "cmd.fly.other.help.param.player.type")
                    PlayerRef target,
                    @Parameter
                    @ParameterHelp(description = "cmd.flyspeed.help.param.player.description", type = "cmd.flyspeed.help.param.player.type")
                    float speed) {
        update(target, speed);
        localizer.send(target, "cmd.flyspeed.changed", speed);
        localizer.send(sender, "cmd.flyspeed.other.changed", speed);
    }

    //

    private void update(@NotNull PlayerRef player, @Nullable Float value) {
        var ref = player.getReference();
        if (ref == null) {
            return;
        }

        var store = ref.getStore();

        MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
        if (movementManager == null) {
            return;
        }

        MovementSettings settings = movementManager.getSettings();
        if (value == null) {
            settings.horizontalFlySpeed = 10.0F;
            settings.verticalFlySpeed = 10.0F;
        } else {
            settings.horizontalFlySpeed = value;
            settings.verticalFlySpeed = value;
        }

        movementManager.update(player.getPacketHandler());
    }

}
