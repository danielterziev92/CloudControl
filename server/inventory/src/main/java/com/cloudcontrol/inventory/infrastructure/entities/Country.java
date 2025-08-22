package com.cloudcontrol.inventory.infrastructure.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "countries",
        indexes = {
                @Index(name = "inx_countries_name", columnList = "name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    public static final int NAME_MAX_LENGTH = 48;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country name is required")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String name;
}