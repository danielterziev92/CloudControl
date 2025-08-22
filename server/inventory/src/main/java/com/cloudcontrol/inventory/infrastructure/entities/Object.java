package com.cloudcontrol.inventory.infrastructure.entities;

import com.cloudcontrol.inventory.domain.enums.ProductPriceType;
import com.cloudcontrol.inventory.domain.validation.AllowedObjectPriceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "objects",
        indexes = {
                @Index(name = "idx_objects_code_active", columnList = "code, is_active"),
                @Index(name = "idx_objects_name_active", columnList = "name, is_active"),
                @Index(name = "idx_objects_city_id", columnList = "city_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_object_name", columnNames = "name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"city"})
public class Object {

    public static final int CODE_MAX_LENGTH = 10;
    public static final int NAME_MAX_LENGTH = 100;
    public static final int ADDRESS_MAX_LENGTH = 255;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = CODE_MAX_LENGTH, message = "Object code cannot exceed {max} characters")
    @Column(name = "code", length = CODE_MAX_LENGTH)
    private String code;

    @NotBlank(message = "Object name is required")
    @Size(max = NAME_MAX_LENGTH, message = "Object name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false, unique = true)
    private String name;

    @NotNull(message = "Object price type is required")
    @AllowedObjectPriceType
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "price_type", nullable = false)
    private ProductPriceType priceType;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Size(max = ADDRESS_MAX_LENGTH, message = "Address cannot exceed {max} characters")
    @Column(name = "address", length = ADDRESS_MAX_LENGTH)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (code != null) {
            code = code.trim();
        }

        if (name != null) {
            name = name.trim();
        }

        if (address != null) {
            address = address.trim();
        }
    }
}
