/**
 * NAVBAR COMPONENT
 * 
 * Site-wide navigation bar with:
 * - Logo
 * - Navigation links
 * - Auth buttons (Login/Register or User menu)
 */
import { Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth()
  
  return (
    <nav className="bg-white shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center">
              <span className="text-2xl font-bold text-gradient">
                QuickServe
              </span>
            </Link>
          </div>
          
          {/* Navigation Links */}
          <div className="hidden sm:flex sm:items-center sm:space-x-8">
            <Link 
              to="/services" 
              className="text-gray-600 hover:text-gray-900 transition-colors"
            >
              Services
            </Link>
            
            {isAuthenticated && (
              <Link 
                to="/my-bookings" 
                className="text-gray-600 hover:text-gray-900 transition-colors"
              >
                My Bookings
              </Link>
            )}
            
            {user?.role === 'SERVICE_PROVIDER' && (
              <Link 
                to="/provider/dashboard" 
                className="text-gray-600 hover:text-gray-900 transition-colors"
              >
                Dashboard
              </Link>
            )}
          </div>
          
          {/* Auth Buttons */}
          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <span className="text-gray-700">
                  Hi, {user?.name.split(' ')[0]}
                </span>
                <button 
                  onClick={logout}
                  className="btn-secondary"
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="text-gray-600 hover:text-gray-900">
                  Login
                </Link>
                <Link to="/register" className="btn-primary">
                  Register
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  )
}
