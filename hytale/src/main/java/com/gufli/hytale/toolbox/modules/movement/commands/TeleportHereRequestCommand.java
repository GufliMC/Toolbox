package com.gufli.hytale.toolbox.modules.movement.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
import com.gufli.hytale.toolbox.modules.movement.data.TeleportRequest;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class TeleportHereRequestCommand {

    private final MovementModule module;

    public TeleportHereRequestCommand(MovementModule module) {
        this.module = module;
    }

    @Command("tphererequest")
    @Command("tpahere")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tprequest.help.description")
    public void tphererequest(@Source PlayerRef sender,
                          @Parameter
                          @ParameterHelp(description = "cmd.tprequest.help.param.target.description", type = "cmd.tprequest.help.param.target.type")
                          PlayerRef target) {

        TeleportRequest request = module.findTeleportRequest(sender, target).orElse(null);
        if ( request != null ) {
            module.plugin().localizer().send(sender, "cmd.tprequest.error.too-many-requests");
            return;
        }

        module.teleportRequest(sender, target, TeleportRequest.TeleportRequestTarget.REQUESTER);
        module.plugin().localizer().send(sender, "cmd.tphererequest.requester", target.getUsername());
        module.plugin().localizer().send(target, "cmd.tphererequest.requestee", sender.getUsername());
    }
}
