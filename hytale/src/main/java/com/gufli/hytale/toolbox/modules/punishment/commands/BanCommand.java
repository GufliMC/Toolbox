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
import org.jetbrains.annotations.Nullable;

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
        this.ban(target, sender, reason, null);
    }

    @Command("ban")
    @CommandHelp(description = "cmd.ban.help.description")
    @Permission("gufli.toolbox.command.ban")
    public void ban(@Source
                    CommandSender sender,
                    @Parameter
                    @ParameterHelp(description = "cmd.ban.help.param.player.description", type = "cmd.ban.help.param.player.type")
                    PlayerRef target) {
        this.ban(target, sender, "", null);
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
                        Duration duration,
                        @Parameter
                        @ParameterHelp(description = "cmd.tempban.help.param.reason.description", type = "cmd.tempban.help.param.reason.type")
                        String reason) {
        this.ban(target, sender, reason, duration);
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
                        Duration duration) {
        this.ban(target, sender, "", duration);
    }

    private void ban(@NotNull PlayerRef target, @NotNull CommandSender sender, @NotNull String reason, @Nullable Duration duration) {
        PlayerRef issuer = null;
        if (sender instanceof PlayerRef) {
            issuer = (PlayerRef) sender;
        }

        module.ban(target, issuer, "", duration);

        if ( duration != null ) {
            String d = module.plugin().localizer().localize(Locale.ENGLISH, duration);
            module.plugin().localizer().send(sender, "cmd.tempban", target.getUsername(), d);
        } else {
            module.plugin().localizer().send(sender, "cmd.ban", target.getUsername());
        }

    }

}
