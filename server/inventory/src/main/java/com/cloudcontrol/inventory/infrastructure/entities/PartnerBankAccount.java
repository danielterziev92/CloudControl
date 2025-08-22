package com.cloudcontrol.inventory.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "partner_bank_accounts",
        indexes = {
                @Index(name = "inx_partner_bank_accounts_partner_id", columnList = "partner_id"),
                @Index(name = "inx_partner_bank_accounts_partner_id_is_default", columnList = "partner_id, is_default"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "partner")
public class PartnerBankAccount {

    public static final int NAME_MAX_LENGTH = 150;
    public static final int IBAN_MAX_LENGTH = 34;
    public static final int SWIFT_MAX_LENGTH = 11;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bank account name is required")
    @Size(max = NAME_MAX_LENGTH, message = "Bank account name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String name;

    @NotBlank(message = "IBAN is required")
    @Size(max = IBAN_MAX_LENGTH, message = "IBAN cannot exceed {max} characters")
    @Column(name = "iban", length = IBAN_MAX_LENGTH, nullable = false)
    private String iban;

    @NotBlank(message = "SWIFT is required")
    @Size(max = SWIFT_MAX_LENGTH, message = "Swift cannot exceed {max} characters")
    @Column(name = "swift", length = SWIFT_MAX_LENGTH, nullable = false)
    private String swift;

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @NotNull(message = "Partner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}