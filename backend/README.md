# QuickServe Backend

> A beginner-friendly Spring Boot backend for a local services booking platform.

This document explains everything you need to know about the backend architecture, from "what is Spring Boot?" to "where should I add new features?"

## 📚 Table of Contents

1. [Understanding Spring Boot](#understanding-spring-boot)
2. [Project Structure Overview](#project-structure-overview)
3. [Package-by-Package Explanation](#package-by-package-explanation)
4. [Request Lifecycle](#request-lifecycle)
5. [Understanding Key Technologies](#understanding-key-technologies)
6. [Caching with Redis](#caching-with-redis)
7. [Running the Application](#running-the-application)
8. [Adding New Features](#adding-new-features)

---

## Understanding Spring Boot

### What is Spring Boot?

**Spring Boot** is a framework that makes it easier to create stand-alone, production-grade Spring applications. Think of it as a "starter kit" that:

- **Auto-configures** most things for you (database connections, web server, etc.)
- **Provides embedded servers** (Tomcat) so you don't need to install one separately
- **Manages dependencies** through "starters" (bundles of related libraries)

### Why Spring Boot for QuickServe?

| Feature | Why It Matters |
|---------|---------------|
| **Mature Ecosystem** | Millions of developers, endless Stack Overflow answers |
| **Enterprise Ready** | Used by Netflix, Amazon, and thousands of companies |
| **Batteries Included** | Security, caching, database access all built-in |
| **Easy Scaling** | Can evolve from monolith to microservices |

---

## Project Structure Overview

```
backend/
├── pom.xml                             # Maven dependencies
├── src/main/java/com/quickserve/
│   ├── QuickServeApplication.java      # Entry point
│   │
│   ├── controller/                     # HTTP endpoints
│   ├── service/                        # Business logic
│   ├── repository/                     # Database access
│   ├── model/                          # Database entities
│   │   └── enums/                      # Status enums
│   ├── dto/                            # Request/Response objects
│   │   ├── request/                    # Incoming data
│   │   └── response/                   # Outgoing data
│   ├── config/                         # Configuration classes
│   ├── security/                       # JWT authentication
│   ├── cache/                          # Redis caching
│   ├── exception/                      # Error handling
│   └── util/                           # Helper utilities
│
└── src/main/resources/
    ├── application.yml                 # Main config
    ├── application-dev.yml             # Dev settings
    └── application-prod.yml            # Production settings
```

---

## Package-by-Package Explanation

### 📁 controller/

**Purpose:** Receive HTTP requests and return HTTP responses.

**What goes here:**
- REST endpoint definitions
- Request validation
- Response status codes

**What does NOT go here:**
- Business logic (goes in service/)
- Database queries (goes in repository/)

```java
// Example: BookingController.java
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        // Controller only calls service - NO business logic here!
        return ResponseEntity.ok(bookingService.createBooking(request));
    }
}
```

---

### 📁 service/

**Purpose:** Contains ALL business logic. The heart of your application.

**What goes here:**
- Business rules ("Can't cancel within 24 hours")
- Coordination between repositories
- Data transformation (Entity → DTO)
- Transaction management

**Key Annotations:**
- `@Service` - Marks as a Spring-managed service
- `@Transactional` - All database operations in a method succeed or fail together

```java
// Example: BookingService.java
@Service
public class BookingService {
    
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Business rule: Check provider availability
        if (hasConflict) {
            throw new BadRequestException("Provider not available");
        }
        // Database operation
        Booking saved = bookingRepository.save(booking);
        // Transform to DTO
        return mapToResponse(saved);
    }
}
```

---

### 📁 repository/

**Purpose:** Database access layer using Spring Data JPA.

**Magic Feature:** Spring generates implementations from method names!

```java
// You write:
Optional<User> findByEmail(String email);

// Spring generates:
SELECT * FROM users WHERE email = ?
```

**Method Naming Patterns:**
| Method Name | Generated Query |
|-------------|-----------------|
| `findByEmail` | WHERE email = ? |
| `findByEmailAndActive` | WHERE email = ? AND active = ? |
| `findByCreatedAtAfter` | WHERE created_at > ? |
| `countByStatus` | SELECT COUNT(*) WHERE status = ? |

---

### 📁 model/

**Purpose:** JPA entities that map to database tables.

**Important Annotations:**
| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as a database table |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-generate IDs |
| `@Column` | Column configuration |
| `@ManyToOne` | Foreign key relationship |

**entity vs DTO:**
- **Entity:** Matches database exactly, has JPA annotations
- **DTO:** Shaped for API, only includes needed fields

---

### 📁 dto/

**Purpose:** Data Transfer Objects for API communication.

**Why not just use entities?**
1. **Security:** Don't expose passwords or internal fields
2. **Flexibility:** Different endpoints return different fields
3. **Stability:** API contract independent of database structure

**Structure:**
```
dto/
├── request/          # Data coming IN
│   └── BookingRequest.java
└── response/         # Data going OUT
    └── BookingResponse.java
```

---

### 📁 config/

**Purpose:** Spring configuration and customization.

**Key Configurations:**
- `SecurityConfig` - Authentication/authorization rules
- `RedisConfig` - Cache connection settings
- `CorsConfig` - Allow frontend to call backend
- `SwaggerConfig` - API documentation

---

### 📁 security/

**Purpose:** JWT authentication system.

**Components:**
- `JwtTokenProvider` - Create and validate tokens
- `JwtAuthenticationFilter` - Intercept requests to check tokens
- `CustomUserDetailsService` - Load user from database

---

### 📁 cache/

**Purpose:** Redis caching for performance.

**When to cache:**
- Data that doesn't change often (categories)
- Expensive queries (search results)
- Temporary data (OTPs)

---

### 📁 exception/

**Purpose:** Handle errors gracefully.

**Components:**
- Custom exceptions (`ResourceNotFoundException`)
- `GlobalExceptionHandler` - Converts exceptions to HTTP responses

---

## Request Lifecycle

Here's what happens when a request hits the server:

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                              HTTP REQUEST                                        │
│                    POST /api/v1/bookings                                        │
│                    Body: { "providerId": 1, "date": "2024-01-20" }              │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (1) CORS FILTER                                                                │
│      Checks if request origin is allowed                                        │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (2) JWT FILTER                                                                 │
│      Extracts token from Authorization header                                   │
│      Validates token signature and expiration                                   │
│      Loads user details and sets authentication                                 │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (3) CONTROLLER                                                                 │
│      @PostMapping("/api/v1/bookings")                                           │
│      Validates request body (@Valid)                                            │
│      Calls service method                                                       │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (4) SERVICE                                                                    │
│      Applies business rules                                                     │
│      Checks provider availability                                               │
│      Calls repository to save                                                   │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (5) REPOSITORY                                                                 │
│      Spring Data JPA generates SQL                                              │
│      INSERT INTO bookings VALUES (...)                                          │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (6) DATABASE                                                                   │
│      PostgreSQL stores the record                                               │
│      Returns generated ID                                                       │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│  (7) RESPONSE                                                                   │
│      Service transforms Entity → DTO                                            │
│      Controller returns ResponseEntity                                          │
│      JSON: { "id": 1, "status": "PENDING", ... }                               │
└────────────────────────────────────────────────────────────────────────────────┘
```

---

## Understanding Key Technologies

### What is JPA (Java Persistence API)?

JPA is a specification for ORM (Object-Relational Mapping). It lets you:
- Work with database tables as Java objects
- Write Java code instead of SQL
- Switch databases without changing code

**Hibernate** is the implementation (the actual code that does the work).

### Why PostgreSQL?

| Feature | Why It's Great |
|---------|---------------|
| **Reliability** | ACID compliant, data integrity guaranteed |
| **JSON Support** | Store flexible data when needed |
| **Performance** | Excellent for read-heavy workloads |
| **Open Source** | Free, strong community |

### What is JWT Authentication?

JWT (JSON Web Token) is a stateless authentication method:

1. **Login:** Server validates credentials, returns a signed token
2. **Subsequent Requests:** Client sends token, server validates signature
3. **No Session Storage:** Server doesn't need to remember logged-in users

**Token Structure:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGUiOiJVU0VSIn0.xxx
└─────── Header ───────┘└────────────── Payload ─────────────────┘└ Signature ┘
```

---

## Caching with Redis

### What is Redis?

An in-memory data store - essentially a super-fast dictionary. Reading from Redis is ~100x faster than PostgreSQL.

### How We Use Redis in QuickServe

**1. Caching Service Listings**
```java
@Cacheable(value = "categories", key = "'all'")
public List<Category> getAllCategories() {
    return categoryRepository.findAll();  // Only runs on cache miss
}
```

**2. Storing OTPs**
```java
// Store OTP with 5-minute expiry
redisTemplate.opsForValue().set("otp:" + email, otp, Duration.ofMinutes(5));
```

**3. Rate Limiting**
```java
// Track API calls per user
String key = "rate:" + userId;
Long count = redisTemplate.opsForValue().increment(key);
```

### Cache Flow

```
Request for categories
        │
        ▼
┌───────────────┐    Cache Hit     ┌─────────────────┐
│   Check       │ ────────────────▶ │  Return from    │
│   Redis       │                   │  Redis (1ms)    │
└───────────────┘                   └─────────────────┘
        │
        │ Cache Miss
        ▼
┌───────────────┐                   ┌─────────────────┐
│  Query        │ ─────────────────▶│  Store in       │
│  PostgreSQL   │                   │  Redis + Return │
│  (100ms)      │                   │                 │
└───────────────┘                   └─────────────────┘
```

---

## Running the Application

### Prerequisites

- Java 17+
- PostgreSQL 14+
- Redis 6+
- Maven 3.8+

### Setup Steps

1. **Create Database**
   ```sql
   CREATE DATABASE quickserve_dev;
   ```

2. **Update Configuration**
   Edit `application-dev.yml` with your database credentials.

3. **Run Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access API Docs**
   Open `http://localhost:8080/api/swagger-ui.html`

---

## Adding New Features

### Adding a New Entity

1. Create entity in `model/` package
2. Create repository in `repository/` package
3. Create DTOs in `dto/request/` and `dto/response/`
4. Create service in `service/` package
5. Create controller in `controller/` package

### Adding a New Endpoint

1. Add method to appropriate controller
2. Add service method with business logic
3. Add repository method if new query needed
4. Add DTOs if new request/response shape needed

### Best Practices

- ✅ Business logic in Services, never in Controllers
- ✅ Use DTOs for API, never expose entities directly
- ✅ Use `@Transactional` for operations that change data
- ✅ Add validation annotations to request DTOs
- ✅ Write meaningful exception messages
- ❌ Don't put SQL in controllers
- ❌ Don't ignore exceptions
- ❌ Don't expose sensitive data in responses
