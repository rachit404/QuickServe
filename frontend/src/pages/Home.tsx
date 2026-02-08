/**
 * HOME PAGE
 * 
 * Landing page with:
 * - Hero section
 * - Featured categories
 * - How it works section
 * - Call to action
 */
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { Category } from '../types/provider'
import { getCategories } from '../services/providerService'

export default function Home() {
  const [categories, setCategories] = useState<Category[]>([])
  const [isLoading, setIsLoading] = useState(true)
  
  useEffect(() => {
    loadCategories()
  }, [])
  
  const loadCategories = async () => {
    try {
      const data = await getCategories()
      setCategories(data.slice(0, 6)) // Show first 6
    } catch (error) {
      console.error('Failed to load categories:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  return (
    <div>
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-primary-600 to-primary-800 text-white">
        <div className="max-w-7xl mx-auto px-4 py-20 sm:py-32">
          <div className="text-center">
            <h1 className="text-4xl sm:text-6xl font-bold mb-6">
              Book Local Services
              <br />
              <span className="text-primary-200">In Minutes</span>
            </h1>
            <p className="text-xl text-primary-100 mb-8 max-w-2xl mx-auto">
              Find trusted professionals for plumbing, electrical, tutoring, 
              and more. Book instantly, pay securely.
            </p>
            <div className="flex justify-center gap-4">
              <Link to="/services" className="btn-primary bg-white text-primary-600 hover:bg-gray-100">
                Browse Services
              </Link>
              <Link to="/register" className="btn-secondary bg-transparent border-2 border-white text-white hover:bg-white/10">
                Join as Provider
              </Link>
            </div>
          </div>
        </div>
      </section>
      
      {/* Categories Section */}
      <section className="max-w-7xl mx-auto px-4 py-16">
        <h2 className="text-3xl font-bold text-center mb-12">
          Popular Services
        </h2>
        
        {isLoading ? (
          <div className="text-center text-gray-500">Loading...</div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-6">
            {categories.map((category) => (
              <Link
                key={category.id}
                to={`/services/${category.slug}`}
                className="card text-center hover:shadow-xl transition-shadow group"
              >
                <div className="text-4xl mb-3">{category.icon || '🔧'}</div>
                <h3 className="font-semibold text-gray-900 group-hover:text-primary-600">
                  {category.name}
                </h3>
              </Link>
            ))}
          </div>
        )}
      </section>
      
      {/* How It Works */}
      <section className="bg-gray-100 py-16">
        <div className="max-w-7xl mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">
            How It Works
          </h2>
          
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-primary-600">1</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Choose a Service</h3>
              <p className="text-gray-600">Browse categories and find the right professional.</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-primary-600">2</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Book Instantly</h3>
              <p className="text-gray-600">Select a time slot and confirm your booking.</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-primary-600">3</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Get It Done</h3>
              <p className="text-gray-600">Professional arrives, completes the job, you pay.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  )
}
