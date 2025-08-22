package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "product_barcodes",
        indexes = {
                @Index(name = "inx_product_barcodes_barcode", columnList = "barcode"),
                @Index(name = "inx_product_barcodes_product_id", columnList = "product_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_product_barcode_barcode_product", columnNames = {"barcode", "product_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"product"})
public class ProductBarcode {

    public static final int BARCODE_MAX_LENGTH = 128;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Barcode is required")
    @Size(max = BARCODE_MAX_LENGTH, message = "Barcode cannot exceed {max} characters")
    @Column(name = "barcode", length = BARCODE_MAX_LENGTH, nullable = false)
    private String barcode;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (barcode != null) {
            barcode = barcode.trim();
        }
    }
}
