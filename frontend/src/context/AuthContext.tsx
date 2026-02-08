/**
 * AUTHENTICATION CONTEXT
 * 
 * React Context for managing authentication state globally.
 * 
 * WHAT IS CONTEXT:
 * Context provides a way to pass data through the component tree
 * without having to pass props down manually at every level.
 * 
 * HOW IT WORKS:
 * 1. AuthProvider wraps the entire app
 * 2. Any component can access auth state using useAuth() hook
 * 3. When state changes, all consumers re-render
 */
import { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { User } from '../types/user'
import * as authService from '../services/authService'

/**
 * Shape of the auth context value.
 */
interface AuthContextType {
  user: User | null
  isLoading: boolean
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  register: (name: string, email: string, password: string) => Promise<void>
  logout: () => void
}

// Create the context with undefined default
const AuthContext = createContext<AuthContextType | undefined>(undefined)

/**
 * Provider component that wraps app and provides auth state.
 */
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  
  // On mount, check if user is already logged in
  useEffect(() => {
    checkAuth()
  }, [])
  
  /**
   * Check if user has valid session.
   * Called on app startup.
   */
  const checkAuth = async () => {
    try {
      if (authService.isAuthenticated()) {
        const currentUser = await authService.getCurrentUser()
        setUser(currentUser)
      }
    } catch (error) {
      // Token might be expired
      authService.logout()
    } finally {
      setIsLoading(false)
    }
  }
  
  /**
   * Login with email and password.
   */
  const login = async (email: string, password: string) => {
    const response = await authService.login({ email, password })
    setUser(response.user)
  }
  
  /**
   * Register new user.
   */
  const register = async (name: string, email: string, password: string) => {
    const response = await authService.register({ name, email, password })
    setUser(response.user)
  }
  
  /**
   * Logout user.
   */
  const logout = () => {
    authService.logout()
    setUser(null)
  }
  
  const value: AuthContextType = {
    user,
    isLoading,
    isAuthenticated: !!user,
    login,
    register,
    logout,
  }
  
  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

/**
 * Hook to access auth context.
 * MUST be used within AuthProvider.
 */
export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  
  return context
}
