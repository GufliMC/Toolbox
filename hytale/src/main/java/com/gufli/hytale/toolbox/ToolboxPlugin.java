package com.gufli.hytale.toolbox;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.hytale.HytaleColonel;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.chat.ChatModule;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
import com.gufli.hytale.toolbox.scheduler.AsyncScheduler;
import com.gufli.hytale.toolbox.scheduler.BrickThreadPoolAsyncScheduler;
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


    private final Set<AbstractModule> modules = new CopyOnWriteArraySet<>();

    public ToolboxPlugin(@NotNull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        localizer.registerLocales(this);

        setupModules();

        colonel.init();
    }

    private void setupModules() {
        this.modules.add(new MovementModule(this));
        this.modules.add(new ChatModule(this));
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

    public <T extends AbstractModule> T module(Class<T> type) {
        return this.modules.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow();
    }
}
