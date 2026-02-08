package com.quickserve.model.enums;

/**
 * PAYMENT STATUS
 * 
 * Tracks the state of a payment transaction.
 * 
 * STATES:
 * 
 * PENDING
 *   → Payment initiated but not yet completed
 *   → Waiting for payment gateway response
 * 
 * COMPLETED
 *   → Payment successful
 *   → Money received
 * 
 * FAILED
 *   → Payment attempt failed
 *   → User can retry
 * 
 * REFUNDED
 *   → Money returned to customer
 *   → Can be full or partial refund
 */
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED
}
