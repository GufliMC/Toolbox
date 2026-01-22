package com.gufli.hytale.toolbox.modules.punishment.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EBan;
import com.gufli.hytale.toolbox.modules.information.InformationModule;
import com.gufli.hytale.toolbox.modules.punishment.PunishmentModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand {

    private final PunishmentModule module;

    public UnbanCommand(@NotNull PunishmentModule module) {
        this.module = module;
    }

    @Command("unban")
    @CommandHelp(description = "cmd.unban.help.description")
    @Permission("gufli.toolbox.command.unban")
    public void unban(@Source
                      CommandSender sender,
                      @Parameter("player")
                      @ParameterHelp(description = "cmd.unban.help.param.player.description", type = "cmd.unban.help.param.player.type")
                      String playerName) {
        InformationModule informationModule = module.plugin().module(InformationModule.class);
        informationModule.playerAsync(playerName).thenAccept(oplayer -> {
            var player = oplayer.orElse(null);
            if (player == null) {
                module.plugin().localizer().send(sender, "cmderr.args.player-not-found", playerName);
                return;
            }

            EBan ban = module.findActiveBan(player.id());
            if (ban == null) {
                module.plugin().localizer().send(sender, "cmd.unban.error.player-not-banned", player.name());
                return;
            }

            module.unban(player.id());
            module.plugin().localizer().send(sender, "cmd.unban", player.name());
        });
    }
}
