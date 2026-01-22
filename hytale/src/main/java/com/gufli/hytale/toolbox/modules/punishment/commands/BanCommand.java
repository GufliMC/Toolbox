package com.gufli.hytale.toolbox.modules.punishment.commands;

import com.gufli.brick.i18n.common.time.DurationParser;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.punishment.PunishmentModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Locale;

public class BanCommand {

    private final PunishmentModule module;

    public BanCommand(@NotNull PunishmentModule module) {
        this.module = module;
    }

    @Command("ban")
    @CommandHelp(description = "cmd.ban.help.description")
    @Permission("gufli.toolbox.command.ban")
    public void ban(@Source
                    CommandSender sender,
                    @Parameter
                    @ParameterHelp(description = "cmd.ban.help.param.player.description", type = "cmd.ban.help.param.player.type")
                    PlayerRef target,
                    @Parameter
                    @ParameterHelp(description = "cmd.ban.help.param.reason.description", type = "cmd.ban.help.param.reason.type")
                    String reason) {
        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.ban(target, issuer, reason, null);
        module.plugin().localizer().send(sender, "cmd.ban", target.getUsername());
    }

    @Command("ban")
    @CommandHelp(description = "cmd.ban.help.description")
    @Permission("gufli.toolbox.command.ban")
    public void ban(@Source
                    CommandSender sender,
                    @Parameter
                    @ParameterHelp(description = "cmd.ban.help.param.player.description", type = "cmd.ban.help.param.player.type")
                    PlayerRef target) {
        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.ban(target, issuer, "", null);
        module.plugin().localizer().send(sender, "cmd.ban", target.getUsername());
    }

    @Command("tempban")
    @CommandHelp(description = "cmd.tempban.help.description")
    @Permission("gufli.toolbox.command.tempban")
    public void tempban(@Source
                        CommandSender sender,
                        @Parameter
                        @ParameterHelp(description = "cmd.tempban.help.param.player.description", type = "cmd.tempban.help.param.player.type")
                        PlayerRef target,
                        @Parameter
                        @ParameterHelp(description = "cmd.tempban.help.param.duration.description", type = "cmd.tempban.help.param.duration.type", examples = {"1d", "2h30m"})
                        String durationStr,
                        @Parameter
                        @ParameterHelp(description = "cmd.tempban.help.param.reason.description", type = "cmd.tempban.help.param.reason.type")
                        String reason) {
        Duration duration = DurationParser.parse(durationStr);
        if ( duration.isZero() ) {
            module.plugin().localizer().send(sender, "cmderr.args.invalid-duration", durationStr);
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.ban(target, issuer, reason, duration);

        String d = module.plugin().localizer().localize(Locale.ENGLISH, duration);
        module.plugin().localizer().send(sender, "cmd.tempban", target.getUsername(), d, reason);
    }

    @Command("tempban")
    @CommandHelp(description = "cmd.tempban.help.description")
    @Permission("gufli.toolbox.command.tempban")
    public void tempban(@Source
                        CommandSender sender,
                        @Parameter
                        @ParameterHelp(description = "cmd.tempban.help.param.player.description", type = "cmd.tempban.help.param.player.type")
                        PlayerRef target,
                        @Parameter
                        @ParameterHelp(description = "cmd.tempban.help.param.duration.description", type = "cmd.tempban.help.param.duration.type", examples = {"1d", "2h30m"})
                        String durationStr) {
        Duration duration = DurationParser.parse(durationStr);
        if ( duration.isZero() ) {
            module.plugin().localizer().send(sender, "cmderr.args.invalid-duration", durationStr);
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.ban(target, issuer, "", duration);

        String d = module.plugin().localizer().localize(Locale.ENGLISH, duration);
        module.plugin().localizer().send(sender, "cmd.tempban", target.getUsername(), d);
    }

}
