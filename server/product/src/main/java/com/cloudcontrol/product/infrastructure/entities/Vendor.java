package com.cloudcontrol.product.infrastructure.entities;

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
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"createdAt",})
public class Vendor {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int URL_MAX_LENGTH = 2048;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vendor name is required")
    @NotBlank(message = "Vendor name cannot be blank")
    @Size(max = NAME_MAX_LENGTH, message = "Vendor name cannot exceed {max} characters")
    @Column(name = "name", nullable = false, unique = true, length = NAME_MAX_LENGTH)
    private String name;

    @URL(message = "Please provide a valid URL")
    @Size(max = URL_MAX_LENGTH, message = "Vendor URL cannot exceed {max} characters")
    @Column(name = "url", length = URL_MAX_LENGTH)
    private String url;

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
            name = name.trim();
        }

        if (url != null && !url.trim().isEmpty()) {
            url = url.trim();
        } else {
            url = null;
        }
    }
}
