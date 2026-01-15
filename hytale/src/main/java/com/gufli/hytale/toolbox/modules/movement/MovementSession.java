package com.gufli.hytale.toolbox.modules.movement;

import com.hypixel.hytale.math.vector.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovementSession {

    private final List<Position> history = new ArrayList<>();
    private final List<Position> teleports = new ArrayList<>();

    public void add(UUID worldId, Transform transform) {
        var previous = !history.isEmpty() ? history.getLast() : null;
        if ( previous != null && previous.worldId().equals(worldId) && Math.sqrt(previous.transform().getPosition().distanceSquaredTo(transform.getPosition())) < 0.1 ) {
            this.history.removeLast();
        }

        this.history.add(new Position(worldId, transform.clone()));

        if ( previous != null && (!previous.worldId().equals(worldId) || Math.sqrt(previous.transform().getPosition().distanceSquaredTo(transform.getPosition())) > 25) ) {
            this.teleports.add(new Position(worldId, transform.clone()));
        }

        while ( this.history.size() > 60 ) {
            this.history.removeFirst();
        }

        while ( this.teleports.size() > 60 ) {
            this.teleports.removeFirst();
        }
    }

    public Optional<Position> lastTeleport() {
        if ( teleports.isEmpty() ) {
            return Optional.empty();
        }
        return Optional.ofNullable(teleports.getLast());
    }

    public Position lastPosition() {
        return history.getLast();
    }

}
