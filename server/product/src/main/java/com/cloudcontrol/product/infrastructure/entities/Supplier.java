package com.cloudcontrol.product.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"createdAt", "updatedAt", "deletedAt"})
public class Supplier {

    public static final int CODE_MAX_LENGTH = 10;
    public static final int COMPANY_NAME_MAX_LENGTH = 200;
    public static final int CONTACT_PERSON_NAME_MAX_LENGTH = 100;
    public static final int EMAIL_MAX_LENGTH = 254;
    public static final int PHONE_NUMBER_MAX_LENGTH = 20;
    public static final int COUNTRY_MAX_LENGTH = 48;
    public static final int CITY_MAX_LENGTH = 168;
    public static final int ADDRESS_MAX_LENGTH = 255;
    public static final int POSTAL_CODE_MAX_LENGTH = 12;
    public static final int TAX_NUMBER_MAX_LENGTH = 20;
    public static final int VAT_NUMBER_MAX_LENGTH = 20;
    public static final int BANK_ACCOUNT_NAME_MAX_LENGTH = 150;
    public static final int BANK_IBAN_MAX_LENGTH = 34;
    public static final int BANK_SWIFT_MAX_LENGTH = 11;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = CODE_MAX_LENGTH, message = "Supplier code cannot exceed {max} characters")
    @Column(name = "code", length = CODE_MAX_LENGTH, unique = true)
    private String code;

    @Size(max = COMPANY_NAME_MAX_LENGTH, message = "Company name cannot exceed {max} characters")
    @Column(name = "company_name", length = COMPANY_NAME_MAX_LENGTH, unique = true, nullable = false)
    private String companyName;

    @Size(max = CONTACT_PERSON_NAME_MAX_LENGTH, message = "Contact person name cannot exceed {max} characters")
    @Column(name = "contact_person_name", length = CONTACT_PERSON_NAME_MAX_LENGTH)
    private String contactPersonName;

    @Email(message = "Please provide a valid email address")
    @Size(max = EMAIL_MAX_LENGTH, message = "Email cannot exceed {max} characters")
    @Column(name = "email", length = EMAIL_MAX_LENGTH)
    private String email;

    @Size(max = PHONE_NUMBER_MAX_LENGTH, message = "Phone number cannot exceed {max} characters")
    @Column(name = "phone_number", length = PHONE_NUMBER_MAX_LENGTH)
    private String phoneNumber;

    @Size(max = COUNTRY_MAX_LENGTH, message = "Country cannot exceed {max} characters")
    @Column(name = "country", length = COUNTRY_MAX_LENGTH)
    private String country;

    @Size(max = CITY_MAX_LENGTH, message = "City cannot exceed {max} characters")
    @Column(name = "city", length = CITY_MAX_LENGTH)
    private String city;

    @Size(max = ADDRESS_MAX_LENGTH, message = "Address cannot exceed {max} characters")
    @Column(name = "address", length = ADDRESS_MAX_LENGTH)
    private String address;

    @Size(max = POSTAL_CODE_MAX_LENGTH, message = "Postal code cannot exceed {max} characters")
    @Column(name = "postal_code", length = POSTAL_CODE_MAX_LENGTH)
    private String postalCode;

    @Size(max = TAX_NUMBER_MAX_LENGTH, message = "Tax number cannot exceed {max} characters")
    @Column(name = "tax_number", length = TAX_NUMBER_MAX_LENGTH)
    private String taxNumber;

    @Size(max = VAT_NUMBER_MAX_LENGTH, message = "VAT number cannot exceed {max} characters")
    @Column(name = "vat_number", length = VAT_NUMBER_MAX_LENGTH)
    private String vatNumber;

    @Size(max = BANK_ACCOUNT_NAME_MAX_LENGTH, message = "Bank account name cannot exceed {max} characters")
    @Column(name = "bank_name", length = BANK_ACCOUNT_NAME_MAX_LENGTH)
    private String bankName;

    @Size(max = BANK_IBAN_MAX_LENGTH, message = "Bank IBAN cannot exceed {max} characters")
    @Column(name = "bank_iban", length = BANK_IBAN_MAX_LENGTH)
    private String bankIbank;

    @Size(max = BANK_SWIFT_MAX_LENGTH, message = "Bank Swift cannot exceed {max} characters")
    @Column(name = "bank_swift", length = BANK_SWIFT_MAX_LENGTH)
    private String bankSwift;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PreRemove
    protected void deleteSupplier() {
        this.deletedAt = LocalDateTime.now();
        this.active = Boolean.FALSE;
    }
}
