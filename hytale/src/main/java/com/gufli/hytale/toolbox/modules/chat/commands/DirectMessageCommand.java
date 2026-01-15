package com.gufli.hytale.toolbox.modules.chat.commands;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.dispatch.definition.ReadMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class DirectMessageCommand {

    private final HytaleLocalizer localizer;

    public DirectMessageCommand(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    @Command("directmessage")
    @Command("dm")
    public void directmessage(@Source PlayerRef sender, @Parameter PlayerRef target, @Parameter(read = ReadMode.GREEDY) String message) {
        localizer.send(sender, "cmd.directmessage.sender.format", target.getUsername(), message);
        localizer.send(target, "cmd.directmessage.target.format", sender.getUsername(), message);
    }

}
