package com.cloudcontrol.inventory.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "city_regions",
        indexes = {
                @Index(name = "inx_city_regions_name", columnList = "name"),
                @Index(name = "inx_city_regions_country_id", columnList = "country_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "country")
public class CityRegion {

    public static final int NAME_MAX_LENGTH = 30;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City region name is required")
    @Size(max = NAME_MAX_LENGTH, message = "City region name cannot exceed {max} characters")
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @NotBlank(message = "Country name is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnore
    private Country country;
}
