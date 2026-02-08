package com.quickserve.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.quickserve.model.enums.BookingStatus;

/**
 * BOOKING ENTITY
 * 
 * This is the CORE of QuickServe - connecting customers with service providers.
 * Each booking represents a scheduled service appointment.
 * 
 * BOOKING LIFECYCLE:
 * 
 * 1. PENDING
 *    └─ Customer creates booking, waiting for provider response
 * 
 * 2a. CONFIRMED
 *    └─ Provider accepted the booking
 *         │
 *    3a. IN_PROGRESS
 *        └─ Service is being performed
 *              │
 *         4a. COMPLETED
 *             └─ Service finished successfully
 *                  │
 *             5. (Customer leaves review/rating)
 * 
 * 2b. REJECTED
 *    └─ Provider declined the booking
 * 
 * At any point before COMPLETED:
 * - CANCELLED (by customer or provider)
 */
@Entity
@Table(name = "bookings")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * The customer who booked the service.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * The service provider who will perform the service.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;
    
    /**
     * Scheduled date and time for the service.
     */
    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;
    
    /**
     * Estimated duration in minutes.
     */
    @Column(name = "duration_minutes")
    @Builder.Default
    private Integer durationMinutes = 60;
    
    /**
     * Current status of the booking.
     * See BookingStatus enum for all possible states.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;
    
    /**
     * Service location - where the provider should go.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;
    
    /**
     * Additional notes from the customer.
     * "Please bring your own ladder", "Dog in the house", etc.
     */
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Quoted amount for the service.
     * May differ from final amount after service completion.
     */
    @Column(name = "quoted_amount", precision = 10, scale = 2)
    private BigDecimal quotedAmount;
    
    /**
     * Final amount charged after service.
     */
    @Column(name = "final_amount", precision = 10, scale = 2)
    private BigDecimal finalAmount;
    
    /**
     * Link to payment if payment has been made.
     */
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;
    
    /**
     * When the provider confirmed/rejected.
     */
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    
    /**
     * When the service was marked complete.
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    /**
     * Rating given by customer after service (1-5).
     */
    private Integer rating;
    
    /**
     * Review text from customer.
     */
    @Column(columnDefinition = "TEXT")
    private String review;
    
    /**
     * If cancelled, store the reason.
     */
    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
