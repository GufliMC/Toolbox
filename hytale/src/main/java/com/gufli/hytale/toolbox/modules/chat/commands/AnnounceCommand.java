package com.gufli.hytale.toolbox.modules.chat.commands;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.dispatch.definition.ReadMode;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.Universe;

public class AnnounceCommand {

    private final HytaleLocalizer localizer;

    public AnnounceCommand(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    @Command("announce")
    @CommandHelp(description = "cmd.announce.help.description")
    @Permission("gufli.toolbox.command.announce")
    public void announce(@Source CommandSender sender,
                         @Parameter(read = ReadMode.GREEDY)
                         @ParameterHelp(description = "cmd.announce.help.param.message.description", type = "cmd.announce.help.param.message.type")
                         String message) {
        Universe.get().getPlayers().forEach(player -> localizer.send(player, "cmd.announce.format", message));
    }

}
