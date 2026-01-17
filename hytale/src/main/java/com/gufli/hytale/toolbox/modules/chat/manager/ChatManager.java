package com.gufli.hytale.toolbox.modules.chat.manager;

import com.gufli.brick.i18n.hytale.localization.HytaleMessageTools;
import com.gufli.hytale.toolbox.modules.chat.ChatModule;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatManager {

    private final ChatModule module;

    public ChatManager(@NotNull ChatModule module) {
        this.module = module;

        module.plugin().getEventRegistry().registerGlobal(PlayerChatEvent.class, event -> {
            ChatResult result = dispatch(event.getSender(), event.getTargets(), event.getContent());
            event.setFormatter(result.formatter());
            event.setTargets(event.getTargets());
        });
    }

    public ChatResult dispatch(@NotNull PlayerRef player, @NotNull List<PlayerRef> targets, @NotNull String message) {
        return new ChatResult(player, targets, (p, s) -> {
            String format = module.config().chatFormat;
            format = format.replace("{player}", p.getUsername());
            format = format.replace("{message}", s);
            return HytaleMessageTools.parse(format);
        });
    }
}
