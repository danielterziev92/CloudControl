package com.cloudcontrol.inventory.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "categories",
        indexes = {
                @Index(name = "idx_categories_name", columnList = "name"),
                @Index(name = "idx_categories_display_order", columnList = "display_order"),
                @Index(name = "idx_categories_parent_id", columnList = "parent_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_category_name_parent", columnNames = {"name", "parent_id"})
        }
)
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"parent", "children"})
public class Category {

    public static final int NAME_MAX_LENGTH = 100;

    private static final Logger logger = LoggerFactory.getLogger(Category.class);

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Category name is required")
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = NAME_MAX_LENGTH, message = "Category name cannot exceed {max} characters")
    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String name;

    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order must be non-negative")
    @Column(name = "display_order", nullable = false)
    private Long displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    public Category(String name, Long displayOrder) {
        this.name = name;
        this.displayOrder = displayOrder;
    }

    public Category(String name, Long displayOrder, Category parent) {
        this.name = name;
        this.displayOrder = displayOrder;
        this.parent = parent;
    }

    @PrePersist
    @PreUpdate
    protected void cleanupData() {
        if (displayOrder == null) {
            displayOrder = 0L;
        }

        if (name != null) {
            String trimmedName = name.trim();
            if (trimmedName.isEmpty()) {
                logger.atError()
                        .addKeyValue("class", Category.class.getName())
                        .addKeyValue("method", "cleanupData")
                        .addKeyValue("originalValue", name)
                        .log("Category name cannot be empty after trimming.");
                throw new IllegalArgumentException("Category name cannot be empty after trimming.");
            }
            name = trimmedName;
        }
    }
}
