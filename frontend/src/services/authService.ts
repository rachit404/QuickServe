/**
 * AUTHENTICATION SERVICE
 * 
 * Handles all auth-related API calls:
 * - Login
 * - Register
 * - Get current user
 * - Logout
 * 
 * This service layer abstracts API calls so components
 * don't need to know the endpoint URLs or data formats.
 */
import api from './api'
import { User, LoginRequest, RegisterRequest, AuthResponse } from '../types/user'

/**
 * Login with email and password.
 * Returns JWT token on success.
 */
export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>('/auth/login', credentials)
  
  // Store token in localStorage
  if (response.data.token) {
    localStorage.setItem('token', response.data.token)
  }
  
  return response.data
}

/**
 * Register a new user.
 * Returns JWT token on success (auto-login after register).
 */
export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>('/auth/register', data)
  
  // Store token in localStorage
  if (response.data.token) {
    localStorage.setItem('token', response.data.token)
  }
  
  return response.data
}

/**
 * Get the current logged-in user's details.
 * Called on app startup to restore session.
 */
export const getCurrentUser = async (): Promise<User> => {
  const response = await api.get<User>('/auth/me')
  return response.data
}

/**
 * Logout - remove token from storage.
 * Optionally call backend to invalidate token.
 */
export const logout = (): void => {
  localStorage.removeItem('token')
  // Could also call API to invalidate token in Redis
  // await api.post('/auth/logout')
}

/**
 * Check if user is authenticated.
 * Quick check without API call.
 */
export const isAuthenticated = (): boolean => {
  return !!localStorage.getItem('token')
}
