package com.gufli.hytale.toolbox.modules.teleport.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.teleport.data.Position;
import com.gufli.hytale.toolbox.modules.teleport.data.TeleportRequest;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TeleportAcceptCommand {

    private final TeleportModule module;

    public TeleportAcceptCommand(TeleportModule module) {
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
            module.plugin().localizer().send(sender, "cmd.tpaccept.error.no-request-exists-target", target.getUsername());
            return;
        }

        this.accept(target, sender, request);
    }

    @Command("tpaccept")
    @Permission("gufli.toolbox.command.tprequest")
    @CommandHelp(description = "cmd.tpaccept.help.description")
    public void tpaccept(@Source PlayerRef sender) {
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

        this.accept(target, sender, request);
    }

    private void accept(@NotNull PlayerRef requester, @NotNull PlayerRef requestee, @NotNull TeleportRequest request) {
        module.teleportRequestCancel(requester, requestee);

        if ( request.target() == TeleportRequest.TeleportRequestTarget.REQUESTEE ) {
            module.teleport(requester, new Position(requestee.getWorldUuid(), requestee.getTransform()));
            module.plugin().localizer().send(requester, "cmd.tprequest.teleport.teleportee", requestee.getUsername());
            module.plugin().localizer().send(requestee, "cmd.tprequest.teleport.destination", requester.getUsername());
        } else {
            module.teleport(requestee, new Position(requester.getWorldUuid(), requester.getTransform()));
            module.plugin().localizer().send(requestee, "cmd.tprequest.teleport.teleportee", requester.getUsername());
            module.plugin().localizer().send(requester, "cmd.tprequest.teleport.destination", requestee.getUsername());
        }
    }
}
