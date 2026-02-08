/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                           CONTROLLER LAYER                                     ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  Controllers are the "front door" of your application. They receive HTTP      ║
 * ║  requests, delegate work to services, and return HTTP responses.               ║
 * ║                                                                                ║
 * ║  Think of it as a receptionist in an office:                                  ║
 * ║  - Receives visitors (HTTP requests)                                          ║
 * ║  - Directs them to the right department (Service layer)                       ║
 * ║  - Doesn't do the actual work themselves                                      ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ REST endpoint definitions (@GetMapping, @PostMapping, etc.)                ║
 * ║  ✓ Request parameter handling (@PathVariable, @RequestBody, etc.)             ║
 * ║  ✓ Response formatting (ResponseEntity, HTTP status codes)                    ║
 * ║  ✓ Input validation triggering (@Valid annotation)                            ║
 * ║  ✓ Authentication/authorization annotations (@PreAuthorize)                   ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ Business logic (should be in Service layer)                                ║
 * ║  ✗ Database queries (should be in Repository layer)                           ║
 * ║  ✗ Complex data transformations (should be in Service layer)                  ║
 * ║  ✗ Direct entity exposure (use DTOs instead)                                  ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  REQUEST LIFECYCLE:                                                           ║
 * ║                                                                                ║
 * ║  HTTP Request                                                                 ║
 * ║       ↓                                                                        ║
 * ║  [Security Filter] ──→ Validates JWT, checks permissions                      ║
 * ║       ↓                                                                        ║
 * ║  [Controller] ──→ Receives request, validates input                           ║
 * ║       ↓                                                                        ║
 * ║  [Service] ──→ Executes business logic                                        ║
 * ║       ↓                                                                        ║
 * ║  [Repository] ──→ Queries database                                            ║
 * ║       ↓                                                                        ║
 * ║  [Database] ──→ Returns data                                                  ║
 * ║       ↓                                                                        ║
 * ║  Response flows back up through the layers                                    ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  NAMING CONVENTIONS:                                                          ║
 * ║  - Class: [Resource]Controller (e.g., UserController, BookingController)      ║
 * ║  - Methods: descriptive verb phrases (getUserById, createBooking)             ║
 * ║  - URL paths: lowercase, plural nouns (/users, /bookings)                     ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  COMMON ANNOTATIONS YOU'LL SEE:                                               ║
 * ║                                                                                ║
 * ║  @RestController = @Controller + @ResponseBody                                ║
 * ║    → Marks class as a REST API controller                                     ║
 * ║    → Return values are automatically serialized to JSON                       ║
 * ║                                                                                ║
 * ║  @RequestMapping("/api/v1/users")                                             ║
 * ║    → Base URL path for all endpoints in this controller                       ║
 * ║                                                                                ║
 * ║  @GetMapping("/{id}")                                                         ║
 * ║    → Maps HTTP GET requests to this method                                    ║
 * ║                                                                                ║
 * ║  @PostMapping                                                                 ║
 * ║    → Maps HTTP POST requests (typically for creating resources)               ║
 * ║                                                                                ║
 * ║  @PathVariable                                                                ║
 * ║    → Extracts value from URL path (e.g., /users/{id} → id = 123)             ║
 * ║                                                                                ║
 * ║  @RequestBody                                                                 ║
 * ║    → Deserializes JSON request body into a Java object                        ║
 * ║                                                                                ║
 * ║  @Valid                                                                       ║
 * ║    → Triggers validation on the request object                                ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE CONTROLLER STRUCTURE:
 * 
 * @RestController
 * @RequestMapping("/api/v1/users")
 * @RequiredArgsConstructor  // Lombok: auto-injects dependencies
 * public class UserController {
 *     
 *     private final UserService userService;  // Injected by Spring
 *     
 *     @GetMapping("/{id}")
 *     public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
 *         // Controller only delegates to service - no business logic here!
 *         return ResponseEntity.ok(userService.getUserById(id));
 *     }
 *     
 *     @PostMapping
 *     public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
 *         UserResponse created = userService.createUser(request);
 *         return ResponseEntity.status(HttpStatus.CREATED).body(created);
 *     }
 * }
 */
package com.quickserve.controller;
