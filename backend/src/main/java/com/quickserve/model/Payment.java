package com.quickserve.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.quickserve.model.enums.PaymentStatus;

/**
 * PAYMENT ENTITY
 * 
 * Tracks payment information for bookings.
 * 
 * PAYMENT FLOW:
 * 
 * 1. Booking is confirmed
 * 2. Customer initiates payment (online or cash)
 * 3. For online payments:
 *    - We create a Payment record with status PENDING
 *    - Redirect to payment gateway (Razorpay, Stripe, etc.)
 *    - Gateway processes payment
 *    - Webhook/callback updates status to COMPLETED or FAILED
 * 4. For cash payments:
 *    - Payment record created when service is marked complete
 *    - Provider confirms cash collection
 * 
 * SECURITY NOTE:
 * We NEVER store full credit card numbers!
 * Only store gateway-provided references for tracking.
 */
@Entity
@Table(name = "payments")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * The booking this payment is for.
     */
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    /**
     * Payment amount.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    /**
     * Payment status.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    /**
     * Payment method used.
     * Examples: "card", "upi", "netbanking", "cash"
     */
    @Column(name = "payment_method")
    private String paymentMethod;
    
    /**
     * Transaction ID from payment gateway.
     * Used for tracking and refunds.
     */
    @Column(name = "transaction_id")
    private String transactionId;
    
    /**
     * Reference ID from our system (sent to gateway).
     * Usually a combination of booking ID and timestamp.
     */
    @Column(name = "reference_id")
    private String referenceId;
    
    /**
     * Name of the payment gateway used.
     * "razorpay", "stripe", "cash"
     */
    private String gateway;
    
    /**
     * Any failure reason if payment failed.
     */
    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;
    
    /**
     * When the payment was completed successfully.
     */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    /**
     * If refunded, when it was refunded.
     */
    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;
    
    /**
     * Refund amount (may be partial).
     */
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
