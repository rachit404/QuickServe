/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                         MODEL / ENTITY LAYER                                   ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  Entities represent your database tables as Java classes. Each entity class   ║
 * ║  maps to a table, and each field maps to a column.                            ║
 * ║                                                                                ║
 * ║  Think of it as a blueprint:                                                  ║
 * ║  - User class = users table                                                   ║
 * ║  - String email field = email VARCHAR column                                  ║
 * ║  - Long id field = id BIGINT primary key column                               ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  OBJECT-RELATIONAL MAPPING (ORM):                                             ║
 * ║                                                                                ║
 * ║  ORM is the technique of converting between:                                  ║
 * ║  - Java objects (User user = new User())                                      ║
 * ║  - Database rows (INSERT INTO users VALUES ...)                               ║
 * ║                                                                                ║
 * ║  Hibernate (the JPA implementation) handles this automatically.               ║
 * ║  You work with objects; Hibernate writes the SQL.                             ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  COMMON JPA ANNOTATIONS:                                                      ║
 * ║                                                                                ║
 * ║  @Entity                                                                      ║
 * ║    → Marks class as a JPA entity (database table)                             ║
 * ║                                                                                ║
 * ║  @Table(name = "users")                                                       ║
 * ║    → Specifies table name (optional if class name matches)                    ║
 * ║                                                                                ║
 * ║  @Id                                                                          ║
 * ║    → Marks field as the primary key                                           ║
 * ║                                                                                ║
 * ║  @GeneratedValue(strategy = GenerationType.IDENTITY)                          ║
 * ║    → Auto-generate ID (PostgreSQL uses SERIAL/BIGSERIAL)                      ║
 * ║                                                                                ║
 * ║  @Column(name = "email", nullable = false, unique = true)                     ║
 * ║    → Column configuration (constraints, name, size, etc.)                     ║
 * ║                                                                                ║
 * ║  @Enumerated(EnumType.STRING)                                                 ║
 * ║    → Store enum as string (not integer) for readability                       ║
 * ║                                                                                ║
 * ║  @CreatedDate, @LastModifiedDate                                              ║
 * ║    → Auto-set timestamps for auditing                                         ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  RELATIONSHIP ANNOTATIONS:                                                    ║
 * ║                                                                                ║
 * ║  @OneToMany                                                                   ║
 * ║    → One user has many bookings                                               ║
 * ║    → Example: User { @OneToMany List<Booking> bookings }                      ║
 * ║                                                                                ║
 * ║  @ManyToOne                                                                   ║
 * ║    → Many bookings belong to one user                                         ║
 * ║    → Example: Booking { @ManyToOne User user }                                ║
 * ║                                                                                ║
 * ║  @ManyToMany                                                                  ║
 * ║    → Many-to-many relationship (junction table)                               ║
 * ║    → Example: User { @ManyToMany Set<Role> roles }                            ║
 * ║                                                                                ║
 * ║  @JoinColumn(name = "user_id")                                                ║
 * ║    → Specifies the foreign key column name                                    ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  FETCH TYPES (IMPORTANT FOR PERFORMANCE):                                     ║
 * ║                                                                                ║
 * ║  LAZY (default for collections):                                              ║
 * ║    → Related data is NOT loaded until accessed                                ║
 * ║    → Better performance, but can cause LazyInitializationException            ║
 * ║                                                                                ║
 * ║  EAGER:                                                                       ║
 * ║    → Related data is ALWAYS loaded with the parent                            ║
 * ║    → Simpler, but can load too much data (N+1 problem)                        ║
 * ║                                                                                ║
 * ║  RECOMMENDATION: Start with LAZY, use JOIN FETCH in queries when needed       ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ JPA entity annotations                                                     ║
 * ║  ✓ Field definitions matching database columns                                ║
 * ║  ✓ Relationship mappings                                                      ║
 * ║  ✓ Validation annotations on fields                                           ║
 * ║  ✓ Lifecycle callbacks (@PrePersist, @PreUpdate)                              ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ Business logic (belongs in Service)                                        ║
 * ║  ✗ API-specific fields (use DTOs for that)                                    ║
 * ║  ✗ Complex calculations (do in Service)                                       ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  ENTITY vs DTO:                                                               ║
 * ║                                                                                ║
 * ║  Entity (this package):                                                       ║
 * ║  - Maps directly to database                                                  ║
 * ║  - Contains ALL fields from the table                                         ║
 * ║  - Has relationships to other entities                                        ║
 * ║  - Should never be exposed directly to API                                    ║
 * ║                                                                                ║
 * ║  DTO (dto package):                                                           ║
 * ║  - Shaped for specific API needs                                              ║
 * ║  - Contains only fields needed for that operation                             ║
 * ║  - No database/JPA annotations                                                ║
 * ║  - Safe to expose in API responses                                            ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE ENTITY:
 * 
 * @Entity
 * @Table(name = "users")
 * @Data  // Lombok: generates getters, setters, toString, etc.
 * @NoArgsConstructor
 * @AllArgsConstructor
 * @Builder
 * public class User {
 *     
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.IDENTITY)
 *     private Long id;
 *     
 *     @Column(nullable = false)
 *     private String name;
 *     
 *     @Column(nullable = false, unique = true)
 *     private String email;
 *     
 *     @Column(nullable = false)
 *     private String password;  // Hashed, never plain text!
 *     
 *     @Enumerated(EnumType.STRING)
 *     private UserRole role;
 *     
 *     // One user can have many bookings
 *     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
 *     private List<Booking> bookings = new ArrayList<>();
 *     
 *     // Audit fields
 *     @CreatedDate
 *     private LocalDateTime createdAt;
 *     
 *     @LastModifiedDate
 *     private LocalDateTime updatedAt;
 * }
 */
package com.quickserve.model;
