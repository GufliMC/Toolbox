package com.gufli.hytale.toolbox.modules.information;

import com.gufli.brick.i18n.hytale.localization.HytaleMessageTools;
import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.database.entity.EPlayer;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class InformationModule extends AbstractModule {

    private Set<EPlayer> players = new CopyOnWriteArraySet<>();

    public InformationModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        if (config().joinQuit.enabled) {
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

        if (config().motd.enabled) {
            plugin.getEventRegistry().register(PlayerConnectEvent.class, event -> {
                String motd = config().motd.motd;
                motd = motd.replace("{player}", event.getPlayerRef().getUsername());
                event.getPlayerRef().sendMessage(HytaleMessageTools.parse(motd));
            });
        }

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            players.removeIf(ep -> ep.id().equals(event.getPlayerRef().getUuid()));
        });

        plugin.getEventRegistry().register(PlayerConnectEvent.class, event -> {
            PlayerRef ref = event.getPlayerRef();
            plugin.database().findAsync(EPlayer.class, ref.getUuid()).thenAccept(player -> {
                if (player != null) {
                    players.add(player);
                } else {
                    player = new EPlayer(ref.getUuid(), ref.getUsername());
                    onFirstJoin(ref);
                }

                player.setSeentAt(Instant.now());
                plugin.database().persistAsync(player);
            });
        });
    }

    public InformationConfig config() {
        return this.plugin().config().information;
    }

    //

    private void onFirstJoin(@NotNull PlayerRef player) {
        if (!config().joinQuit.enabled) {
            return;
        }

        String join = config().joinQuit.joinFirstMessage;
        join = join.replace("{player}", player.getUsername());
        Universe.get().sendMessage(HytaleMessageTools.parse(join));
    }

    //

    public Optional<EPlayer> player(@NotNull UUID id) {
        return players.stream().filter(ep -> ep.id().equals(id)).findFirst();
    }

}
