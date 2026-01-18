package com.gufli.hytale.toolbox.database.entity;

import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "players")
public class EPlayer {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @WhenCreated
    private Instant createdAt;

    @WhenModified
    private Instant updatedAt;

    private Instant seentAt;

    //

    private EPlayer() {
    }

    public EPlayer(@NotNull UUID id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant seentAt() {
        return seentAt;
    }

}