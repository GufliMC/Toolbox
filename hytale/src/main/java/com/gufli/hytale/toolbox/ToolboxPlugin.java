package com.gufli.hytale.toolbox;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.hytale.HytaleColonel;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.chat.ChatModule;
import com.gufli.hytale.toolbox.modules.commands.CommandsModule;
import com.gufli.hytale.toolbox.modules.item.ItemModule;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
import com.gufli.hytale.toolbox.scheduler.AsyncScheduler;
import com.gufli.hytale.toolbox.scheduler.BrickThreadPoolAsyncScheduler;
import com.gufli.config.toml.TomlConfig;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
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

    private ToolboxConfig config = new ToolboxConfig();

    private final Set<AbstractModule> modules = new CopyOnWriteArraySet<>();

    public ToolboxPlugin(@NotNull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        config = TomlConfig.load(getDataDirectory().resolve("config.toml"), new ToolboxConfig());

        localizer.registerLocales(this);

        setupModules();

        colonel.init();

        EventRegistry eventRegistry = getEventRegistry();
        eventRegistry.registerGlobal(PlayerConnectEvent.class, event -> {
            I18nModule i18n = I18nModule.get();
            if (i18n != null && event.getPlayerRef() != null) {
                i18n.sendTranslations(event.getPlayerRef().getPacketHandler(), event.getPlayerRef().getLanguage());
            }
        });
    }

    private void setupModules() {
        this.modules.add(new MovementModule(this));
        this.modules.add(new ChatModule(this));
        this.modules.add(new ItemModule(this));
        this.modules.add(new CommandsModule(this));
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

    public <T extends AbstractModule> T module(Class<T> type) {
        return this.modules.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow();
    }
}
