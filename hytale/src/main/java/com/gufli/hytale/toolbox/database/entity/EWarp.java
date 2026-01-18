package com.gufli.hytale.toolbox.database.entity;

import com.hypixel.hytale.math.vector.Transform;
import io.ebean.annotation.Index;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Index(unique = true, columnNames = {"name"})
@Table(name = "warps")
public class EWarp {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private UUID worldId;

    @NotNull
    private Transform position;

    @WhenCreated
    Instant createdAt;

    @WhenModified
    Instant updatedAt;

    private EWarp() {
    }

    public EWarp(@org.jetbrains.annotations.NotNull String name,
                 @org.jetbrains.annotations.NotNull UUID worldId,
                 @org.jetbrains.annotations.NotNull Transform position) {
        this.name = name;
        this.worldId = worldId;
        this.position = position;
    }

    //

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public UUID worldId() {
        return worldId;
    }

    public Transform position() {
        return position;
    }

    public void setLocation(@org.jetbrains.annotations.NotNull UUID worldId,
                            @org.jetbrains.annotations.NotNull Transform position) {
        this.worldId = worldId;
        this.position = position;
    }
}
