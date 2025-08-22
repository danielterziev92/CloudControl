package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "product_locations",
        indexes = {
                @Index(name = "inx_product_locations_product_id", columnList = "product_id"),
                @Index(name = "inx_product_locations_zone", columnList = "zone"),
                @Index(name = "inx_product_locations_zone_series", columnList = "zone, series"),
                @Index(name = "inx_product_locations_zone_series_shelf", columnList = "zone, series, shelf"),
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_product_location_zone_series_shelf_level_position",
                        columnNames = {"zone", "series", "shelf", "level", "position"}
                ),

        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"product"})
public class ProductLocation {

    public static final int ZONE_MAX_LENGTH = 10;
    public static final int SERIES_MIN_VALUE = 1;
    public static final int SHELF_MAX_LENGTH = 10;
    public static final int LEVEL_MIN_VALUE = 0;
    public static final int POSITION_MIN_VALUE = 0;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Zone is required")
    @Size(max = ZONE_MAX_LENGTH, message = "Zone cannot exceed {max} characters")
    @Column(name = "zone", length = ZONE_MAX_LENGTH, nullable = false)
    private String zone;

    @NotNull(message = "Series is required")
    @Min(value = SERIES_MIN_VALUE, message = "Series must be greater than or equal to {value}")
    @Column(name = "series", nullable = false)
    private Integer series;

    @NotBlank(message = "Shelf is required")
    @Size(max = SHELF_MAX_LENGTH, message = "Shelf cannot exceed {max} characters")
    @Column(name = "shelf", length = SHELF_MAX_LENGTH, nullable = false)
    private String shelf;

    @NotNull(message = "Level is required")
    @Min(value = LEVEL_MIN_VALUE, message = "Level must be non-negative")
    @Column(name = "level", nullable = false)
    private Integer level;

    @NotNull(message = "Position is required")
    @Min(value = POSITION_MIN_VALUE, message = "Position must be non-negative")
    @Column(name = "position", nullable = false)
    private Integer position;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (zone != null) {
            zone = zone.trim();
        }

        if (shelf != null) {
            shelf = shelf.trim();
        }
    }
}
