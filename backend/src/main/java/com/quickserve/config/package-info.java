/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                          CONFIGURATION LAYER                                   ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  Configuration classes customize how Spring and its modules behave.           ║
 * ║  They're like preference settings for your application.                        ║
 * ║                                                                                ║
 * ║  Think of it as the "settings panel":                                         ║
 * ║  - How should security work?                                                  ║
 * ║  - How should we connect to Redis?                                            ║
 * ║  - Which origins are allowed for CORS?                                        ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  SPRING CONFIGURATION BASICS:                                                 ║
 * ║                                                                                ║
 * ║  @Configuration                                                               ║
 * ║    → Marks class as a source of bean definitions                              ║
 * ║    → Spring processes this class at startup                                   ║
 * ║                                                                                ║
 * ║  @Bean                                                                        ║
 * ║    → Marks a method as a bean producer                                        ║
 * ║    → The return value becomes a Spring-managed object                         ║
 * ║    → Other components can inject this bean                                    ║
 * ║                                                                                ║
 * ║  Example:                                                                     ║
 * ║  @Configuration                                                               ║
 * ║  public class MyConfig {                                                      ║
 * ║      @Bean                                                                    ║
 * ║      public RestTemplate restTemplate() {                                     ║
 * ║          return new RestTemplate();  // Now available for injection anywhere  ║
 * ║      }                                                                        ║
 * ║  }                                                                            ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  KEY CONFIGURATIONS IN QUICKSERVE:                                            ║
 * ║                                                                                ║
 * ║  SecurityConfig:                                                              ║
 * ║    → Configure authentication and authorization                               ║
 * ║    → Define which endpoints are public/protected                              ║
 * ║    → Set up JWT filter                                                        ║
 * ║    → Configure password encoder                                               ║
 * ║                                                                                ║
 * ║  RedisConfig:                                                                 ║
 * ║    → Configure Redis connection settings                                      ║
 * ║    → Set up RedisTemplate for cache operations                                ║
 * ║    → Configure serialization for stored objects                               ║
 * ║                                                                                ║
 * ║  CorsConfig:                                                                  ║
 * ║    → Allow frontend (different origin) to call backend                        ║
 * ║    → Specify allowed origins, methods, headers                                ║
 * ║    → Critical for frontend-backend communication                              ║
 * ║                                                                                ║
 * ║  SwaggerConfig:                                                               ║
 * ║    → Configure OpenAPI/Swagger documentation                                  ║
 * ║    → Set API info, security schemes                                           ║
 * ║    → Customize how endpoints appear in docs                                   ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ Spring Security configuration                                              ║
 * ║  ✓ Third-party library configurations                                         ║
 * ║  ✓ Bean definitions for custom components                                     ║
 * ║  ✓ Cross-cutting concerns (logging, auditing)                                 ║
 * ║  ✓ Profile-specific configurations                                            ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ Business logic (belongs in Service)                                        ║
 * ║  ✗ Data access (belongs in Repository)                                        ║
 * ║  ✗ API endpoints (belongs in Controller)                                      ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  UNDERSTANDING CORS:                                                          ║
 * ║                                                                                ║
 * ║  CORS (Cross-Origin Resource Sharing) is a security feature that blocks       ║
 * ║  web pages from making requests to a different domain than the one that       ║
 * ║  served the page.                                                             ║
 * ║                                                                                ║
 * ║  Example:                                                                     ║
 * ║  - Frontend runs on: http://localhost:3000                                    ║
 * ║  - Backend runs on: http://localhost:8080                                     ║
 * ║  - Browser blocks this by default!                                            ║
 * ║                                                                                ║
 * ║  We configure CORS to explicitly allow our frontend to call our backend.      ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE CONFIGURATION:
 * 
 * @Configuration
 * public class CorsConfig {
 *     
 *     @Bean
 *     public WebMvcConfigurer corsConfigurer() {
 *         return new WebMvcConfigurer() {
 *             @Override
 *             public void addCorsMappings(CorsRegistry registry) {
 *                 registry.addMapping("/ **")
 *                     .allowedOrigins("http://localhost:3000")  // Frontend URL
 *                     .allowedMethods("GET", "POST", "PUT", "DELETE")
 *                     .allowedHeaders("*")
 *                     .allowCredentials(true);
 *             }
 *         };
 *     }
 * }
 */
package com.quickserve.config;
