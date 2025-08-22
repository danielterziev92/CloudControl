package com.cloudcontrol.inventory.infrastructure.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "countries",
        indexes = {
                @Index(name = "inx_countries_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_countries_name", columnNames = "name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Country {
    public static final int NAME_MAX_LENGTH = 48;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country name is required")
    @Size(max = NAME_MAX_LENGTH, message = "Country name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false, unique = true)
    private String name;

    @PrePersist
    @PreUpdate
    private void normalizeName() {
        if (name != null) {
            name = name.trim();
        }
    }
}