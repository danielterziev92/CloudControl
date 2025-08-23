package com.cloudcontrol.inventory.infrastructure.entities;

import com.cloudcontrol.inventory.domain.enums.ProductPriceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product_prices",
        indexes = {
                @Index(name = "idx_product_prices_product_id", columnList = "product_id"),
                @Index(name = "idx_product_prices_type_product_id", columnList = "type, product_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_product_price_type_product", columnNames = {"type", "product_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"product"})
public class ProductPrice {

    public static final int PRICE_PRECISION = 15;
    public static final int PRICE_SCALE = 8;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotNull(message = "ProductPriceType is required")
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private ProductPriceType type;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column(name = "price", precision = PRICE_PRECISION, scale = PRICE_SCALE, nullable = false)
    private BigDecimal price;

    @NotNull(message = "Currency is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
