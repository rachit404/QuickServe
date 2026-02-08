package com.quickserve.repository;

import com.quickserve.model.Booking;
import com.quickserve.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BOOKING REPOSITORY
 * 
 * Data access for Booking entities.
 * Contains queries for both customer and provider perspectives.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Get all bookings for a customer (paginated).
     */
    Page<Booking> findByUserIdOrderByScheduledDateDesc(Long userId, Pageable pageable);
    
    /**
     * Get bookings for a customer with specific status.
     */
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    
    /**
     * Get all bookings for a service provider (paginated).
     */
    Page<Booking> findByProviderIdOrderByScheduledDateDesc(Long providerId, Pageable pageable);
    
    /**
     * Get pending bookings for a provider (needs response).
     */
    List<Booking> findByProviderIdAndStatusOrderByScheduledDateAsc(
        Long providerId, 
        BookingStatus status
    );
    
    /**
     * Check if provider has conflicting booking at a time.
     * Used to prevent double-booking.
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE " +
           "b.provider.id = :providerId AND " +
           "b.status IN ('CONFIRMED', 'IN_PROGRESS') AND " +
           "b.scheduledDate BETWEEN :start AND :end")
    boolean hasConflictingBooking(
        @Param("providerId") Long providerId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    /**
     * Get upcoming bookings for a provider.
     */
    @Query("SELECT b FROM Booking b WHERE " +
           "b.provider.id = :providerId AND " +
           "b.status = 'CONFIRMED' AND " +
           "b.scheduledDate > :now " +
           "ORDER BY b.scheduledDate ASC")
    List<Booking> findUpcomingForProvider(
        @Param("providerId") Long providerId,
        @Param("now") LocalDateTime now
    );
}
