/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                        DTO (DATA TRANSFER OBJECTS) LAYER                       ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  DTOs are simple objects used to transfer data between layers of the          ║
 * ║  application. They separate your API contracts from your database structure.   ║
 * ║                                                                                ║
 * ║  Think of DTOs as "shipping containers":                                      ║
 * ║  - They carry data from one place to another                                  ║
 * ║  - They're shaped for the journey, not for storage                            ║
 * ║  - Different containers for different purposes                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHY WE NEED DTOs (not just Entities):                                        ║
 * ║                                                                                ║
 * ║  1. SECURITY:                                                                 ║
 * ║     Entity might have: id, email, password, secretKey                         ║
 * ║     Response DTO has: id, email, name                                         ║
 * ║     → Prevents accidental exposure of sensitive data                          ║
 * ║                                                                                ║
 * ║  2. FLEXIBILITY:                                                              ║
 * ║     - List endpoint: returns UserSummaryDTO (id, name)                        ║
 * ║     - Detail endpoint: returns UserDetailDTO (id, name, email, bookings)      ║
 * ║     - Admin endpoint: returns UserAdminDTO (includes role, created date)      ║
 * ║                                                                                ║
 * ║  3. VERSIONING:                                                               ║
 * ║     - API v1: returns fields A, B, C                                          ║
 * ║     - API v2: returns fields A, B, C, D (new field)                           ║
 * ║     - Database structure unchanged, just different DTOs                       ║
 * ║                                                                                ║
 * ║  4. AVOIDING CIRCULAR REFERENCES:                                             ║
 * ║     User → Bookings → User → Bookings... (infinite loop in JSON!)             ║
 * ║     DTOs break this by only including needed fields                           ║
 * ║                                                                                ║
 * ║  5. VALIDATION:                                                               ║
 * ║     Request DTOs can have different validation than entities                  ║
 * ║     - CreateUserRequest: password required                                    ║
 * ║     - UpdateUserRequest: password optional                                    ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  DTO TYPES IN QUICKSERVE:                                                     ║
 * ║                                                                                ║
 * ║  REQUEST DTOs (dto/request/):                                                 ║
 * ║    → Data coming IN to the API                                                ║
 * ║    → Have validation annotations                                              ║
 * ║    → Example: UserRegistrationRequest, BookingRequest                         ║
 * ║                                                                                ║
 * ║  RESPONSE DTOs (dto/response/):                                               ║
 * ║    → Data going OUT from the API                                              ║
 * ║    → Shaped for frontend needs                                                ║
 * ║    → Example: UserResponse, BookingResponse                                   ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  VALIDATION ANNOTATIONS (for Request DTOs):                                   ║
 * ║                                                                                ║
 * ║  @NotNull            → Field must not be null                                 ║
 * ║  @NotBlank           → String must not be null or empty                       ║
 * ║  @NotEmpty           → Collection must not be empty                           ║
 * ║  @Email              → Must be valid email format                             ║
 * ║  @Size(min, max)     → String/collection size constraints                     ║
 * ║  @Min(value)         → Minimum numeric value                                  ║
 * ║  @Max(value)         → Maximum numeric value                                  ║
 * ║  @Pattern(regexp)    → Must match regex pattern                               ║
 * ║  @Past               → Date must be in the past                               ║
 * ║  @Future             → Date must be in the future                             ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  ENTITY → DTO MAPPING:                                                        ║
 * ║                                                                                ║
 * ║  Option 1: Manual mapping in Service layer (simple, clear)                    ║
 * ║  Option 2: Use ModelMapper library (less code, more magic)                    ║
 * ║  Option 3: Use MapStruct (compile-time, best performance)                     ║
 * ║                                                                                ║
 * ║  We recommend starting with manual mapping for clarity.                       ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  NAMING CONVENTIONS:                                                          ║
 * ║  - Request: [Action][Entity]Request (CreateUserRequest, UpdateBookingRequest) ║
 * ║  - Response: [Entity]Response or [Entity][Detail]Response                     ║
 * ║  - Keep names descriptive and action-oriented                                 ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE DTOs:
 * 
 * // REQUEST DTO - for creating a new booking
 * @Data
 * public class BookingRequest {
 *     
 *     @NotNull(message = "Service provider ID is required")
 *     private Long providerId;
 *     
 *     @NotNull(message = "Service date is required")
 *     @Future(message = "Booking date must be in the future")
 *     private LocalDateTime scheduledDate;
 *     
 *     @Size(max = 500, message = "Notes cannot exceed 500 characters")
 *     private String notes;
 * }
 * 
 * // RESPONSE DTO - for returning booking details
 * @Data
 * @Builder
 * public class BookingResponse {
 *     private Long id;
 *     private String providerName;          // Flattened from Provider entity
 *     private String serviceName;           // Flattened from Category entity
 *     private LocalDateTime scheduledDate;
 *     private BookingStatus status;
 *     private BigDecimal amount;
 *     
 *     // No password, internal IDs, or sensitive data!
 * }
 * 
 * // API RESPONSE WRAPPER - standardized response format
 * @Data
 * @Builder
 * public class ApiResponse<T> {
 *     private boolean success;
 *     private String message;
 *     private T data;
 *     private LocalDateTime timestamp;
 *     
 *     public static <T> ApiResponse<T> success(T data) {
 *         return ApiResponse.<T>builder()
 *             .success(true)
 *             .data(data)
 *             .timestamp(LocalDateTime.now())
 *             .build();
 *     }
 * }
 */
package com.quickserve.dto;
