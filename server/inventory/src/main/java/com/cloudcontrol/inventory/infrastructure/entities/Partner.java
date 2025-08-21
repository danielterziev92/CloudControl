package com.cloudcontrol.inventory.infrastructure.entities;

import com.cloudcontrol.inventory.domain.enums.PartnerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(
        name = "partners",
        indexes = {
                @Index(name = "idx_partners_sku_active", columnList = "sku, is_active"),
                @Index(name = "idx_partners_company_name_active", columnList = "company_name, is_active"),
                @Index(name = "idx_partners_tax_number_active", columnList = "tax_number, is_active"),
                @Index(name = "idx_partners_vat_number_active", columnList = "vat_number, is_active"),
                @Index(name = "idx_partners_is_active", columnList = "is_active"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_partner_company_name", columnNames = "company_name"),
                @UniqueConstraint(name = "uc_partner_tax_number", columnNames = "tax_number"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"createdAt", "updatedAt", "deletedAt", "bankAccounts"})
public class Partner {

    public static final int SKU_MAX_LENGTH = 50;
    public static final int COMPANY_NAME_MAX_LENGTH = 200;
    public static final int CONTACT_PERSON_NAME_MAX_LENGTH = 100;
    public static final int TAX_NUMBER_MAX_LENGTH = 18;
    public static final int VAT_NUMBER_MAX_LENGTH = 20;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = SKU_MAX_LENGTH, message = "Partner code cannot exceed {max} characters")
    @Column(name = "sku", length = SKU_MAX_LENGTH)
    private String sku;

    @NotBlank(message = "Company name is required")
    @Size(max = COMPANY_NAME_MAX_LENGTH, message = "Company name cannot exceed {max} characters")
    @Column(name = "company_name", length = COMPANY_NAME_MAX_LENGTH, unique = true, nullable = false)
    private String companyName;

    @Size(max = CONTACT_PERSON_NAME_MAX_LENGTH, message = "Contact person name cannot exceed {max} characters")
    @Column(name = "contact_person_name", length = CONTACT_PERSON_NAME_MAX_LENGTH)
    private String contactPersonName;

    @NotBlank(message = "Tax number is required")
    @Size(max = TAX_NUMBER_MAX_LENGTH, message = "Tax number cannot exceed {max} characters")
    @Column(name = "tax_number", length = TAX_NUMBER_MAX_LENGTH, unique = true, nullable = false)
    private String taxNumber;

    @Size(max = VAT_NUMBER_MAX_LENGTH, message = "VAT number cannot exceed {max} characters")
    @Column(name = "vat_number", length = VAT_NUMBER_MAX_LENGTH)
    private String vatNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "partner_type", length = PartnerType.MAX_LENGTH, nullable = false)
    private PartnerType partnerType = PartnerType.getDefault();

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PartnerBankAccount> bankAccounts;

    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PartnerAddress> addresses;

    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PartnerAdditionalInfo> additionalInfos;
}
