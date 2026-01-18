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
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.Collection;

public class TeleportCancelCommand {

    private final TeleportModule module;

    public TeleportCancelCommand(TeleportModule module) {
        this.module = module;
    }

    @Command("tpcancel")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tpcancel.help.description")
    public void tpcancel(@Source PlayerRef sender,
                          @Parameter
                          @ParameterHelp(description = "cmd.tpcancel.help.param.target.description", type = "cmd.tpcancel.help.param.target.type")
                          PlayerRef target) {

        TeleportRequest request = module.findTeleportRequest(sender, target).orElse(null);
        if ( request == null ) {
            module.plugin().localizer().send(sender, "cmd.tpcancel.error.no-request-exists", target.getUsername());
            return;
        }

        module.teleportRequestCancel(sender, target);
        module.plugin().localizer().send(sender, "cmd.tpcancel.requester", target.getUsername());
        module.plugin().localizer().send(target, "cmd.tpcancel.requestee", sender.getUsername());
    }

    @Command("tpcancel")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tpcancel.help.description")
    public void tpcancel(@Source PlayerRef sender) {
        Collection<TeleportRequest> requests = module.findTeleportRequestsByRequester(sender);
        if ( requests.isEmpty() ) {
            module.plugin().localizer().send(sender, "cmd.tpcancel.error.no-request-exists");
            return;
        }
        if ( requests.size() > 1 ) {
            module.plugin().localizer().send(sender, "cmd.tpcancel.error.multiple-requests");
            return;
        }

        TeleportRequest request = requests.iterator().next();
        PlayerRef target = Universe.get().getPlayer(request.requester());
        if ( target == null ) {
            module.plugin().localizer().send(sender, "cmd.tpcancel.error.no-request-exists");
            return;
        }

        module.teleportRequestCancel(sender, target);
        module.plugin().localizer().send(sender, "cmd.tpcancel.requester", target.getUsername());
        module.plugin().localizer().send(target, "cmd.tpcancel.requestee", sender.getUsername());
    }


}
