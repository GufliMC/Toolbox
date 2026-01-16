package com.gufli.hytale.toolbox.modules.movement.data;

import com.hypixel.hytale.math.vector.Transform;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Position(@NotNull UUID worldId, @NotNull Transform transform) {
}