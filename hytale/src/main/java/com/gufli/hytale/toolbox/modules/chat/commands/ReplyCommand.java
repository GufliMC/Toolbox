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

public class ReplyCommand {

    private final ChatModule module;

    public ReplyCommand(ChatModule module) {
        this.module = module;
    }

    @Command("reply")
    @Command("r")
    @Permission("gufli.toolbox.command.directmessage")
    @CommandHelp(description = "cmd.reply.help.description")
    public void reply(@Source PlayerRef sender,
                      @Parameter(read = ReadMode.GREEDY)
                      @ParameterHelp(description = "cmd.reply.help.param.message.description", type = "cmd.reply.help.param.message.type")
                      String message) {
        PlayerRef target = module.replyToTarget(sender).orElse(null);
        if (target == null) {
            module.plugin().localizer().send(sender, "cmd.reply.no-reply-target");
            return;
        }

        module.directMessage(sender, target, message);
    }

}
