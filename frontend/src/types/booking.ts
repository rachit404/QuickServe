/**
 * BOOKING TYPES
 * 
 * TypeScript interfaces for booking-related data.
 */

/**
 * Booking status.
 * Should match backend BookingStatus enum.
 */
export type BookingStatus = 
  | 'PENDING' 
  | 'CONFIRMED' 
  | 'REJECTED' 
  | 'IN_PROGRESS' 
  | 'COMPLETED' 
  | 'CANCELLED'

/**
 * Booking entity as returned from API.
 */
export interface Booking {
  id: number
  userId: number
  providerId: number
  providerName: string
  scheduledDate: string
  durationMinutes: number
  status: BookingStatus
  address: string
  notes?: string
  quotedAmount?: number
  finalAmount?: number
  rating?: number
  review?: string
  createdAt: string
}

/**
 * Request to create a new booking.
 */
export interface BookingRequest {
  providerId: number
  scheduledDate: string
  durationMinutes?: number
  address: string
  notes?: string
}

/**
 * Response after creating a booking.
 */
export interface BookingResponse {
  success: boolean
  message: string
  booking: Booking
}

/**
 * Paginated list of bookings.
 */
export interface BookingListResponse {
  bookings: Booking[]
  totalPages: number
  totalElements: number
  currentPage: number
}
