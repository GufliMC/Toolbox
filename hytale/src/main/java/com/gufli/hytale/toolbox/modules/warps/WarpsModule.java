package com.gufli.hytale.toolbox.modules.warps;

import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.database.entity.EWarp;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.teleport.data.Position;
import com.gufli.hytale.toolbox.modules.warps.arguments.WarpArguments;
import com.gufli.hytale.toolbox.modules.warps.commands.WarpCommand;
import com.gufli.hytale.toolbox.modules.warps.commands.WarpRemoveCommand;
import com.gufli.hytale.toolbox.modules.warps.commands.WarpSetCommand;
import com.gufli.hytale.toolbox.modules.warps.commands.WarpListCommand;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class WarpsModule extends AbstractModule {

    private final Set<EWarp> warps = new CopyOnWriteArraySet<>();

    public WarpsModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        registerCommands(new WarpArguments(this));
        registerCommands(new WarpCommand(this));
        registerCommands(new WarpSetCommand(this));
        registerCommands(new WarpRemoveCommand(this));
        registerCommands(new WarpListCommand(this));

        plugin.database().findAllAsync(EWarp.class).thenAccept(this.warps::addAll);
    }

    public void teleport(@NotNull PlayerRef player, @NotNull EWarp warp) {
        plugin().module(TeleportModule.class).teleport(player, new Position(warp.worldId(), warp.position()));
    }

    public Collection<EWarp> warps() {
        return Collections.unmodifiableSet(warps);
    }

    public Optional<EWarp> warp(@NotNull String name) {
        return warps.stream()
                .filter(warp -> warp.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public EWarp addWarp(@NotNull String name, @NotNull UUID worldId, @NotNull Transform position) {
        EWarp warp = warp(name).orElse(null);
        if ( warp == null ) {
            warp = new EWarp(name, worldId, position.clone());
            warps.add(warp);
        } else {
            warp.setLocation(worldId, position.clone());
        }
        plugin().database().persistAsync(warp);
        return warp;
    }

    public void removeWarp(EWarp warp) {
        warps.remove(warp);
        plugin().database().removeAsync(warp);
    }

}
