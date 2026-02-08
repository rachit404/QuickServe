/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                          EXCEPTION LAYER                                       ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  PURPOSE:                                                                      ║
 * ║  This package handles errors gracefully. Instead of returning ugly stack      ║
 * ║  traces, we return user-friendly error messages with appropriate HTTP codes.  ║
 * ║                                                                                ║
 * ║  Think of it as a PR team:                                                    ║
 * ║  - Something goes wrong internally                                            ║
 * ║  - PR team crafts a proper response for the public                            ║
 * ║  - Users see: "User not found" not: "NullPointerException at line 452"        ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHY CUSTOM EXCEPTIONS:                                                       ║
 * ║                                                                                ║
 * ║  1. MEANINGFUL MESSAGES:                                                      ║
 * ║     throw new ResourceNotFoundException("User with ID 123 not found")         ║
 * ║     vs                                                                        ║
 * ║     throw new RuntimeException("user missing")                                ║
 * ║                                                                                ║
 * ║  2. AUTOMATIC HTTP STATUS CODES:                                              ║
 * ║     ResourceNotFoundException → 404 NOT FOUND                                 ║
 * ║     BadRequestException → 400 BAD REQUEST                                     ║
 * ║     UnauthorizedException → 401 UNAUTHORIZED                                  ║
 * ║                                                                                ║
 * ║  3. CONSISTENT API RESPONSES:                                                 ║
 * ║     All errors follow the same format:                                        ║
 * ║     {                                                                         ║
 * ║       "success": false,                                                       ║
 * ║       "message": "User not found",                                            ║
 * ║       "timestamp": "2024-01-15T10:30:00"                                      ║
 * ║     }                                                                         ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  COMMON HTTP STATUS CODES:                                                    ║
 * ║                                                                                ║
 * ║  SUCCESS:                                                                     ║
 * ║    200 OK - Request succeeded                                                 ║
 * ║    201 CREATED - Resource created                                             ║
 * ║    204 NO CONTENT - Success but no response body                              ║
 * ║                                                                                ║
 * ║  CLIENT ERRORS:                                                               ║
 * ║    400 BAD REQUEST - Invalid input                                            ║
 * ║    401 UNAUTHORIZED - Not authenticated                                       ║
 * ║    403 FORBIDDEN - Authenticated but not authorized                           ║
 * ║    404 NOT FOUND - Resource doesn't exist                                     ║
 * ║    409 CONFLICT - Resource already exists                                     ║
 * ║    422 UNPROCESSABLE ENTITY - Validation failed                               ║
 * ║                                                                                ║
 * ║  SERVER ERRORS:                                                               ║
 * ║    500 INTERNAL SERVER ERROR - Something broke                                ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  GLOBAL EXCEPTION HANDLER:                                                    ║
 * ║                                                                                ║
 * ║  @ControllerAdvice annotated class intercepts all exceptions and converts     ║
 * ║  them to proper HTTP responses. This is a cross-cutting concern - you         ║
 * ║  define it once and it applies to ALL controllers.                            ║
 * ║                                                                                ║
 * ║  How it works:                                                                ║
 * ║  1. Controller throws ResourceNotFoundException                               ║
 * ║  2. GlobalExceptionHandler catches it                                         ║
 * ║  3. Handler returns ResponseEntity with status 404 and error message          ║
 * ║  4. Client receives proper JSON error response                                ║
 * ║                                                                                ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                ║
 * ║  WHAT BELONGS HERE:                                                           ║
 * ║  ✓ Custom exception classes                                                   ║
 * ║  ✓ Global exception handler                                                   ║
 * ║  ✓ Error response DTOs                                                        ║
 * ║                                                                                ║
 * ║  WHAT DOES NOT BELONG HERE:                                                   ║
 * ║  ✗ Business logic                                                             ║
 * ║  ✗ Data access                                                                ║
 * ║  ✗ Logging configuration                                                      ║
 * ║                                                                                ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * EXAMPLE CUSTOM EXCEPTION:
 * 
 * @ResponseStatus(HttpStatus.NOT_FOUND)
 * public class ResourceNotFoundException extends RuntimeException {
 *     public ResourceNotFoundException(String message) {
 *         super(message);
 *     }
 *     
 *     public ResourceNotFoundException(String resource, Long id) {
 *         super(String.format("%s with ID %d not found", resource, id));
 *     }
 * }
 * 
 * EXAMPLE GLOBAL EXCEPTION HANDLER:
 * 
 * @RestControllerAdvice
 * @Slf4j
 * public class GlobalExceptionHandler {
 *     
 *     @ExceptionHandler(ResourceNotFoundException.class)
 *     @ResponseStatus(HttpStatus.NOT_FOUND)
 *     public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
 *         log.warn("Resource not found: {}", ex.getMessage());
 *         return ErrorResponse.builder()
 *             .success(false)
 *             .message(ex.getMessage())
 *             .timestamp(LocalDateTime.now())
 *             .build();
 *     }
 *     
 *     @ExceptionHandler(MethodArgumentNotValidException.class)
 *     @ResponseStatus(HttpStatus.BAD_REQUEST)
 *     public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
 *         // Extract validation errors and return them
 *         Map<String, String> errors = new HashMap<>();
 *         ex.getBindingResult().getFieldErrors().forEach(error -> 
 *             errors.put(error.getField(), error.getDefaultMessage())
 *         );
 *         return ErrorResponse.builder()
 *             .success(false)
 *             .message("Validation failed")
 *             .errors(errors)
 *             .timestamp(LocalDateTime.now())
 *             .build();
 *     }
 * }
 */
package com.quickserve.exception;
