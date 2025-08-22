package com.cloudcontrol.inventory.infrastructure.entities;

import com.cloudcontrol.inventory.domain.enums.ProductIdentifierType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(
        name = "product_identifiers",
        indexes = {
                @Index(name = "inx_product_identifiers_value", columnList = "value"),
                @Index(name = "inx_product_identifiers_type_value", columnList = "type, value"),
                @Index(name = "inx_product_identifiers_product_id", columnList = "product_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_product_identifier_type_product_id", columnNames = {"type", "product_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"product"})
public class ProductIdentifier {

    public static final int TYPE_MIN = 0;
    public static final int TYPE_MAX = 4;
    public static final int VALUE_MAX_LENGTH = 50;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ProductIdentifierType is required")
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private ProductIdentifierType type;

    @NotBlank(message = "ProductIdentifier value is required")
    @Size(max = VALUE_MAX_LENGTH, message = "ProductIdentifier value cannot exceed {max} characters")
    @Column(name = "value", length = VALUE_MAX_LENGTH, nullable = false)
    private String value;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (value != null) {
            value = value.trim().toUpperCase();
        }
    }
}
