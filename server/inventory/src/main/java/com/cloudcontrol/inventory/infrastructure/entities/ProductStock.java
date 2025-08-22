package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(
        name = "product_stocks",
        indexes = {
                @Index(name = "idx_product_stocks_product_id", columnList = "product_id"),
                @Index(name = "idx_product_stocks_lot_id", columnList = "lot_id"),
                @Index(name = "idx_product_stocks_object_id", columnList = "object_id"),
                @Index(name = "idx_product_stocks_product_object", columnList = "product_id, object_id"),
                @Index(name = "idx_product_stocks_product_object_lot", columnList = "product_id, object_id, lot_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_product_stock_product_object_lot",
                        columnNames = {"product_id", "object_id", "lot_id"}
                ),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"product", "lot", "object"})
public class ProductStock {

    public static final int QUANTITY_PRECISION = 15;
    public static final int QUANTITY_SCALE = 8;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Long version;

    @NotNull(message = "Quantity is required")
    @Column(name = "quantity", precision = QUANTITY_PRECISION, scale = QUANTITY_SCALE, nullable = false)
    private BigDecimal quantity;

    @NotNull(message = "Object is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", nullable = false)
    private Object object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private ProductLot lot;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (quantity != null) {
            quantity = quantity.setScale(QUANTITY_SCALE, RoundingMode.HALF_UP);
        }
    }
}
