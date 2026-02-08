/**
 * USER TYPES
 * 
 * TypeScript interfaces for user-related data.
 * 
 * WHY TYPESCRIPT TYPES:
 * - Catch errors at compile time
 * - Better IDE autocomplete
 * - Self-documenting code
 * - Easier refactoring
 */

/**
 * User roles in the system.
 * Should match backend UserRole enum.
 */
export type UserRole = 'CUSTOMER' | 'SERVICE_PROVIDER' | 'ADMIN'

/**
 * User entity as returned from API.
 */
export interface User {
  id: number
  name: string
  email: string
  phoneNumber?: string
  role: UserRole
  emailVerified: boolean
  createdAt: string
}

/**
 * Login request payload.
 */
export interface LoginRequest {
  email: string
  password: string
}

/**
 * Register request payload.
 */
export interface RegisterRequest {
  name: string
  email: string
  password: string
  phoneNumber?: string
  role?: UserRole
}

/**
 * Authentication response with JWT token.
 */
export interface AuthResponse {
  token: string
  user: User
  expiresIn: number
}
