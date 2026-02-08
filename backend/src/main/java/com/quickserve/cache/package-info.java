/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                            CACHE LAYER                                         ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  The Cache layer handles storing and retrieving frequently accessed data      ║
 * ║  in Redis, avoiding expensive database queries.                               ║
 * ║                                                                                ║
 * ║  Think of it as a sticky note system:                                         ║
 * ║  - Frequently looked-up info goes on a sticky note                            ║
 * ║  - Check the sticky note first before searching files                         ║
 * ║  - Much faster than going through the whole filing cabinet!                   ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHY CACHING MATTERS:                                                         ║
 * ║                                                                                ║
 * ║  Without caching:                                                             ║
 * ║    Request → Query DB → Return (100ms)                                        ║
 * ║    1000 requests = 1000 DB queries = DB overloaded!                           ║
 * ║                                                                                ║
 * ║  With caching:                                                                ║
 * ║    Request → Check cache → Cache hit? Return instantly! (1ms)                 ║
 * ║                          → Cache miss? Query DB → Store in cache → Return     ║
 * ║    1000 requests = 1 DB query + 999 cache hits = Happy database!              ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  REDIS USE CASES IN QUICKSERVE:                                               ║
 * ║                                                                                ║
 * ║  1. SERVICE LISTINGS CACHE:                                                   ║
 * ║     - Categories don't change often                                           ║
 * ║     - Service providers in an area don't change every minute                  ║
 * ║     - Cache for 10 minutes, serve thousands of requests                       ║
 * ║                                                                                ║
 * ║  2. OTP (One-Time Password) STORAGE:                                          ║
 * ║     - User requests password reset → Generate OTP                             ║
 * ║     - Store in Redis with 5-minute expiry                                     ║
 * ║     - Redis handles expiry automatically!                                     ║
 * ║                                                                                ║
 * ║  3. RATE LIMITING:                                                            ║
 * ║     - Track "user:123:api_calls" in Redis                                     ║
 * ║     - Increment on each request                                               ║
 * ║     - Block if exceeds limit                                                  ║
 * ║                                                                                ║
 * ║  4. SESSION/TOKEN BLACKLISTING:                                               ║
 * ║     - When user logs out, add token to blacklist                              ║
 * ║     - Check blacklist before validating JWT                                   ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  SPRING CACHING ANNOTATIONS:                                                  ║
 * ║                                                                                ║
 * ║  @Cacheable("categories")                                                     ║
 * ║    → First call: Execute method, store result in cache                        ║
 * ║    → Next calls: Return cached result without executing method                ║
 * ║                                                                                ║
 * ║  @CacheEvict("categories")                                                    ║
 * ║    → Remove item from cache                                                   ║
 * ║    → Use when data is updated or deleted                                      ║
 * ║                                                                                ║
 * ║  @CachePut("categories")                                                      ║
 * ║    → Always execute method AND update cache                                   ║
 * ║    → Use for update operations                                                ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  CACHE KEYS:                                                                  ║
 * ║                                                                                ║
 * ║  Keys are generated from method parameters:                                   ║
 * ║                                                                                ║
 * ║  @Cacheable(value = "providers", key = "#categoryId")                         ║
 * ║  List<Provider> getProvidersByCategory(Long categoryId)                       ║
 * ║                                                                                ║
 * ║  Cache key: "providers::5" (for categoryId = 5)                               ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  CACHE EVICTION STRATEGIES:                                                   ║
 * ║                                                                                ║
 * ║  TTL (Time To Live):                                                          ║
 * ║    - Cache expires after X minutes/hours                                      ║
 * ║    - Simple, but might serve stale data briefly                               ║
 * ║                                                                                ║
 * ║  Explicit Eviction:                                                           ║
 * ║    - Clear cache when data changes                                            ║
 * ║    - Always fresh, but more code to manage                                    ║
 * ║                                                                                ║
 * ║  Recommended: Use both! TTL as safety net + explicit eviction on updates      ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE CACHING USAGE:
 * 
 * @Service
 * @RequiredArgsConstructor
 * public class CategoryService {
 *     
 *     private final CategoryRepository categoryRepository;
 *     
 *     // First call: Queries database and caches result
 *     // Next 1000 calls: Returns from cache instantly!
 *     @Cacheable(value = "categories", key = "'all'")
 *     public List<CategoryResponse> getAllCategories() {
 *         return categoryRepository.findAll()
 *             .stream()
 *             .map(this::toResponse)
 *             .collect(Collectors.toList());
 *     }
 *     
 *     // When a category is updated, clear the cache
 *     @CacheEvict(value = "categories", allEntries = true)
 *     public CategoryResponse updateCategory(Long id, CategoryRequest request) {
 *         // ... update logic
 *         // Cache is automatically cleared after this method completes
 *     }
 * }
 * 
 * EXAMPLE CACHE SERVICE (for manual operations):
 * 
 * @Service
 * @RequiredArgsConstructor
 * public class CacheService {
 *     
 *     private final RedisTemplate<String, Object> redisTemplate;
 *     
 *     // Store OTP with expiry
 *     public void storeOtp(String email, String otp) {
 *         String key = "otp:" + email;
 *         redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(5));
 *     }
 *     
 *     // Verify OTP
 *     public boolean verifyOtp(String email, String otp) {
 *         String key = "otp:" + email;
 *         String stored = (String) redisTemplate.opsForValue().get(key);
 *         return otp.equals(stored);
 *     }
 * }
 */
package com.quickserve.cache;
