package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "partner_addresses",
        indexes = {
                @Index(name = "inx_partner_addresses_partner_id", columnList = "partner_id"),
                @Index(name = "inx_partner_addresses_partner_id_is_default", columnList = "partner_id, is_default"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"city", "partner"})
public class PartnerAddress {

    public static final int ADDRESS_MAX_LENGTH = 255;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Address is required")
    @NotBlank(message = "Address is required")
    @NotEmpty(message = "Address cannot be empty")
    @Column(name = "address", nullable = false)
    private String address;

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @NotNull(message = "City is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @NotNull(message = "Partner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}
