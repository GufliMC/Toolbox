package com.gufli.hytale.toolbox.modules.information;

import com.gufli.brick.i18n.hytale.localization.HytaleMessageTools;
import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

public class InformationModule extends AbstractModule {

    public InformationModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        if ( config().joinQuit.enabled ) {
            plugin.getEventRegistry().registerGlobal(AddPlayerToWorldEvent.class, event -> {
                event.setBroadcastJoinMessage(false);
            });

            plugin.getEventRegistry().register(PlayerConnectEvent.class, event -> {
                String join = config().joinQuit.joinMessage;
                join = join.replace("{player}", event.getPlayerRef().getUsername());
                Universe.get().sendMessage(HytaleMessageTools.parse(join));
            });

            plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
                String quit = config().joinQuit.quitMessage;
                quit = quit.replace("{player}", event.getPlayerRef().getUsername());
                Universe.get().sendMessage(HytaleMessageTools.parse(quit));
            });
        }

        if ( config().motd.enabled ) {
            plugin.getEventRegistry().register(PlayerConnectEvent.class, event -> {
                String motd = config().motd.motd;
                motd = motd.replace("{player}", event.getPlayerRef().getUsername());
                event.getPlayerRef().sendMessage(HytaleMessageTools.parse(motd));
            });
        }
    }

    public InformationConfig config() {
        return this.plugin().config().information;
    }

}
