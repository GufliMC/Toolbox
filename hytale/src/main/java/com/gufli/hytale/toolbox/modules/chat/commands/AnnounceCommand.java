package com.gufli.hytale.toolbox.modules.chat.commands;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.dispatch.definition.ReadMode;
import com.hypixel.hytale.server.core.command.system.CommandSender;

public class AnnounceCommand {

    private final HytaleLocalizer localizer;

    public AnnounceCommand(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    @Command("announce")
    public void announce(@Source CommandSender sender, @Parameter(read = ReadMode.GREEDY) String message) {
        localizer.send(sender, "cmd.announce.format", message);
    }

}
