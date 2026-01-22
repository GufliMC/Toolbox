package com.gufli.hytale.toolbox.database.entity;

import io.ebean.annotation.WhenCreated;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class EPunishment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "playerId", nullable = false)
    private EPlayer player;

    @ManyToOne
    @JoinColumn(name = "issuerId")
    private EPlayer issuer;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = true)
    private Instant expiresAt;

    @Column(nullable = true)
    private Instant canceledAt;

    @WhenCreated
    private Instant createdAt;

    protected EPunishment() {
    }

    public EPunishment(@NotNull EPlayer player, @Nullable EPlayer issuer, @NotNull String reason, @Nullable Instant expiresAt) {
        this.player = player;
        this.issuer = issuer;
        this.reason = reason;
        this.expiresAt = expiresAt;
    }

    public UUID id() {
        return id;
    }

    public EPlayer player() {
        return player;
    }

    public EPlayer issuer() {
        return issuer;
    }

    public String reason() {
        return reason;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public Instant canceledAt() {
        return canceledAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public boolean isCanceled() {
        return canceledAt != null;
    }

    public boolean isActive() {
        return !isCanceled() && !isExpired();
    }

    public void cancel() {
        this.canceledAt = Instant.now();
    }

    public boolean isPermanent() {
        return expiresAt == null;
    }
}