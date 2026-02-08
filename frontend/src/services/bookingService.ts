/**
 * BOOKING SERVICE
 * 
 * Handles all booking-related API calls:
 * - Create bookings
 * - Get user's bookings
 * - Cancel bookings
 * - Provider: Accept/reject bookings
 */
import api from './api'
import { 
  Booking, 
  BookingRequest, 
  BookingResponse,
  BookingListResponse 
} from '../types/booking'

/**
 * Create a new booking.
 * Customer books a service with a provider.
 */
export const createBooking = async (data: BookingRequest): Promise<BookingResponse> => {
  const response = await api.post<BookingResponse>('/bookings', data)
  return response.data
}

/**
 * Get current user's bookings (paginated).
 */
export const getMyBookings = async (
  page: number = 0, 
  size: number = 10
): Promise<BookingListResponse> => {
  const response = await api.get<BookingListResponse>('/bookings/my', {
    params: { page, size }
  })
  return response.data
}

/**
 * Get a single booking by ID.
 */
export const getBookingById = async (id: number): Promise<Booking> => {
  const response = await api.get<Booking>(`/bookings/${id}`)
  return response.data
}

/**
 * Cancel a booking.
 * Can be called by customer or provider.
 */
export const cancelBooking = async (id: number, reason: string): Promise<Booking> => {
  const response = await api.put<Booking>(`/bookings/${id}/cancel`, { reason })
  return response.data
}

/**
 * Provider: Accept a pending booking.
 */
export const acceptBooking = async (id: number): Promise<Booking> => {
  const response = await api.put<Booking>(`/bookings/${id}/accept`)
  return response.data
}

/**
 * Provider: Reject a pending booking.
 */
export const rejectBooking = async (id: number, reason?: string): Promise<Booking> => {
  const response = await api.put<Booking>(`/bookings/${id}/reject`, { reason })
  return response.data
}

/**
 * Provider: Mark booking as complete.
 */
export const completeBooking = async (id: number, finalAmount: number): Promise<Booking> => {
  const response = await api.put<Booking>(`/bookings/${id}/complete`, { finalAmount })
  return response.data
}
