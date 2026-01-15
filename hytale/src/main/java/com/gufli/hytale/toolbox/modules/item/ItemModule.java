package com.gufli.hytale.toolbox.modules.item;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.item.commands.RepairCommand;
import org.jetbrains.annotations.NotNull;

public class ItemModule extends AbstractModule {

    public ItemModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        registerCommands(new RepairCommand(this));
    }

}
