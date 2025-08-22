package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
        name = "product_tax_groups",
        indexes = {
                @Index(name = "inx_product_tax_groups_name", columnList = "name"),
                @Index(name = "inx_product_tax_groups_value", columnList = "value"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_product_tax_group_name", columnNames = "name"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductTaxGroup {

    public static final int NAME_MAX_LENGTH = 7;
    public static final int VALUE_MIN = 0;
    public static final int VALUE_MAX = 100;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tax group name is required")
    @Size(max = NAME_MAX_LENGTH, message = "Tax group name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String name;

    @NotNull(message = "Tax group value is required")
    @Min(value = VALUE_MIN, message = "Tax group value must be at least {min}")
    @Max(value = VALUE_MAX, message = "Tax group value cannot exceed {max}")
    @Column(name = "value", nullable = false)
    private Integer value;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (name != null) {
            name = name.trim();
        }
    }
}
