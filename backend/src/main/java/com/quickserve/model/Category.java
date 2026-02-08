package com.quickserve.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CATEGORY ENTITY
 * 
 * Service categories like Plumber, Electrician, Tutor, etc.
 * These help users browse and find the right service provider.
 * 
 * EXAMPLE CATEGORIES:
 * - Home Services
 *   - Plumber
 *   - Electrician
 *   - Carpenter
 *   - Painter
 * - Personal Services
 *   - Beauty & Makeup
 *   - Fitness Trainer
 *   - Tutor
 * - Cleaning
 *   - Home Cleaning
 *   - Car Wash
 *   - Laundry
 * 
 * DESIGN DECISION:
 * Categories are managed by admins, not created by users.
 * This ensures consistency and prevents duplicate/spam categories.
 */
@Entity
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Display name of the category.
     * Example: "Plumber", "Electrician", "Math Tutor"
     */
    @Column(nullable = false, unique = true)
    private String name;
    
    /**
     * URL-friendly version of the name.
     * Example: "plumber", "electrician", "math-tutor"
     * Used in URLs: /services/plumber
     */
    @Column(nullable = false, unique = true)
    private String slug;
    
    /**
     * Description of the category.
     * Shown on category pages to help users understand what services are included.
     */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /**
     * Icon name or URL for displaying in the UI.
     * Could be an icon library name (like "plumber-icon") or image URL.
     */
    private String icon;
    
    /**
     * Whether this category is visible to users.
     * Allows admins to prepare categories before making them public.
     */
    @Builder.Default
    private boolean active = true;
    
    /**
     * Display order for sorting categories in the UI.
     */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
    
    /**
     * All service providers in this category.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ServiceProvider> providers = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
