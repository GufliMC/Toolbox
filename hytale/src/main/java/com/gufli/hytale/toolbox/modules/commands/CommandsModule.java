package com.gufli.hytale.toolbox.modules.commands;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.hypixel.hytale.server.core.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommandsModule extends AbstractModule {

    public CommandsModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        for (Map.Entry<String, CommandsConfig.CustomCommand> entry : config().custom.entrySet() ) {
            plugin().colonel().builder()
                    .path(entry.getKey())
                    .executor(ctx -> {
                        String output = entry.getValue().output.replace("\\n", "\n");
                        Message result = plugin().localizer().recursiveLocalizeMessage(ctx.source(), output);
                        ctx.source().sendMessage(result);
                    })
                    .property("permission", "gufli.toolbox.command." + entry.getKey())
                    .register();
        }
    }

    public CommandsConfig config() {
        return plugin().config().commands;
    }

}
