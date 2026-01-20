package com.gufli.hytale.toolbox.modules.warmup.data;

import com.gufli.hytale.toolbox.scheduler.Countdown;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public record Warmup(@NotNull UUID player,
                     @NotNull Runnable executor,
                     @NotNull Countdown countdown,
                     @NotNull Consumer<WarmupCancellationReason> cancelled,
                     @NotNull WarmupCancellationReason... cancellationReasons) {
}
