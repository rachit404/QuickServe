/**
 * PROVIDER SERVICE
 * 
 * Handles service provider related API calls:
 * - List providers by category
 * - Get provider details
 * - Search providers
 */
import api from './api'
import { ServiceProvider, ProviderListResponse, Category } from '../types/provider'

/**
 * Get all service categories.
 * Used in navigation and service browsing.
 */
export const getCategories = async (): Promise<Category[]> => {
  const response = await api.get<Category[]>('/categories')
  return response.data
}

/**
 * Get providers by category (paginated).
 */
export const getProvidersByCategory = async (
  categoryId: number,
  page: number = 0,
  size: number = 10
): Promise<ProviderListResponse> => {
  const response = await api.get<ProviderListResponse>(`/providers/category/${categoryId}`, {
    params: { page, size }
  })
  return response.data
}

/**
 * Get a single provider's details.
 */
export const getProviderById = async (id: number): Promise<ServiceProvider> => {
  const response = await api.get<ServiceProvider>(`/providers/${id}`)
  return response.data
}

/**
 * Get top-rated providers in a category.
 */
export const getTopProviders = async (categoryId: number): Promise<ServiceProvider[]> => {
  const response = await api.get<ServiceProvider[]>(`/providers/category/${categoryId}/top`)
  return response.data
}

/**
 * Search providers by area.
 */
export const searchProviders = async (area: string): Promise<ServiceProvider[]> => {
  const response = await api.get<ServiceProvider[]>('/providers/search', {
    params: { area }
  })
  return response.data
}
