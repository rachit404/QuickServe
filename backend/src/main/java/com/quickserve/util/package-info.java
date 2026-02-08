/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                           UTILITIES LAYER                                      ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  Utility classes contain reusable helper methods that don't fit into any      ║
 * ║  specific domain. Think of them as your toolbox of handy functions.           ║
 * ║                                                                                ║
 * ║  Examples:                                                                    ║
 * ║  - Date formatting utilities                                                  ║
 * ║  - String manipulation helpers                                                ║
 * ║  - Random code generators                                                     ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  BEST PRACTICES FOR UTILITY CLASSES:                                          ║
 * ║                                                                                ║
 * ║  1. STATELESS:                                                                ║
 * ║     Utility methods should not store state. They take inputs, produce         ║
 * ║     outputs, and don't remember anything between calls.                       ║
 * ║                                                                                ║
 * ║  2. PURE FUNCTIONS:                                                           ║
 * ║     Same input should always produce same output. No side effects.            ║
 * ║     formatDate("2024-01-15") always returns "Jan 15, 2024"                    ║
 * ║                                                                                ║
 * ║  3. STATIC OR INJECTABLE:                                                     ║
 * ║     - Static methods: For simple utilities (DateUtils.format(date))           ║
 * ║     - Injectable beans: When utilities need configuration                     ║
 * ║                                                                                ║
 * ║  4. WELL-TESTED:                                                              ║
 * ║     Utility methods are easy to unit test. Test edge cases thoroughly!        ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ Date/time formatting utilities                                             ║
 * ║  ✓ String manipulation helpers                                                ║
 * ║  ✓ Random generators (OTP, reference numbers)                                 ║
 * ║  ✓ Validation utilities                                                       ║
 * ║  ✓ Conversion utilities                                                       ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ Business logic (belongs in Service)                                        ║
 * ║  ✗ Data access (belongs in Repository)                                        ║
 * ║  ✗ Configuration (belongs in Config)                                          ║
 * ║  ✗ Security logic (belongs in Security)                                       ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE UTILITY CLASS:
 * 
 * public final class DateUtils {
 *     
 *     // Private constructor prevents instantiation
 *     private DateUtils() {
 *         throw new UnsupportedOperationException("Utility class");
 *     }
 *     
 *     private static final DateTimeFormatter DISPLAY_FORMAT = 
 *         DateTimeFormatter.ofPattern("MMM dd, yyyy");
 *     
 *     public static String formatForDisplay(LocalDateTime dateTime) {
 *         return dateTime.format(DISPLAY_FORMAT);
 *     }
 *     
 *     public static boolean isToday(LocalDateTime dateTime) {
 *         return dateTime.toLocalDate().equals(LocalDate.now());
 *     }
 *     
 *     public static boolean isWithinNextHours(LocalDateTime dateTime, int hours) {
 *         LocalDateTime now = LocalDateTime.now();
 *         return dateTime.isAfter(now) && 
 *                dateTime.isBefore(now.plusHours(hours));
 *     }
 * }
 */
package com.quickserve.util;
