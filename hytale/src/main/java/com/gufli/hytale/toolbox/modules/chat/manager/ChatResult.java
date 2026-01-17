package com.gufli.hytale.toolbox.modules.chat.manager;

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ChatResult(@NotNull PlayerRef sender,
                         @NotNull List<PlayerRef> targets,
                         @NotNull PlayerChatEvent.Formatter formatter) {
}
