package com.cloudcontrol.inventory.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "product_vendors",
        indexes = {
                @Index(name = "inx_vendor_name", columnList = "name"),
                @Index(name = "inx_vendors_display_order", columnList = "display_order")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_vendor_name", columnNames = "name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"createdAt",})
public class ProductVendor {

    public static final int NAME_MIN_LENGTH = 1;
    public static final int NAME_MAX_LENGTH = 100;
    public static final int PICTURE_URL_MAX_LENGTH = 2048;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vendor name cannot be blank")
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = "Vendor name must be between {min} and {max} characters")
    @Column(name = "name", nullable = false, unique = true, length = NAME_MAX_LENGTH)
    private String name;

    @URL(message = "Please provide a valid URL")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    @Size(max = PICTURE_URL_MAX_LENGTH, message = "Vendor picture URL cannot exceed {max} characters")
    @Column(name = "picture_url", length = PICTURE_URL_MAX_LENGTH)
    private String pictureUrl;

    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order must be non-negative")
    @Builder.Default
    @Column(name = "display_order", nullable = false)
    @JdbcTypeCode(value = SqlTypes.INTEGER)
    private Integer displayOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void cleanupData() {
        if (displayOrder == null) {
            displayOrder = 0;
        }

        if (name != null) {
            name = name.trim();
        }

        if (pictureUrl != null) {
            pictureUrl = pictureUrl.trim();
            if (pictureUrl.isEmpty()) pictureUrl = null;
        }
    }
}
