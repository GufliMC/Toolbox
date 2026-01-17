package com.gufli.hytale.toolbox.modules.admin.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.modules.admin.AdminModule;
import com.hypixel.hytale.protocol.packets.camera.SetFlyCameraMode;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class FreecamCommand {

    private final AdminModule module;

    public FreecamCommand(@NotNull AdminModule module) {
        this.module = module;
    }

    @Command("freecam")
    @CommandHelp(description = "cmd.freecam.help.description")
    @Permission("gufli.toolbox.command.freecam")
    public void freecam(@Source PlayerRef sender) {
        boolean enable = !module.isFreecam(sender);

        SetFlyCameraMode setFlyCameraMode = new SetFlyCameraMode(enable);
        sender.getPacketHandler().write(setFlyCameraMode);
        module.setFreecam(sender, enable);

        if (enable) {
            module.plugin().localizer().send(sender, "cmd.freecam.enabled");
        } else {
            module.plugin().localizer().send(sender, "cmd.freecam.disabled");
        }
    }

}
