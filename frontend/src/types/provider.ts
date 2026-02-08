/**
 * PROVIDER & CATEGORY TYPES
 */

/**
 * Service category.
 */
export interface Category {
  id: number
  name: string
  slug: string
  description?: string
  icon?: string
  active: boolean
  displayOrder: number
  providerCount?: number
}

/**
 * Service provider entity.
 */
export interface ServiceProvider {
  id: number
  userId: number
  userName: string
  userEmail: string
  userPhone?: string
  categoryId: number
  categoryName: string
  bio?: string
  hourlyRate?: number
  rating: number
  totalRatings: number
  verified: boolean
  available: boolean
  serviceArea?: string
  experienceYears?: number
  createdAt: string
}

/**
 * Paginated provider list response.
 */
export interface ProviderListResponse {
  providers: ServiceProvider[]
  totalPages: number
  totalElements: number
  currentPage: number
}
