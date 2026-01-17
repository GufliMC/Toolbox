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
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.Collection;

public class TeleportDenyCommand {

    private final MovementModule module;

    public TeleportDenyCommand(MovementModule module) {
        this.module = module;
    }

    @Command("tpdeny")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tpdeny.help.description")
    public void tpdeny(@Source PlayerRef sender,
                          @Parameter
                          @ParameterHelp(description = "cmd.tpdeny.help.param.target.description", type = "cmd.tpdeny.help.param.target.type")
                          PlayerRef target) {

        TeleportRequest request = module.findTeleportRequest(target, sender).orElse(null);
        if ( request == null ) {
            module.plugin().localizer().send(sender, "cmd.tpaccept.error.no-request-exists", target.getUsername());
            return;
        }

        module.teleportRequestCancel(target, sender);
        module.plugin().localizer().send(target, "cmd.tpdeny.requester", sender.getUsername());
        module.plugin().localizer().send(sender, "cmd.tpdeny.requestee", target.getUsername());
    }

    @Command("tpdeny")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tpaccept.help.description")
    public void tpdeny(@Source PlayerRef sender) {
        Collection<TeleportRequest> requests = module.findTeleportRequestsByRequestee(sender);
        if ( requests.isEmpty() ) {
            module.plugin().localizer().send(sender, "cmd.tpaccept.error.no-request-exists");
            return;
        }
        if ( requests.size() > 1 ) {
            module.plugin().localizer().send(sender, "cmd.tpaccept.error.multiple-requests");
            return;
        }

        TeleportRequest request = requests.iterator().next();
        PlayerRef target = Universe.get().getPlayer(request.requester());
        if ( target == null ) {
            module.plugin().localizer().send(sender, "cmd.tpaccept.error.no-request-exists");
            return;
        }

        module.teleportRequestCancel(target, sender);
        module.plugin().localizer().send(target, "cmd.tpdeny.requester", sender.getUsername());
        module.plugin().localizer().send(sender, "cmd.tpdeny.requestee", target.getUsername());
    }
}
