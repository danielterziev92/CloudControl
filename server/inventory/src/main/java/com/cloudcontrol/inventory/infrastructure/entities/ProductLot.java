package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_lots",
        indexes = {
                @Index(name = "inx_product_lots_product_id", columnList = "product_id"),
                @Index(name = "inx_product_lots_lot", columnList = "lot"),
                @Index(name = "inx_product_lots_date_of_expiry", columnList = "date_of_expiry")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_product_lots_product_id_lot", columnNames = {"product_id", "lot"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"product", "location"})
public class ProductLot {

    public static final int LOT_MAX_LENGTH = 50;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Lot is required")
    @Size(max = LOT_MAX_LENGTH, message = "Lot cannot exceed {max} characters")
    @Column(name = "lot", length = LOT_MAX_LENGTH, nullable = false)
    private String lot;

    @Column(name = "date_of_issue")
    private LocalDateTime dateOfIssue;

    @Column(name = "date_of_expiry")
    private LocalDateTime dateOfExpiry;

    @NotNull(message = "Location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private ProductLocation location;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (lot != null) {
            lot = lot.trim();
        }
    }
}
