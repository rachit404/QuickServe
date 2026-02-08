/**
 * PROVIDER DASHBOARD
 * 
 * Dashboard for service providers to manage bookings.
 * TODO: Implement booking management and analytics.
 */
export default function ProviderDashboard() {
  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Provider Dashboard</h1>
      <div className="grid md:grid-cols-3 gap-6 mb-8">
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-600">Pending</h3>
          <p className="text-3xl font-bold">0</p>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-600">Today's Bookings</h3>
          <p className="text-3xl font-bold">0</p>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-600">This Month</h3>
          <p className="text-3xl font-bold">₹0</p>
        </div>
      </div>
      <div className="card">
        <h2 className="text-xl font-semibold mb-4">Recent Bookings</h2>
        <p className="text-gray-500">No bookings yet.</p>
      </div>
    </div>
  )
}
