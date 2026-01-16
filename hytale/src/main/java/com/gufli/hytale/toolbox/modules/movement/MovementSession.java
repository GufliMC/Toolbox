package com.gufli.hytale.toolbox.modules.movement;

import com.hypixel.hytale.math.vector.Transform;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class MovementSession {

    private final List<Position> history = new CopyOnWriteArrayList<>();
    private final List<Teleport> teleports = new CopyOnWriteArrayList<>();

    public void add(@NotNull UUID worldId, @NotNull Transform transform) {
        var previous = !history.isEmpty() ? history.getLast() : null;
        if ( previous != null && previous.worldId().equals(worldId) && Math.sqrt(previous.transform().getPosition().distanceSquaredTo(transform.getPosition())) < 0.1 ) {
            this.history.remove(previous);
        }

        var current = new Position(worldId, transform.clone());

        this.history.add(current);

        if ( previous != null && (!previous.worldId().equals(worldId) || Math.sqrt(previous.transform().getPosition().distanceSquaredTo(transform.getPosition())) > 10) ) {
            this.teleports.add(new Teleport(previous, current));
        }

        while ( this.history.size() > 60 ) {
            this.history.removeFirst();
        }

        while ( this.teleports.size() > 60 ) {
            this.teleports.removeFirst();
        }
    }

    public Optional<Teleport> previousTeleport() {
        if ( teleports.isEmpty() ) {
            return Optional.empty();
        }
        return Optional.of(teleports.getLast());
    }

    public Position previousPosition() {
        return history.getLast();
    }

}
