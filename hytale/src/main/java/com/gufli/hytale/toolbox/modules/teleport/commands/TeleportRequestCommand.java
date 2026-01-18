package com.gufli.hytale.toolbox.modules.teleport.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.teleport.data.TeleportRequest;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class TeleportRequestCommand {

    private final TeleportModule module;

    public TeleportRequestCommand(TeleportModule module) {
        this.module = module;
    }

    @Command("tprequest")
    @Command("tpa")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tprequest.help.description")
    public void tprequest(@Source PlayerRef sender,
                          @Parameter
                          @ParameterHelp(description = "cmd.tprequest.help.param.target.description", type = "cmd.tprequest.help.param.target.type")
                          PlayerRef target) {

        TeleportRequest request = module.findTeleportRequest(sender, target).orElse(null);
        if ( request != null ) {
            module.plugin().localizer().send(sender, "cmd.tprequest.error.too-many-requests");
            return;
        }

        module.teleportRequest(sender, target, TeleportRequest.TeleportRequestTarget.REQUESTEE);
        module.plugin().localizer().send(sender, "cmd.tprequest.requester", target.getUsername());
        module.plugin().localizer().send(target, "cmd.tprequest.requestee", sender.getUsername());
    }
}
