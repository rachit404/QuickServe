/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                          REPOSITORY LAYER                                      ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  Repositories are the DATA ACCESS layer. They abstract database operations    ║
 * ║  so the rest of your application doesn't need to know SQL or database details.║
 * ║                                                                                ║
 * ║  Think of it as a librarian:                                                  ║
 * ║  - You ask for a book by title (findByTitle)                                  ║
 * ║  - The librarian knows where to find it (database query)                      ║
 * ║  - You don't need to know how the library is organized                        ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  SPRING DATA JPA MAGIC:                                                       ║
 * ║                                                                                ║
 * ║  The amazing thing about Spring Data JPA is that you often don't need to      ║
 * ║  write any implementation code! Just define an interface, and Spring          ║
 * ║  generates the implementation at runtime.                                      ║
 * ║                                                                                ║
 * ║  Example:                                                                     ║
 * ║  public interface UserRepository extends JpaRepository<User, Long> {          ║
 * ║      Optional<User> findByEmail(String email);                                ║
 * ║  }                                                                            ║
 * ║                                                                                ║
 * ║  Spring automatically generates:                                              ║
 * ║  SELECT * FROM users WHERE email = ?                                          ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  METHODS YOU GET FOR FREE (from JpaRepository):                               ║
 * ║                                                                                ║
 * ║  save(entity)           → INSERT or UPDATE                                    ║
 * ║  saveAll(entities)      → Batch INSERT/UPDATE                                 ║
 * ║  findById(id)           → SELECT by primary key                               ║
 * ║  findAll()              → SELECT all records                                  ║
 * ║  findAll(Pageable)      → SELECT with pagination                              ║
 * ║  delete(entity)         → DELETE                                              ║
 * ║  deleteById(id)         → DELETE by primary key                               ║
 * ║  count()                → COUNT all records                                   ║
 * ║  existsById(id)         → Check if exists                                     ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  QUERY DERIVATION (Method Name Magic):                                        ║
 * ║                                                                                ║
 * ║  Spring Data can generate queries from method names:                          ║
 * ║                                                                                ║
 * ║  findByEmail(String email)                                                    ║
 * ║  → SELECT * FROM users WHERE email = ?                                        ║
 * ║                                                                                ║
 * ║  findByEmailAndActive(String email, boolean active)                           ║
 * ║  → SELECT * FROM users WHERE email = ? AND active = ?                         ║
 * ║                                                                                ║
 * ║  findByCreatedAtAfter(LocalDateTime date)                                     ║
 * ║  → SELECT * FROM users WHERE created_at > ?                                   ║
 * ║                                                                                ║
 * ║  countByStatus(BookingStatus status)                                          ║
 * ║  → SELECT COUNT(*) FROM bookings WHERE status = ?                             ║
 * ║                                                                                ║
 * ║  findTop5ByOrderByRatingDesc()                                                ║
 * ║  → SELECT * FROM providers ORDER BY rating DESC LIMIT 5                       ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  CUSTOM QUERIES (@Query):                                                     ║
 * ║                                                                                ║
 * ║  For complex queries, use @Query annotation with JPQL or native SQL:          ║
 * ║                                                                                ║
 * ║  @Query("SELECT b FROM Booking b WHERE b.user.id = :userId " +                ║
 * ║         "AND b.status = :status ORDER BY b.scheduledDate DESC")               ║
 * ║  List<Booking> findUserBookingsByStatus(                                      ║
 * ║      @Param("userId") Long userId,                                            ║
 * ║      @Param("status") BookingStatus status                                    ║
 * ║  );                                                                           ║
 * ║                                                                                ║
 * ║  JPQL vs Native SQL:                                                          ║
 * ║  - JPQL: Uses entity/field names (portable across databases)                  ║
 * ║  - Native: Uses actual SQL (database-specific, but more powerful)             ║
 * ║                                                                                ║
 * ║  @Query(value = "SELECT * FROM bookings WHERE ...", nativeQuery = true)       ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ Data access method definitions                                             ║
 * ║  ✓ Custom query methods                                                       ║
 * ║  ✓ Pagination methods                                                         ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ Business logic (belongs in Service)                                        ║
 * ║  ✗ Data transformation (belongs in Service)                                   ║
 * ║  ✗ Validation (belongs in Controller/Service/Entity)                          ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  NAMING CONVENTIONS:                                                          ║
 * ║  - Class: [Entity]Repository (e.g., UserRepository, BookingRepository)        ║
 * ║  - Methods: Standard JPA naming (findBy..., countBy..., deleteBy...)          ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE REPOSITORY:
 * 
 * @Repository  // Marks this as a Spring Data repository (optional, Spring detects it)
 * public interface BookingRepository extends JpaRepository<Booking, Long> {
 *     
 *     // Method name query - Spring generates the SQL
 *     List<Booking> findByUserId(Long userId);
 *     
 *     // More complex method name query
 *     List<Booking> findByUserIdAndStatusOrderByScheduledDateDesc(
 *         Long userId, 
 *         BookingStatus status
 *     );
 *     
 *     // Custom JPQL query
 *     @Query("SELECT b FROM Booking b WHERE b.provider.id = :providerId " +
 *            "AND b.scheduledDate BETWEEN :start AND :end")
 *     List<Booking> findProviderBookingsInRange(
 *         @Param("providerId") Long providerId,
 *         @Param("start") LocalDateTime start,
 *         @Param("end") LocalDateTime end
 *     );
 *     
 *     // Native SQL query (when JPQL isn't enough)
 *     @Query(value = "SELECT * FROM bookings WHERE status = 'PENDING' " +
 *                    "AND EXTRACT(HOUR FROM scheduled_date) = :hour",
 *            nativeQuery = true)
 *     List<Booking> findPendingBookingsAtHour(@Param("hour") int hour);
 * }
 */
package com.quickserve.repository;
