/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                            SERVICE LAYER                                       ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  The Service layer is the HEART of your application. This is where all        ║
 * ║  business logic lives. It orchestrates operations, applies business rules,     ║
 * ║  and coordinates between controllers and repositories.                         ║
 * ║                                                                                ║
 * ║  Think of it as the "brain" that knows the rules of your business:            ║
 * ║  - "A booking can only be cancelled 24 hours before the service"              ║
 * ║  - "Service providers must be verified before they can accept jobs"            ║
 * ║  - "Maximum 3 active bookings per user"                                        ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHY BUSINESS LOGIC BELONGS HERE (not in controllers):                        ║
 * ║                                                                                ║
 * ║  1. REUSABILITY:                                                              ║
 * ║     The same booking logic might be called from:                              ║
 * ║     - Web API (REST controller)                                               ║
 * ║     - Mobile API                                                              ║
 * ║     - Scheduled jobs                                                          ║
 * ║     - Admin operations                                                        ║
 * ║     If logic is in service, it's reusable everywhere.                         ║
 * ║                                                                                ║
 * ║  2. TESTABILITY:                                                              ║
 * ║     Services can be unit tested without mocking HTTP requests.                ║
 * ║     Just mock the repositories and test pure business logic.                  ║
 * ║                                                                                ║
 * ║  3. SINGLE RESPONSIBILITY:                                                    ║
 * ║     Controllers handle HTTP concerns only.                                    ║
 * ║     Services handle business concerns only.                                   ║
 * ║     Each layer has one job.                                                   ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ Business logic and rules                                                   ║
 * ║  ✓ Validation beyond simple field validation                                  ║
 * ║  ✓ Transaction management (@Transactional)                                    ║
 * ║  ✓ Coordination between multiple repositories                                 ║
 * ║  ✓ DTO to Entity transformation (and vice versa)                              ║
 * ║  ✓ Calling external services (payment gateways, email, etc.)                  ║
 * ║  ✓ Caching logic (@Cacheable, @CacheEvict)                                    ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ HTTP-specific code (HttpServletRequest, ResponseEntity)                    ║
 * ║  ✗ Direct SQL queries (use Repository methods)                                ║
 * ║  ✗ Presentation logic (formatting for display)                                ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  TRANSACTION MANAGEMENT:                                                      ║
 * ║                                                                                ║
 * ║  @Transactional annotation ensures:                                           ║
 * ║  - All database operations in a method succeed together, or fail together     ║
 * ║  - If an exception occurs, all changes are rolled back                        ║
 * ║  - No partial updates that could corrupt data                                 ║
 * ║                                                                                ║
 * ║  Example:                                                                     ║
 * ║  @Transactional                                                               ║
 * ║  public void processBooking(BookingRequest request) {                         ║
 * ║      // If ANY of these fail, ALL changes are rolled back                     ║
 * ║      Booking booking = bookingRepository.save(newBooking);                    ║
 * ║      paymentService.processPayment(booking);                                  ║
 * ║      notificationService.sendConfirmation(booking);                           ║
 * ║  }                                                                            ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  NAMING CONVENTIONS:                                                          ║
 * ║  - Class: [Domain]Service (e.g., UserService, BookingService)                 ║
 * ║  - Interface + Implementation pattern:                                        ║
 * ║    → UserService (interface)                                                  ║
 * ║    → UserServiceImpl (implementation)                                         ║
 * ║  - Methods: business-oriented verbs (createBooking, cancelOrder, verifyUser)  ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  DEPENDENCY INJECTION:                                                        ║
 * ║                                                                                ║
 * ║  Services receive their dependencies through constructor injection:           ║
 * ║                                                                                ║
 * ║  @Service  // Marks this as a Spring-managed service bean                     ║
 * ║  @RequiredArgsConstructor  // Lombok generates constructor                    ║
 * ║  public class BookingService {                                                ║
 * ║      private final BookingRepository bookingRepository;  // Injected          ║
 * ║      private final UserService userService;              // Injected          ║
 * ║      private final PaymentService paymentService;        // Injected          ║
 * ║  }                                                                            ║
 * ║                                                                                ║
 * ║  WHY CONSTRUCTOR INJECTION (not @Autowired on fields):                        ║
 * ║  - Dependencies are explicit and required                                     ║
 * ║  - Easier to test (just pass mocks in constructor)                            ║
 * ║  - Immutable (final fields can't be changed)                                  ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE SERVICE STRUCTURE:
 * 
 * @Service
 * @RequiredArgsConstructor
 * @Slf4j  // Lombok: creates a logger
 * public class BookingService {
 *     
 *     private final BookingRepository bookingRepository;
 *     private final UserRepository userRepository;
 *     private final CacheService cacheService;
 *     
 *     @Transactional
 *     public BookingResponse createBooking(Long userId, BookingRequest request) {
 *         // BUSINESS LOGIC: Validate user has no conflicting bookings
 *         User user = userRepository.findById(userId)
 *             .orElseThrow(() -> new ResourceNotFoundException("User not found"));
 *         
 *         // BUSINESS RULE: Check for booking conflicts
 *         if (hasConflictingBooking(user, request.getScheduledDate())) {
 *             throw new BadRequestException("You already have a booking at this time");
 *         }
 *         
 *         // Create and save booking
 *         Booking booking = new Booking();
 *         booking.setUser(user);
 *         booking.setScheduledDate(request.getScheduledDate());
 *         booking.setStatus(BookingStatus.PENDING);
 *         
 *         Booking saved = bookingRepository.save(booking);
 *         
 *         // SIDE EFFECT: Invalidate cache
 *         cacheService.evictUserBookingsCache(userId);
 *         
 *         // Transform entity to DTO before returning
 *         return mapToResponse(saved);
 *     }
 * }
 */
package com.quickserve.service;
