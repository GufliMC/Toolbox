package com.gufli.hytale.toolbox.modules.punishment.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EMute;
import com.gufli.hytale.toolbox.modules.information.InformationModule;
import com.gufli.hytale.toolbox.modules.punishment.PunishmentModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

public class UnmuteCommand {

    private final PunishmentModule module;

    public UnmuteCommand(@NotNull PunishmentModule module) {
        this.module = module;
    }

    @Command("unmute")
    @CommandHelp(description = "cmd.unmute.help.description")
    @Permission("gufli.toolbox.command.unmute")
    public void unmute(@Source CommandSender sender,
                       @Parameter("player")
                       @ParameterHelp(description = "cmd.unmute.help.param.player.description", type = "cmd.unmute.help.param.player.type")
                       String playerName) {
        InformationModule informationModule = module.plugin().module(InformationModule.class);
        informationModule.playerAsync(playerName).thenAccept(oplayer -> {
            var player = oplayer.orElse(null);
            if (player == null) {
                module.plugin().localizer().send(sender, "cmderr.args.player-not-found", playerName);
                return;
            }

            EMute mute = module.findActiveMute(player.id());
            if (mute == null) {
                module.plugin().localizer().send(sender, "cmd.unmute.error.player-not-muted", player.name());
                return;
            }

            module.unmute(player.id());

            module.plugin().localizer().send(sender, "cmd.unmute", player.name());

            var pr = Universe.get().getPlayer(player.id());
            if ( pr == null ) return;

            module.plugin().localizer().send(pr, "cmd.unmute.target");
        });
    }
}