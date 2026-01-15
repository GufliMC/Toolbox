package com.gufli.hytale.toolbox.modules.movement;

import com.hypixel.hytale.math.vector.Transform;

import java.util.UUID;

public record Position(UUID worldId, Transform transform) {
}