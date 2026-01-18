package com.gufli.hytale.toolbox.modules.movement.data;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public record TeleportRequest(@NotNull UUID requester, @NotNull UUID requestee, @NotNull TeleportRequestTarget target, @NotNull Instant timestamp) {

    public enum TeleportRequestTarget {
        REQUESTER,
        REQUESTEE,
    }
}
