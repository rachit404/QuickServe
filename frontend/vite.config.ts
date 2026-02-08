import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

/**
 * VITE CONFIGURATION
 * 
 * Vite is a modern build tool that's MUCH faster than webpack.
 * It uses native ES modules in development for instant hot reload.
 * 
 * KEY FEATURES:
 * - Hot Module Replacement (HMR) - changes appear instantly
 * - Path aliases - clean imports like @/components/Button
 * - Proxy - redirect API calls to backend during development
 */
export default defineConfig({
  plugins: [react()],
  
  // Path aliases (must match tsconfig.json paths)
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      '@components': path.resolve(__dirname, './src/components'),
      '@pages': path.resolve(__dirname, './src/pages'),
      '@services': path.resolve(__dirname, './src/services'),
      '@hooks': path.resolve(__dirname, './src/hooks'),
      '@types': path.resolve(__dirname, './src/types'),
      '@utils': path.resolve(__dirname, './src/utils'),
    },
  },
  
  // Development server configuration
  server: {
    port: 3000,
    
    // Proxy API calls to backend
    // This solves CORS issues during development!
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  
  // Build configuration
  build: {
    outDir: 'dist',
    sourcemap: true,
  },
})
