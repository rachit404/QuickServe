/**
 * API SERVICE LAYER
 * 
 * This file creates a configured Axios instance for making HTTP requests.
 * 
 * WHY A SEPARATE API LAYER:
 * 1. Single place to configure base URL, headers, etc.
 * 2. Intercept requests to add auth tokens
 * 3. Intercept responses to handle errors globally
 * 4. Easy to mock for testing
 * 
 * AXIOS vs FETCH:
 * Axios provides:
 * - Automatic JSON parsing
 * - Request/response interceptors
 * - Request cancellation
 * - Better error handling
 */
import axios, { AxiosError, AxiosResponse } from 'axios'

// Create axios instance with default config
const api = axios.create({
  // Base URL for all requests
  // In development, Vite proxy forwards /api to backend
  baseURL: '/api',
  
  // Default headers
  headers: {
    'Content-Type': 'application/json',
  },
  
  // Request timeout (10 seconds)
  timeout: 10000,
})

/**
 * REQUEST INTERCEPTOR
 * 
 * Runs BEFORE every request is sent.
 * Used to add authentication token to headers.
 */
api.interceptors.request.use(
  (config) => {
    // Get token from localStorage
    const token = localStorage.getItem('token')
    
    // If token exists, add it to Authorization header
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

/**
 * RESPONSE INTERCEPTOR
 * 
 * Runs AFTER every response is received.
 * Used to handle common response scenarios:
 * - 401 Unauthorized → Redirect to login
 * - Transform data
 * - Log errors
 */
api.interceptors.response.use(
  // Successful response (2xx)
  (response: AxiosResponse) => {
    return response
  },
  
  // Error response (non-2xx)
  (error: AxiosError) => {
    // Handle 401 Unauthorized
    if (error.response?.status === 401) {
      // Clear stored token
      localStorage.removeItem('token')
      
      // Redirect to login if not already there
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }
    
    // Handle network errors
    if (!error.response) {
      console.error('Network error:', error.message)
    }
    
    return Promise.reject(error)
  }
)

export default api
