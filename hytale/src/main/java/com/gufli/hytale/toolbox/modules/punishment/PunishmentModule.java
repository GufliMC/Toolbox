package com.gufli.hytale.toolbox.modules.punishment;

import com.gufli.brick.i18n.hytale.localization.HytaleMessageTools;
import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.database.entity.EBan;
import com.gufli.hytale.toolbox.database.entity.EMute;
import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.modules.information.InformationModule;
import com.gufli.hytale.toolbox.modules.punishment.commands.BanCommand;
import com.gufli.hytale.toolbox.modules.punishment.commands.MuteCommand;
import com.gufli.hytale.toolbox.modules.punishment.commands.UnbanCommand;
import com.gufli.hytale.toolbox.modules.punishment.commands.UnmuteCommand;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class PunishmentModule extends AbstractModule {

    private final Set<EMute> mutes = new CopyOnWriteArraySet<>();
    private final Set<EBan> bans = new CopyOnWriteArraySet<>();

    private final InformationModule informationModule = plugin().module(InformationModule.class);

    public PunishmentModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);

        // Register commands
        registerCommands(new BanCommand(this));
        registerCommands(new MuteCommand(this));
        registerCommands(new UnbanCommand(this));
        registerCommands(new UnmuteCommand(this));

        plugin.database().findAllAsync(EMute.class).thenAccept(this.mutes::addAll);
        plugin.database().findAllAsync(EBan.class).thenAccept(this.bans::addAll);

        // events
        plugin.getEventRegistry().registerGlobal(PlayerChatEvent.class, event -> {
            PlayerRef player = event.getSender();
            EMute mute = findActiveMute(player.getUuid());
            if (mute == null) {
                return;
            }

            event.setCancelled(true);

            if (mute.isPermanent()) {
                plugin().localizer().send(player, "punishment.mute.chat.permanent");
            } else {
                var remaining = Duration.between(Instant.now(), mute.expiresAt());
                String duration = plugin().localizer().localize(player, remaining);
                plugin().localizer().send(player, "punishment.mute.chat.temporary", duration);
            }
        });

        plugin.getEventRegistry().register(PlayerSetupConnectEvent.class, event -> {
            EBan ban = findActiveBan(event.getUuid());
            if (ban == null) {
                return;
            }

            event.setCancelled(true);

            String message;
            if (ban.isPermanent()) {
                message = plugin().localizer().localize(Locale.ENGLISH, "punishment.ban.connect.permanent");
            } else {
                var remaining = Duration.between(Instant.now(), ban.expiresAt());
                String duration = plugin().localizer().localize(Locale.ENGLISH, remaining);
                message = plugin().localizer().localize(Locale.ENGLISH, "punishment.ban.connect.temporary", duration);
            }

            event.setReason(HytaleMessageTools.strip(message));
        });
    }

    //

    public EMute mute(@NotNull PlayerRef player, @Nullable PlayerRef issuer, @NotNull String reason, @Nullable Duration duration) {
        var eplayer = informationModule.player(player.getUuid()).orElseThrow();
        var eissuer = issuer != null ? informationModule.player(issuer.getUuid()).orElseThrow() : null;

        Instant expireAt = duration == null ? null : Instant.now().plus(duration);
        EMute mute = new EMute(eplayer, eissuer, reason, expireAt);

        mutes.add(mute);
        plugin().database().persistAsync(mute);
        return mute;
    }

    public void unmute(@NotNull UUID player) {
        EMute mute = findActiveMute(player);
        if (mute == null) {
            return;
        }

        mute.cancel();
        plugin().database().persistAsync(mute);
    }

    public EMute findActiveMute(@NotNull UUID playerId) {
        return mutes.stream()
                .filter(mute -> mute.player().id().equals(playerId) && mute.isActive())
                .findFirst()
                .orElse(null);
    }

    //

    public EBan ban(@NotNull PlayerRef player, @Nullable PlayerRef issuer, @NotNull String reason, @Nullable Duration duration) {
        var eplayer = informationModule.player(player.getUuid()).orElseThrow();
        var eissuer = issuer != null ? informationModule.player(issuer.getUuid()).orElseThrow() : null;

        Instant expireAt = duration == null ? null : Instant.now().plus(duration);
        EBan ban = new EBan(eplayer, eissuer, reason, expireAt);

        bans.add(ban);
        plugin().database().persistAsync(ban);


        String message;
        if (ban.isPermanent()) {
            message = plugin().localizer().localize(player, "punishment.ban.connect.permanent");
        } else {
            var remaining = Duration.between(Instant.now(), ban.expiresAt());
            String d = plugin().localizer().localize(player, remaining);
            message = plugin().localizer().localize(player, "punishment.ban.connect.temporary", d);
        }
        player.getPacketHandler().disconnect(HytaleMessageTools.strip(message));

        return ban;
    }

    public void unban(@NotNull UUID player) {
        EBan ban = findActiveBan(player);
        if (ban == null) {
            return;
        }

        ban.cancel();
        plugin().database().persistAsync(ban);
    }

    public EBan findActiveBan(@NotNull UUID playerId) {
        return bans.stream()
                .filter(ban -> ban.player().id().equals(playerId) && ban.isActive())
                .findFirst()
                .orElse(null);
    }

}
