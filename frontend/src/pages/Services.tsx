/**
 * SERVICES PAGE
 * 
 * Browse and filter service providers by category.
 * TODO: Implement full functionality.
 */
import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { Category, ServiceProvider } from '../types/provider'
import { getCategories, getProvidersByCategory } from '../services/providerService'

export default function Services() {
  const { categorySlug } = useParams()
  const [categories, setCategories] = useState<Category[]>([])
  const [providers, setProviders] = useState<ServiceProvider[]>([])
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  
  useEffect(() => {
    loadCategories()
  }, [])
  
  useEffect(() => {
    if (selectedCategory) {
      loadProviders(selectedCategory.id)
    }
  }, [selectedCategory])
  
  const loadCategories = async () => {
    try {
      const data = await getCategories()
      setCategories(data)
      
      if (categorySlug) {
        const category = data.find(c => c.slug === categorySlug)
        setSelectedCategory(category || null)
      }
    } catch (error) {
      console.error('Failed to load categories:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  const loadProviders = async (categoryId: number) => {
    setIsLoading(true)
    try {
      const data = await getProvidersByCategory(categoryId)
      setProviders(data.providers)
    } catch (error) {
      console.error('Failed to load providers:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">
        {selectedCategory ? selectedCategory.name : 'All Services'}
      </h1>
      
      {/* Category Filters */}
      <div className="flex flex-wrap gap-2 mb-8">
        {categories.map(category => (
          <button
            key={category.id}
            onClick={() => setSelectedCategory(category)}
            className={`px-4 py-2 rounded-full transition-colors ${
              selectedCategory?.id === category.id
                ? 'bg-primary-600 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            }`}
          >
            {category.name}
          </button>
        ))}
      </div>
      
      {/* Providers Grid */}
      {isLoading ? (
        <div className="text-center text-gray-500 py-12">Loading providers...</div>
      ) : providers.length === 0 ? (
        <div className="text-center text-gray-500 py-12">
          {selectedCategory ? 'No providers found in this category.' : 'Select a category to view providers.'}
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {providers.map(provider => (
            <div key={provider.id} className="card">
              <h3 className="text-xl font-semibold mb-2">{provider.userName}</h3>
              <p className="text-gray-600 mb-4">{provider.bio || 'No bio available'}</p>
              <div className="flex items-center justify-between">
                <span className="text-primary-600 font-semibold">
                  ₹{provider.hourlyRate}/hr
                </span>
                <Link to={`/booking/${provider.id}`} className="btn-primary">
                  Book Now
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
