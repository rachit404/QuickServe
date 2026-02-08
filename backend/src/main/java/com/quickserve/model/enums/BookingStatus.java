package com.quickserve.model.enums;

/**
 * BOOKING STATUS
 * 
 * Represents the lifecycle of a booking.
 * 
 * STATE MACHINE:
 * 
 *                    ┌──────────────┐
 *                    │   PENDING    │ ← Initial state when customer books
 *                    └──────────────┘
 *                           │
 *            ┌──────────────┼──────────────┐
 *            ▼              ▼              ▼
 *     ┌───────────┐  ┌───────────┐  ┌───────────┐
 *     │ CONFIRMED │  │ REJECTED  │  │ CANCELLED │
 *     └───────────┘  └───────────┘  └───────────┘
 *            │            (end)          (end)
 *            ▼
 *     ┌─────────────┐
 *     │ IN_PROGRESS │ ← Provider started the job
 *     └─────────────┘
 *            │
 *     ┌──────┴──────┐
 *     ▼             ▼
 * ┌───────────┐ ┌───────────┐
 * │ COMPLETED │ │ CANCELLED │
 * └───────────┘ └───────────┘
 *     (end)         (end)
 * 
 * VALID TRANSITIONS:
 * - PENDING → CONFIRMED (provider accepts)
 * - PENDING → REJECTED (provider declines)
 * - PENDING → CANCELLED (customer cancels)
 * - CONFIRMED → IN_PROGRESS (service starts)
 * - CONFIRMED → CANCELLED (either party cancels)
 * - IN_PROGRESS → COMPLETED (service finished)
 * - IN_PROGRESS → CANCELLED (with special conditions)
 */
public enum BookingStatus {
    
    /**
     * Booking created, waiting for provider response.
     */
    PENDING,
    
    /**
     * Provider accepted the booking.
     */
    CONFIRMED,
    
    /**
     * Provider declined the booking.
     */
    REJECTED,
    
    /**
     * Service is currently being performed.
     */
    IN_PROGRESS,
    
    /**
     * Service completed successfully.
     */
    COMPLETED,
    
    /**
     * Booking was cancelled (by customer or provider).
     */
    CANCELLED
}
