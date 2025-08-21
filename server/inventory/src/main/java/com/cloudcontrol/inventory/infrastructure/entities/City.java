package com.cloudcontrol.inventory.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "cities",
        indexes = {
                @Index(name = "inx_cities_postal_code", columnList = "postal_code"),
                @Index(name = "inx_cities_name", columnList = "name"),
                @Index(name = "inx_cities_postal_code_name", columnList = "postal_code, name"),
                @Index(name = "inx_cities_city_region_id", columnList = "city_region_id"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"cityRegion"})
public class City {
    public static final int POSTAL_CODE_MAX_LENGTH = 12;
    public static final int NAME_MAX_LENGTH = 30;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Postal code is required")
    @NotBlank(message = "Postal code is required")
    @Size(max = POSTAL_CODE_MAX_LENGTH, message = "Postal code cannot exceed {max} characters")
    @Column(name = "postal_code", length = POSTAL_CODE_MAX_LENGTH, nullable = false)
    private String postalCode;

    @NotNull(message = "City name is required")
    @NotBlank(message = "City name is required")
    @Size(max = NAME_MAX_LENGTH, message = "City name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String name;

    @NotNull(message = "City region is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_region_id", nullable = false)
    @JsonIgnore
    private CityRegion cityRegion;
}
