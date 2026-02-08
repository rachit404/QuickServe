package com.quickserve.model.enums;

/**
 * USER ROLES
 * 
 * Defines what a user can do in the system:
 * 
 * CUSTOMER:
 *   - Browse services
 *   - Book services
 *   - Make payments
 *   - Leave reviews
 * 
 * SERVICE_PROVIDER:
 *   - All customer permissions +
 *   - Accept/reject bookings
 *   - View their schedule
 *   - Update their profile/rates
 * 
 * ADMIN:
 *   - All permissions +
 *   - Manage categories
 *   - Verify service providers
 *   - View platform analytics
 *   - Manage users
 * 
 * AUTHORIZATION CHECK:
 * In controllers, we use @PreAuthorize to check roles:
 * 
 * @PreAuthorize("hasRole('ADMIN')")
 * public void createCategory() { ... }
 */
public enum UserRole {
    CUSTOMER,
    SERVICE_PROVIDER,
    ADMIN
}
