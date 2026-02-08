/**
 * ROOT APPLICATION COMPONENT
 * 
 * This component:
 * - Wraps the entire app with context providers
 * - Defines all routes (pages)
 * - Contains the main layout structure
 */
import { Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'

// Layout components
import Navbar from './components/common/Navbar'

// Page components
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Services from './pages/Services'
import Booking from './pages/Booking'
import MyBookings from './pages/MyBookings'
import ProviderDashboard from './pages/ProviderDashboard'

function App() {
  return (
    // AuthProvider wraps the entire app to provide authentication state
    <AuthProvider>
      {/* Main layout container */}
      <div className="min-h-screen bg-gray-50">
        {/* Navbar appears on all pages */}
        <Navbar />
        
        {/* Main content area */}
        <main>
          {/* 
            Routes define which component renders at which URL.
            React Router matches the current URL to these patterns.
          */}
          <Routes>
            {/* Public routes - anyone can access */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/services" element={<Services />} />
            <Route path="/services/:categorySlug" element={<Services />} />
            
            {/* Protected routes - requires login (implement protection later) */}
            <Route path="/booking/:providerId" element={<Booking />} />
            <Route path="/my-bookings" element={<MyBookings />} />
            
            {/* Provider routes - requires SERVICE_PROVIDER role */}
            <Route path="/provider/dashboard" element={<ProviderDashboard />} />
            
            {/* 404 - catch all unmatched routes */}
            <Route path="*" element={
              <div className="flex items-center justify-center h-96">
                <h1 className="text-2xl text-gray-500">Page Not Found</h1>
              </div>
            } />
          </Routes>
        </main>
      </div>
    </AuthProvider>
  )
}

export default App
