package com.cloudcontrol.inventory.infrastructure.entities;

import com.cloudcontrol.inventory.domain.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_sku_active", columnList = "sku, is_active"),
                @Index(name = "idx_products_name_active", columnList = "name, is_active"),
                @Index(name = "idx_products_type_active", columnList = "type, is_active"),
                @Index(name = "idx_products_is_active", columnList = "is_active"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_product_sku", columnNames = "sku")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"createdAt", "updatedAt", "deletedAt"})
public class Product {

    public static final int SKU_MAX_LENGTH = 50;
    public static final int NAME_MAX_LENGTH = 300;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotBlank(message = "Product SKU is required")
    @Size(max = SKU_MAX_LENGTH, message = "Product SKU cannot exceed {max} characters")
    @Column(name = "sku", length = SKU_MAX_LENGTH, nullable = false)
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(max = NAME_MAX_LENGTH, message = "Product name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String name;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @NotNull(message = "Product type is required")
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private ProductType type = ProductType.getDefault();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    protected void normalizeData() {
        if (sku != null) {
            sku = sku.trim();
        }

        if (name != null) {
            name = name.trim();
        }
    }
}
