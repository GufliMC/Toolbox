package com.gufli.hytale.toolbox.modules.chat.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.dispatch.definition.ReadMode;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.chat.ChatModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class DirectMessageCommand {

    private final ChatModule module;

    public DirectMessageCommand(ChatModule module) {
        this.module = module;
    }

    @Command("directmessage")
    @Command("dm")
    @Command("msg")
    @Permission("gufli.toolbox.command.directmessage")
    @CommandHelp(description = "cmd.directmessage.help.description")
    public void directmessage(@Source PlayerRef sender,
                              @Parameter
                              @ParameterHelp(description = "cmd.directmessage.help.param.target.description", type = "cmd.directmessage.help.param.target.type")
                              PlayerRef target,
                              @Parameter(read = ReadMode.GREEDY)
                              @ParameterHelp(description = "cmd.directmessage.help.param.message.description", type = "cmd.directmessage.help.param.message.type")
                              String message
    ) {
        module.directMessage(sender, target, message);
    }

}
