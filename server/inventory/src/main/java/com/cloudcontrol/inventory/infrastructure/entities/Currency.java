package com.cloudcontrol.inventory.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "currencies",
        indexes = {
                @Index(name = "idx_currencies_name", columnList = "name"),
                @Index(name = "idx_currencies_unit", columnList = "unit"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_currency_name", columnNames = "name"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"updatedAt"})
public class Currency {

    public static final int NAME_LENGTH = 3;
    public static final int UNIT_MIN_VALUE = 1;
    public static final int UNIT_MAX_VALUE = 10000;
    private static final String RATE_MIN_VALUE_STRING = "0.0";
    private static final String RATE_MAX_VALUE_STRING = "999999.999999";
    private static final BigDecimal RATE_MIN_VALUE = BigDecimal.ZERO;
    private static final BigDecimal RATE_MAX_VALUE = BigDecimal.valueOf(999999.999999);
    public static final int RATE_PRECISION = 12;
    public static final int RATE_SCALE = 6;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Currency name is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency name must be exactly 3 uppercase letters (ISO 4217 standard)")
    @Length(min = NAME_LENGTH, max = NAME_LENGTH, message = "Currency name must be exactly 3 characters")
    @Column(name = "name", length = NAME_LENGTH, nullable = false, unique = true)
    private String name;

    @NotNull(message = "Currency unit is required")
    @Min(value = UNIT_MIN_VALUE, message = "Currency unit must be at least {min}")
    @Max(value = UNIT_MAX_VALUE, message = "Currency unit cannot exceed {max}")
    @Column(name = "unit", nullable = false)
    private Integer unit;

    @NotNull(message = "Exchange rate is required")
    @DecimalMin(value = RATE_MIN_VALUE_STRING, inclusive = false, message = "Exchange rate must be positive")
    @DecimalMax(value = RATE_MAX_VALUE_STRING, message = "Exchange rate is too large")
    @Digits(integer = 6, fraction = 6, message = "Exchange rate format is invalid")
    @Column(name = "rate", precision = RATE_PRECISION, scale = RATE_SCALE)
    private BigDecimal rate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (name != null) {
            name = name.trim().toUpperCase();
        }

        if (rate != null && rate.compareTo(RATE_MIN_VALUE) <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }

        if (rate != null && rate.compareTo(RATE_MAX_VALUE) > 0) {
            throw new IllegalArgumentException("Exchange rate is too large");
        }
    }
}
