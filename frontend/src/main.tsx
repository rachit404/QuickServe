/**
 * MAIN ENTRY POINT
 * 
 * This is where React mounts to the DOM.
 * The code here runs ONCE when the application starts.
 * 
 * STRICT MODE:
 * Wrapped in React.StrictMode which:
 * - Identifies unsafe lifecycle methods
 * - Warns about deprecated APIs
 * - Helps detect side effects
 * (Only affects development, not production)
 */
import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import './index.css'

// Find the root element in index.html
const rootElement = document.getElementById('root')!

// Create React root and render the app
ReactDOM.createRoot(rootElement).render(
  <React.StrictMode>
    {/* BrowserRouter enables client-side routing */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
)
