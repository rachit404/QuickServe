/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                          SECURITY LAYER                                        ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  The Security layer handles authentication (who you are) and authorization    ║
 * ║  (what you can do). This is where JWT token handling lives.                    ║
 * ║                                                                                ║
 * ║  Think of it as building security:                                            ║
 * ║  - Guards at the door (authentication filter)                                 ║
 * ║  - ID badges (JWT tokens)                                                     ║
 * ║  - Access control (who can enter which rooms)                                 ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  HOW JWT AUTHENTICATION WORKS:                                                ║
 * ║                                                                                ║
 * ║  1. LOGIN FLOW:                                                               ║
 * ║     User sends credentials → Server validates → Server returns JWT            ║
 * ║                                                                                ║
 * ║     POST /api/auth/login                                                      ║
 * ║     { "email": "...", "password": "..." }                                     ║
 * ║                    ↓                                                          ║
 * ║     { "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..." }                            ║
 * ║                                                                                ║
 * ║  2. SUBSEQUENT REQUESTS:                                                      ║
 * ║     Client includes JWT in Authorization header                               ║
 * ║                                                                                ║
 * ║     GET /api/bookings                                                         ║
 * ║     Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...                     ║
 * ║                                                                                ║
 * ║  3. JWT VALIDATION:                                                           ║
 * ║     For every request, the JwtAuthenticationFilter:                           ║
 * ║     - Extracts token from header                                              ║
 * ║     - Validates signature (wasn't tampered with)                              ║
 * ║     - Checks expiration                                                       ║
 * ║     - Loads user details                                                      ║
 * ║     - Sets authentication in SecurityContext                                  ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  JWT TOKEN STRUCTURE:                                                         ║
 * ║                                                                                ║
 * ║  A JWT has three parts separated by dots:                                     ║
 * ║  xxxxx.yyyyy.zzzzz                                                            ║
 * ║    ↓     ↓     ↓                                                              ║
 * ║  Header.Payload.Signature                                                     ║
 * ║                                                                                ║
 * ║  Header: Algorithm and token type                                             ║
 * ║  { "alg": "HS256", "typ": "JWT" }                                             ║
 * ║                                                                                ║
 * ║  Payload: Claims (user data)                                                  ║
 * ║  { "sub": "user@email.com", "role": "USER", "exp": 1234567890 }               ║
 * ║                                                                                ║
 * ║  Signature: Verifies token wasn't modified                                    ║
 * ║  HMACSHA256(base64(header) + "." + base64(payload), secret)                   ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  KEY COMPONENTS:                                                              ║
 * ║                                                                                ║
 * ║  JwtTokenProvider:                                                            ║
 * ║    → Generates JWT tokens                                                     ║
 * ║    → Validates tokens                                                         ║
 * ║    → Extracts user info from tokens                                           ║
 * ║                                                                                ║
 * ║  JwtAuthenticationFilter:                                                     ║
 * ║    → Intercepts every request                                                 ║
 * ║    → Extracts and validates JWT                                               ║
 * ║    → Sets authentication in SecurityContext                                   ║
 * ║                                                                                ║
 * ║  CustomUserDetailsService:                                                    ║
 * ║    → Loads user from database by username/email                               ║
 * ║    → Converts User entity to Spring Security's UserDetails                    ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  SECURITY CONFIGURATION FLOW:                                                 ║
 * ║                                                                                ║
 * ║  HTTP Request                                                                 ║
 * ║       ↓                                                                       ║
 * ║  [CORS Filter] ──→ Handles cross-origin requests                              ║
 * ║       ↓                                                                       ║
 * ║  [JWT Filter] ──→ Validates token, sets authentication                        ║
 * ║       ↓                                                                       ║
 * ║  [Authorization] ──→ Checks if user can access endpoint                       ║
 * ║       ↓                                                                       ║
 * ║  [Controller] ──→ Handles the request                                         ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ JWT generation and validation                                              ║
 * ║  ✓ Authentication filters                                                     ║
 * ║  ✓ UserDetails loading                                                        ║
 * ║  ✓ Custom authentication providers                                            ║
 * ║                                                                                ║
 * ║  SecurityConfig (in config/ package) sets up these components.                ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE JWT FILTER:
 * 
 * @Component
 * @RequiredArgsConstructor
 * public class JwtAuthenticationFilter extends OncePerRequestFilter {
 *     
 *     private final JwtTokenProvider tokenProvider;
 *     private final CustomUserDetailsService userDetailsService;
 *     
 *     @Override
 *     protected void doFilterInternal(HttpServletRequest request,
 *                                     HttpServletResponse response,
 *                                     FilterChain filterChain) {
 *         
 *         // 1. Extract token from header
 *         String token = getTokenFromRequest(request);
 *         
 *         // 2. Validate token
 *         if (token != null && tokenProvider.validateToken(token)) {
 *             
 *             // 3. Get user email from token
 *             String email = tokenProvider.getEmailFromToken(token);
 *             
 *             // 4. Load user details
 *             UserDetails userDetails = userDetailsService.loadUserByUsername(email);
 *             
 *             // 5. Set authentication in context
 *             Authentication auth = new UsernamePasswordAuthenticationToken(
 *                 userDetails, null, userDetails.getAuthorities()
 *             );
 *             SecurityContextHolder.getContext().setAuthentication(auth);
 *         }
 *         
 *         // 6. Continue filter chain
 *         filterChain.doFilter(request, response);
 *     }
 * }
 */
package com.quickserve.security;
