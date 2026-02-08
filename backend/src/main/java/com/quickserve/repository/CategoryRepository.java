package com.quickserve.repository;

import com.quickserve.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CATEGORY REPOSITORY
 * 
 * Data access for Category entities.
 * Categories are relatively static, perfect for caching!
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find category by its URL slug.
     * Used for SEO-friendly URLs like /services/plumber
     */
    Optional<Category> findBySlug(String slug);
    
    /**
     * Get all active categories ordered by display order.
     * For showing in navigation/dropdown menus.
     */
    List<Category> findByActiveTrueOrderByDisplayOrderAsc();
    
    /**
     * Check if category name already exists.
     * Used during category creation to prevent duplicates.
     */
    boolean existsByName(String name);
    
    /**
     * Check if slug already exists.
     */
    boolean existsBySlug(String slug);
}
