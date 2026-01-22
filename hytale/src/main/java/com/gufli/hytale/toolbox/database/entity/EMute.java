package com.gufli.hytale.toolbox.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Entity
@Table(name = "mutes")
public class EMute extends EPunishment {

    private EMute() {
        super();
    }

    public EMute(@NotNull EPlayer player, @Nullable EPlayer issuer, @NotNull String reason, @Nullable Instant expiresAt) {
        super(player, issuer, reason, expiresAt);
    }


}