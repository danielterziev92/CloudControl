package com.cloudcontrol.inventory.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "vendors",
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
public class Vendor {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int PICTURE_URL_MAX_LENGTH = 2048;

    protected static final Logger logger = LoggerFactory.getLogger(Vendor.class);

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vendor name is required")
    @NotBlank(message = "Vendor name cannot be blank")
    @Size(max = NAME_MAX_LENGTH, message = "Vendor name cannot exceed {max} characters")
    @Column(name = "name", nullable = false, unique = true, length = NAME_MAX_LENGTH)
    private String name;

    @URL(message = "Please provide a valid URL")
    @Size(max = PICTURE_URL_MAX_LENGTH, message = "Vendor picture URL cannot exceed {max} characters")
    @Column(name = "picture_url", length = PICTURE_URL_MAX_LENGTH)
    private String pictureUrl;

    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order must be non-negative")
    @Column(name = "display_order", nullable = false)
    @JdbcTypeCode(value = SqlTypes.BIGINT)
    private Long displayOrder;

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
            displayOrder = 0L;
        }

        if (name != null) {
            String trimmedName = name.trim();
            if (trimmedName.isEmpty()) {
                logger.error("Vendor name cannot be empty after trimming. Original value: '{}'", name);
            } else {
                name = trimmedName;
            }
        }

        if (pictureUrl != null) {
            String trimmedUrl = pictureUrl.trim();
            if (trimmedUrl.isEmpty()) {
                logger.warn("Vendor picture URL cannot be empty after trimming. Original value: '{}'", pictureUrl);
                pictureUrl = null;
            } else {
                pictureUrl = trimmedUrl;
            }
        }
    }
}
