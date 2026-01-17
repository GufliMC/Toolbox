package com.gufli.hytale.toolbox.modules.chat;

import com.gufli.hytale.toolbox.module.AbstractModule;
import com.gufli.hytale.toolbox.ToolboxPlugin;
import com.gufli.hytale.toolbox.modules.chat.commands.AnnounceCommand;
import com.gufli.hytale.toolbox.modules.chat.commands.DirectMessageCommand;
import com.gufli.hytale.toolbox.modules.chat.commands.PlayerListCommand;
import com.gufli.hytale.toolbox.modules.chat.commands.ReplyCommand;
import com.gufli.hytale.toolbox.modules.chat.manager.ChatManager;
import com.gufli.hytale.toolbox.modules.chat.manager.ChatResult;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatModule extends AbstractModule {

    private final Map<UUID, UUID> replyTo = new ConcurrentHashMap<>();

    @Nullable
    private ChatManager chatManager;

    public ChatModule(@NotNull ToolboxPlugin plugin) {
        super(plugin);


        registerCommands(new AnnounceCommand(plugin.localizer()));
        registerCommands(new DirectMessageCommand(this));
        registerCommands(new ReplyCommand(this));
        registerCommands(new PlayerListCommand(this));

        plugin.getEventRegistry().register(PlayerDisconnectEvent.class, event -> {
            replyTo.remove(event.getPlayerRef().getUuid());
        });

        if ( config().enabled ) {
            this.chatManager = new ChatManager(this);
        }
    }

    public ChatConfig config() {
        return plugin().config().chat;
    }

    public void directMessage(PlayerRef sender, PlayerRef target, String message) {
        plugin().localizer().send(sender, "cmd.directmessage.sender.format", target.getUsername(), message);
        plugin().localizer().send(target, "cmd.directmessage.target.format", sender.getUsername(), message);

        replyTo.put(target.getUuid(), sender.getUuid());
    }

    public Optional<PlayerRef> replyToTarget(PlayerRef sender) {
        UUID target = replyTo.get(sender.getUuid());
        if (target == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(Universe.get().getPlayer(target));
    }
}
