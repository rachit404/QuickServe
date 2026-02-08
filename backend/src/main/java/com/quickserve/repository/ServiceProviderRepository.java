package com.quickserve.repository;

import com.quickserve.model.ServiceProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SERVICE PROVIDER REPOSITORY
 * 
 * Data access for ServiceProvider entities.
 * Includes custom queries for searching and filtering providers.
 */
@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    
    /**
     * Find provider by their linked user account.
     */
    Optional<ServiceProvider> findByUserId(Long userId);
    
    /**
     * Find all providers in a category.
     * Uses pagination to avoid loading thousands of records.
     */
    Page<ServiceProvider> findByCategoryIdAndVerifiedTrueAndAvailableTrue(
        Long categoryId, 
        Pageable pageable
    );
    
    /**
     * Find top-rated providers in a category.
     */
    List<ServiceProvider> findTop10ByCategoryIdAndVerifiedTrueOrderByRatingDesc(Long categoryId);
    
    /**
     * Search providers by service area.
     * 
     * JPQL query example - more control than method names.
     */
    @Query("SELECT sp FROM ServiceProvider sp WHERE " +
           "sp.verified = true AND sp.available = true AND " +
           "LOWER(sp.serviceArea) LIKE LOWER(CONCAT('%', :area, '%'))")
    List<ServiceProvider> findByServiceArea(@Param("area") String area);
    
    /**
     * Count verified providers per category.
     * Useful for showing category stats on homepage.
     */
    @Query("SELECT sp.category.id, COUNT(sp) FROM ServiceProvider sp " +
           "WHERE sp.verified = true GROUP BY sp.category.id")
    List<Object[]> countProvidersByCategory();
}
