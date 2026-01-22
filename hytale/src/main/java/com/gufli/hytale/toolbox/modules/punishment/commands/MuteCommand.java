package com.gufli.hytale.toolbox.modules.punishment.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.database.entity.EMute;
import com.gufli.hytale.toolbox.modules.punishment.PunishmentModule;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Locale;

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
        this.mute(target, sender, reason, null);
    }

    @Command("mute")
    @CommandHelp(description = "cmd.mute.help.description")
    @Permission("gufli.toolbox.command.mute")
    public void mute(@Source
                     CommandSender sender,
                     @Parameter
                     @ParameterHelp(description = "cmd.mute.help.param.player.description", type = "cmd.mute.help.param.player.type")
                     PlayerRef target) {
        this.mute(target, sender, "", null);
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
                         Duration duration,
                         @Parameter
                         @ParameterHelp(description = "cmd.tempmute.help.param.reason.description", type = "cmd.tempmute.help.param.reason.type")
                         String reason) {
        this.mute(target, sender, reason, duration);
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
                         Duration duration) {
        this.mute(target, sender, "", duration);
    }

    private void mute(@NotNull PlayerRef target, @NotNull CommandSender sender, @NotNull String reason, @Nullable Duration duration) {
        EMute mute = module.findActiveMute(target.getUuid());
        if (mute != null) {
            module.plugin().localizer().send(sender, "cmd.mute.error.player-already-muted", target.getUsername());
            return;
        }

        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.mute(target, issuer, reason, duration);

        if ( duration != null ) {
            String d = module.plugin().localizer().localize(Locale.ENGLISH, duration);
            module.plugin().localizer().send(sender, "cmd.tempmute", target.getUsername(), d, reason);
            module.plugin().localizer().send(target, "punishment.mute.chat.temporary", d, reason);
        } else {
            module.plugin().localizer().send(sender, "cmd.mute", target.getUsername(), reason);
            module.plugin().localizer().send(target, "punishment.mute.chat.permanent", reason);
        }
    }

}