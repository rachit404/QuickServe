# QuickServe Frontend

> A beginner-friendly React/TypeScript frontend for a local services booking platform.

## 📚 Table of Contents

1. [Understanding React + TypeScript](#understanding-react--typescript)
2. [Project Structure](#project-structure)
3. [Key Concepts](#key-concepts)
4. [Folder-by-Folder Guide](#folder-by-folder-guide)
5. [State Management](#state-management)
6. [Styling with Tailwind CSS](#styling-with-tailwind-css)
7. [API Integration](#api-integration)
8. [Running the Application](#running-the-application)
9. [Adding New Features](#adding-new-features)

---

## Understanding React + TypeScript

### What is React?

React is a JavaScript library for building user interfaces. Key concepts:

- **Components:** Reusable building blocks (like LEGO pieces)
- **JSX:** HTML-like syntax in JavaScript
- **State:** Data that changes over time
- **Props:** Data passed from parent to child component

### Why TypeScript?

TypeScript adds types to JavaScript, catching bugs before they happen:

```typescript
// Without TypeScript - bug discovered at runtime
function add(a, b) {
  return a + b
}
add("5", 3) // Returns "53" - not what we wanted!

// With TypeScript - bug caught at compile time
function add(a: number, b: number): number {
  return a + b
}
add("5", 3) // Error: Argument of type 'string' is not assignable
```

---

## Project Structure

```
frontend/
├── index.html               # HTML entry point
├── package.json             # NPM dependencies
├── tsconfig.json            # TypeScript config
├── tailwind.config.js       # Tailwind CSS config
├── vite.config.ts           # Vite build config
│
└── src/
    ├── main.tsx             # React entry point
    ├── App.tsx              # Root component with routes
    ├── index.css            # Global styles
    │
    ├── components/          # Reusable UI components
    │   └── common/          # Shared components (Navbar, etc.)
    │
    ├── pages/               # Page components (one per route)
    │   ├── Home.tsx
    │   ├── Login.tsx
    │   ├── Register.tsx
    │   ├── Services.tsx
    │   ├── Booking.tsx
    │   ├── MyBookings.tsx
    │   └── ProviderDashboard.tsx
    │
    ├── services/            # API layer
    │   ├── api.ts           # Axios configuration
    │   ├── authService.ts   # Auth API calls
    │   ├── bookingService.ts
    │   └── providerService.ts
    │
    ├── context/             # React Context (global state)
    │   └── AuthContext.tsx
    │
    ├── hooks/               # Custom React hooks
    │
    └── types/               # TypeScript interfaces
        ├── user.ts
        ├── booking.ts
        └── provider.ts
```

---

## Key Concepts

### Components

```tsx
// Functional component with props
interface ButtonProps {
  label: string
  onClick: () => void
}

function Button({ label, onClick }: ButtonProps) {
  return <button onClick={onClick}>{label}</button>
}
```

### Hooks

```tsx
// useState - manage component state
const [count, setCount] = useState(0)

// useEffect - run side effects
useEffect(() => {
  fetchData()
}, [dependency]) // Runs when dependency changes
```

### Routing

```tsx
<Routes>
  <Route path="/" element={<Home />} />
  <Route path="/login" element={<Login />} />
</Routes>
```

---

## Folder-by-Folder Guide

### 📁 pages/

**Purpose:** One component per route/page.

Each file represents a full page the user can visit:
- `Home.tsx` → `/`
- `Login.tsx` → `/login`
- `Services.tsx` → `/services`

### 📁 components/

**Purpose:** Reusable UI building blocks.

Components used in multiple places:
- `Navbar.tsx` - appears on every page
- `Button.tsx` - reusable button component
- `Card.tsx` - reusable card container

### 📁 services/

**Purpose:** API communication layer.

Separates HTTP logic from components:

```tsx
// In component:
const data = await bookingService.getMyBookings()

// NOT this:
const data = await axios.get('/api/bookings/my')
```

### 📁 context/

**Purpose:** Global state accessible from any component.

Example: User authentication state is needed everywhere:
- Navbar needs to show user name
- Pages need to check if user is logged in
- Multiple components need to call logout

### 📁 types/

**Purpose:** TypeScript interfaces shared across files.

Defines the shape of data:

```typescript
interface User {
  id: number
  name: string
  email: string
}
```

---

## State Management

### Local State (useState)

For data only one component needs:

```tsx
const [isOpen, setIsOpen] = useState(false)
```

### Context (useContext)

For data multiple components need:

```tsx
// Use auth state anywhere
const { user, login, logout } = useAuth()
```

### When to Use What

| State Type | Use Case |
|-----------|----------|
| `useState` | Form inputs, UI toggles, local data |
| Context | Auth, theme, global settings |
| URL params | Page-specific data (booking ID) |

---

## Styling with Tailwind CSS

### Utility Classes

Instead of writing CSS, compose styles with classes:

```tsx
// Traditional CSS
<div className="container">
// + CSS file: .container { max-width: 1200px; margin: 0 auto; }

// Tailwind
<div className="max-w-7xl mx-auto">
```

### Common Patterns

| What You Want | Tailwind Classes |
|--------------|------------------|
| Centered container | `max-w-7xl mx-auto px-4` |
| Flex row | `flex items-center gap-4` |
| Grid | `grid md:grid-cols-3 gap-6` |
| Card | `bg-white rounded-xl shadow-lg p-6` |
| Primary button | `bg-primary-600 text-white px-4 py-2 rounded-lg` |

### Responsive Design

```tsx
// Stacks on mobile, grid on desktop
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
```

---

## API Integration

### Request Flow

```
Component → Service → Axios → Backend API
    │
    ├── Loading state shown
    │
    ├── Success: Update state, show data
    │
    └── Error: Show error message
```

### Example

```tsx
const [bookings, setBookings] = useState<Booking[]>([])
const [isLoading, setIsLoading] = useState(true)
const [error, setError] = useState('')

useEffect(() => {
  loadBookings()
}, [])

const loadBookings = async () => {
  try {
    const data = await bookingService.getMyBookings()
    setBookings(data.bookings)
  } catch (err) {
    setError('Failed to load bookings')
  } finally {
    setIsLoading(false)
  }
}
```

---

## Running the Application

### Prerequisites

- Node.js 18+
- npm 9+

### Development

```bash
# Install dependencies
npm install

# Start dev server
npm run dev
```

Open `http://localhost:3000`

### Production Build

```bash
npm run build
```

---

## Adding New Features

### Adding a New Page

1. Create component in `src/pages/`
2. Add route in `App.tsx`
3. Add navigation link in `Navbar.tsx`

### Adding a New API Endpoint

1. Add TypeScript types in `src/types/`
2. Add service function in appropriate service file
3. Use in component with loading/error states

### Best Practices

- ✅ Keep components small and focused
- ✅ Extract reusable logic into hooks
- ✅ Use TypeScript strictly (avoid `any`)
- ✅ Handle loading and error states
- ✅ Use semantic HTML
- ❌ Don't put API calls directly in components
- ❌ Don't ignore TypeScript errors
