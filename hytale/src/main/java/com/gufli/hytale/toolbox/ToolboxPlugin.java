package com.gufli.hytale.toolbox;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.hytale.HytaleColonel;
import com.gufli.hytale.toolbox.database.ToolboxDatabaseContext;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.admin.AdminModule;
import com.gufli.hytale.toolbox.modules.chat.ChatModule;
import com.gufli.hytale.toolbox.modules.commands.CommandsModule;
import com.gufli.hytale.toolbox.modules.information.InformationModule;
import com.gufli.hytale.toolbox.modules.item.ItemModule;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.warmup.WarmupModule;
import com.gufli.hytale.toolbox.modules.warps.WarpsModule;
import com.gufli.hytale.toolbox.modules.homes.HomesModule;
import com.gufli.hytale.toolbox.scheduler.AsyncScheduler;
import com.gufli.hytale.toolbox.scheduler.BrickThreadPoolAsyncScheduler;
import com.gufli.config.toml.TomlConfig;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ToolboxPlugin extends JavaPlugin {

    private final HytaleLocalizer localizer = new HytaleLocalizer(this, Locale.ENGLISH);
    private final HytaleColonel colonel = new HytaleColonel(this, localizer);
    private final AsyncScheduler scheduler = new BrickThreadPoolAsyncScheduler("toolbox");

    private ToolboxDatabaseContext database;
    private ToolboxConfig config = new ToolboxConfig();

    private final Set<AbstractModule> modules = new CopyOnWriteArraySet<>();

    public ToolboxPlugin(@NotNull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        config = TomlConfig.load(getDataDirectory().resolve("config.toml"), new ToolboxConfig());
        database = new ToolboxDatabaseContext(config.database);

        localizer.registerLocales(this);
        localizer.hookClientSideTranslations();

        setupModules();

        colonel.init();
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        database.shutdown();
    }

    private void setupModules() {
        this.modules.add(new TeleportModule(this));
        this.modules.add(new ChatModule(this));
        this.modules.add(new ItemModule(this));
        this.modules.add(new CommandsModule(this));
        this.modules.add(new InformationModule(this));
        this.modules.add(new AdminModule(this));
        this.modules.add(new WarpsModule(this));
        this.modules.add(new WarmupModule(this));
        this.modules.add(new HomesModule(this));
    }

    //

    public HytaleColonel colonel() {
        return colonel;
    }

    public HytaleLocalizer localizer() {
        return localizer;
    }

    public AsyncScheduler scheduler() {
        return scheduler;
    }

    public ToolboxConfig config() {
        return config;
    }

    public ToolboxDatabaseContext database() {
        return database;
    }

    public <T extends AbstractModule> T module(Class<T> type) {
        return this.modules.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow();
    }
}
