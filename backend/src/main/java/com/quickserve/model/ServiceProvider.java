package com.quickserve.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE PROVIDER ENTITY
 * 
 * Represents professionals who offer services on the platform:
 * - Plumbers, Electricians, Carpenters
 * - Tutors, Fitness trainers
 * - Beauticians, Makeup artists
 * - And more...
 * 
 * DESIGN DECISION:
 * ServiceProvider is separate from User because:
 * 1. Not all users are service providers
 * 2. Service providers have additional fields (rating, bio, hourly rate)
 * 3. A user can "become" a service provider by creating this linked record
 * 
 * The relationship: User (1) ←→ (0..1) ServiceProvider
 * A user can have zero or one service provider profile.
 */
@Entity
@Table(name = "service_providers")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Link to the user account.
     * OneToOne because each user can only have one provider profile.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * The service category (Plumber, Electrician, etc.)
     * Provider can belong to one primary category.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    /**
     * Professional bio/description shown to customers.
     * Optional but highly recommended for better bookings.
     */
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    /**
     * Hourly rate in the local currency.
     * 
     * We use BigDecimal (not double) for money because:
     * - Double has precision issues (0.1 + 0.2 != 0.3 in floating point)
     * - BigDecimal is exact and designed for financial calculations
     */
    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;
    
    /**
     * Average rating from customers (1-5 stars).
     * Calculated from completed booking reviews.
     */
    @Column(precision = 2, scale = 1)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;
    
    /**
     * Total number of ratings received.
     * Used to calculate weighted average when new ratings come in.
     */
    @Column(name = "total_ratings")
    @Builder.Default
    private Integer totalRatings = 0;
    
    /**
     * Whether this provider is verified by admin.
     * Only verified providers appear in search results.
     */
    @Builder.Default
    private boolean verified = false;
    
    /**
     * Whether the provider is currently accepting new bookings.
     * They might be on vacation or fully booked.
     */
    @Builder.Default
    private boolean available = true;
    
    /**
     * Service area - where the provider operates.
     * Could be enhanced to use geolocation in the future.
     */
    @Column(name = "service_area")
    private String serviceArea;
    
    /**
     * Years of experience in the field.
     */
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    /**
     * All bookings for this provider.
     */
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
