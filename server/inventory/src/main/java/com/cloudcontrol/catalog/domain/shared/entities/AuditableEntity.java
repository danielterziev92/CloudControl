package com.cloudcontrol.catalog.domain.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class AuditableEntity<ID extends Serializable> extends Entity<ID> {
    @Column(name = "create_at", updatable = false)
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updatedAt;

    protected AuditableEntity(ID id) {
        super(id);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
