/** @type {import('tailwindcss').Config} */
export default {
  /*
   * TAILWIND CSS CONFIGURATION
   * 
   * Tailwind is a utility-first CSS framework.
   * Instead of writing CSS classes like .button-primary,
   * you compose styles with utilities like .bg-blue-500.
   * 
   * WHY TAILWIND:
   * - No context switching between HTML and CSS files
   * - Consistent design tokens (colors, spacing, etc.)
   * - Dead CSS elimination in production
   * - Responsive design made easy (sm:, md:, lg:)
   */
  
  // Files where Tailwind should look for class names
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  
  theme: {
    extend: {
      // Custom colors for QuickServe brand
      colors: {
        primary: {
          50: '#f0f9ff',
          100: '#e0f2fe',
          200: '#bae6fd',
          300: '#7dd3fc',
          400: '#38bdf8',
          500: '#0ea5e9',  // Main brand color
          600: '#0284c7',
          700: '#0369a1',
          800: '#075985',
          900: '#0c4a6e',
        },
        secondary: {
          50: '#fdf4ff',
          100: '#fae8ff',
          200: '#f5d0fe',
          300: '#f0abfc',
          400: '#e879f9',
          500: '#d946ef',  // Accent color
          600: '#c026d3',
          700: '#a21caf',
          800: '#86198f',
          900: '#701a75',
        },
      },
      
      // Custom fonts
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      
      // Animation for loading states
      animation: {
        'spin-slow': 'spin 3s linear infinite',
        'pulse-slow': 'pulse 3s infinite',
      },
    },
  },
  
  plugins: [],
}
