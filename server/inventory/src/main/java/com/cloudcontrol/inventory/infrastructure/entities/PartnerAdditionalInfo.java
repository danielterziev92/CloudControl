package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(
        name = "partner_additional_infos",
        indexes = {
                @Index(name = "inx_partner_additional_infos_partner_id", columnList = "partner_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "partner")
public class PartnerAdditionalInfo {

    public static final int EMAIL_MAX_LENGTH = 254;
    public static final int PHONE_NUMBER_MAX_LENGTH = 20;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = EMAIL_MAX_LENGTH, message = "Email cannot exceed {max} characters")
    @Column(name = "email", length = EMAIL_MAX_LENGTH)
    private String email;

    @Size(max = PHONE_NUMBER_MAX_LENGTH, message = "Phone number cannot exceed {max} characters")
    @Column(name = "phone_number", length = PHONE_NUMBER_MAX_LENGTH)
    private String phoneNumber;

    @NotNull(message = "Partner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}
