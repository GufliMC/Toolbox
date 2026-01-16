package com.gufli.hytale.toolbox.modules.movement.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
import com.gufli.hytale.toolbox.modules.movement.data.Position;
import com.gufli.hytale.toolbox.modules.movement.data.TeleportRequest;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class TeleportAcceptCommand {

    private final MovementModule module;

    public TeleportAcceptCommand(MovementModule module) {
        this.module = module;
    }

    @Command("tpaccept")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tpaccept.help.description")
    public void tpaccept(@Source PlayerRef sender,
                          @Parameter
                          @ParameterHelp(description = "cmd.tpaccept.help.param.target.description", type = "cmd.tpaccept.help.param.target.type")
                          PlayerRef target) {

        TeleportRequest request = module.findTeleportRequest(target, sender).orElse(null);
        if ( request == null ) {
            module.plugin().localizer().send(sender, "cmd.tpaccept.error.no-request-exists", target.getUsername());
            return;
        }

        module.teleportRequestCancel(target, sender);

        if ( request.target() == TeleportRequest.TeleportRequestTarget.REQUESTEE ) {
            module.teleport(target, new Position(sender.getWorldUuid(), sender.getTransform()));
            module.plugin().localizer().send(target, "cmd.tprequest.teleport.teleportee", sender.getUsername());
            module.plugin().localizer().send(sender, "cmd.tprequest.teleport.destination", target.getUsername());
        } else {
            module.teleport(sender, new Position(target.getWorldUuid(), target.getTransform()));
            module.plugin().localizer().send(sender, "cmd.tprequest.teleport.teleportee", target.getUsername());
            module.plugin().localizer().send(target, "cmd.tprequest.teleport.destination", sender.getUsername());
        }
    }
}
