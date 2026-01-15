package com.gufli.hytale.toolbox.modules.chat;

import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.modules.chat.commands.AnnounceCommand;
import com.gufli.hytale.toolbox.modules.chat.commands.DirectMessageCommand;
import org.jetbrains.annotations.NotNull;

public class ChatModule extends AbstractModule {

    public ChatModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        registerCommands(new AnnounceCommand(plugin.localizer()));
        registerCommands(new DirectMessageCommand(plugin.localizer()));
    }
}
