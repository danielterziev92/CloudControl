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

    private static final int NAME_LENGTH = 3;
    private static final int UNIT_MIN_VALUE = 1;
    private static final int UNIT_MAX_VALUE = 10000;
    private static final BigDecimal RATE_MIN_VALUE = BigDecimal.ZERO;
    private static final BigDecimal RATE_MAX_VALUE = BigDecimal.valueOf(999999.999999);

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Currency name is required")
    @NotBlank(message = "Currency name is required")
    @Length(min = NAME_LENGTH, max = NAME_LENGTH, message = "Currency name must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency name must be exactly 3 uppercase letters (ISO 4217 standard)")
    @Column(name = "name", length = NAME_LENGTH, nullable = false, unique = true)
    private String name;

    @NotNull(message = "Currency unit is required")
    @Min(value = UNIT_MIN_VALUE, message = "Currency unit must be at least {min}")
    @Max(value = UNIT_MAX_VALUE, message = "Currency unit cannot exceed {max}")
    @Column(name = "unit", nullable = false)
    private Integer unit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate must be positive")
    @DecimalMax(value = "999999.999999", message = "Exchange rate is too large")
    @Digits(integer = 6, fraction = 6, message = "Exchange rate format is invalid")
    @Column(name = "rate", precision = 12, scale = 6)
    private BigDecimal rate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
