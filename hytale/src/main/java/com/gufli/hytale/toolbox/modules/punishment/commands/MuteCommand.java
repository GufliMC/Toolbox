package com.gufli.hytale.toolbox.modules.punishment.commands;

import com.gufli.brick.i18n.common.time.DurationParser;
import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EMute;
import com.gufli.hytale.toolbox.database.entity.EPlayer;
import com.gufli.hytale.toolbox.modules.information.InformationModule;
import com.gufli.hytale.toolbox.modules.punishment.PunishmentModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

public class MuteCommand {

    private final PunishmentModule module;

    public MuteCommand(@NotNull PunishmentModule module) {
        this.module = module;
    }

    @Command("mute")
    @CommandHelp(description = "cmd.mute.help.description")
    @Permission("gufli.toolbox.command.mute")
    public void mute(@Source
                     CommandSender sender,
                     @Parameter
                     @ParameterHelp(description = "cmd.mute.help.param.player.description", type = "cmd.mute.help.param.player.type")
                     PlayerRef target,
                     @Parameter
                     @ParameterHelp(description = "cmd.mute.help.param.reason.description", type = "cmd.mute.help.param.reason.type")
                     String reason) {
        EMute mute = module.findActiveMute(target.getUuid());
        if (mute == null) {
            module.plugin().localizer().send(sender, "cmd.mute.error.player-already-muted", target.getUsername());
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.mute(target, issuer, reason, null);

        module.plugin().localizer().send(sender, "cmd.mute", target.getUsername(), reason);
        module.plugin().localizer().send(target, "punishment.mute.chat.permanent", reason);
    }

    @Command("mute")
    @CommandHelp(description = "cmd.mute.help.description")
    @Permission("gufli.toolbox.command.mute")
    public void mute(@Source
                     CommandSender sender,
                     @Parameter
                     @ParameterHelp(description = "cmd.mute.help.param.player.description", type = "cmd.mute.help.param.player.type")
                     PlayerRef target) {
        EMute mute = module.findActiveMute(target.getUuid());
        if (mute == null) {
            module.plugin().localizer().send(sender, "cmd.mute.error.player-already-muted", target.getUsername());
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.mute(target, issuer, "", null);

        module.plugin().localizer().send(sender, "cmd.mute", target.getUsername());
        module.plugin().localizer().send(target, "punishment.mute.chat.permanent");
    }

    @Command("tempmute")
    @CommandHelp(description = "cmd.tempmute.help.description")
    @Permission("gufli.toolbox.command.tempmute")
    public void tempmute(@Source
                         CommandSender sender,
                         @Parameter
                         @ParameterHelp(description = "cmd.tempmute.help.param.player.description", type = "cmd.tempmute.help.param.player.type")
                         PlayerRef target,
                         @Parameter
                         @ParameterHelp(description = "cmd.tempmute.help.param.duration.description", type = "cmd.tempmute.help.param.duration.type", examples = { "1d", "2h30m" })
                         String durationStr,
                         @Parameter
                         @ParameterHelp(description = "cmd.tempmute.help.param.reason.description", type = "cmd.tempmute.help.param.reason.type")
                         String reason) {
        EMute mute = module.findActiveMute(target.getUuid());
        if (mute == null) {
            module.plugin().localizer().send(sender, "cmd.mute.error.player-already-muted", target.getUsername());
            return;
        }

        Duration duration = DurationParser.parse(durationStr);
        if ( duration.isZero() ) {
            module.plugin().localizer().send(sender, "cmderr.args.invalid-duration", durationStr);
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.mute(target, issuer, reason, duration);

        String d = module.plugin().localizer().localize(Locale.ENGLISH, duration);
        module.plugin().localizer().send(sender, "cmd.tempmute", target.getUsername(), d, reason);
        module.plugin().localizer().send(target, "punishment.mute.chat.temporary", d, reason);
    }

    @Command("tempmute")
    @CommandHelp(description = "cmd.tempmute.help.description")
    @Permission("gufli.toolbox.command.tempmute")
    public void tempmute(@Source
                         CommandSender sender,
                         @Parameter
                         @ParameterHelp(description = "cmd.tempmute.help.param.player.description", type = "cmd.tempmute.help.param.player.type")
                         PlayerRef target,
                         @Parameter
                         @ParameterHelp(description = "cmd.tempmute.help.param.duration.description", type = "cmd.tempmute.help.param.duration.type", examples = { "1d", "2h30m" })
                         String durationStr) {
        EMute mute = module.findActiveMute(target.getUuid());
        if (mute == null) {
            module.plugin().localizer().send(sender, "cmd.mute.error.player-already-muted", target.getUsername());
            return;
        }

        Duration duration = DurationParser.parse(durationStr);
        if ( duration.isZero() ) {
            module.plugin().localizer().send(sender, "cmderr.args.invalid-duration", durationStr);
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.mute(target, issuer, "", duration);

        String d = module.plugin().localizer().localize(Locale.ENGLISH, duration);
        module.plugin().localizer().send(sender, "cmd.tempmute", target.getUsername(), d);
        module.plugin().localizer().send(target, "punishment.mute.chat.temporary", d);
    }

}