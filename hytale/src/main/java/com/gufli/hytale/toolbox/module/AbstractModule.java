package com.gufli.hytale.toolbox.module;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import org.jetbrains.annotations.NotNull;

public class AbstractModule {

    private final ToolboxPlugin plugin;

    public AbstractModule(@NotNull ToolboxPlugin plugin) {
        this.plugin = plugin;
    }

    public final ToolboxPlugin plugin() {
        return plugin;
    }

    protected final void registerCommands(@NotNull Object container) {
        this.plugin.colonel().registerAll(container);
    }
}
