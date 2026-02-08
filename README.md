# QuickServe

> **Local Services Booking Platform** - Book trusted professionals for plumbing, electrical, tutoring, and more.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## 🎯 What is QuickServe?

QuickServe is a full-stack platform connecting customers with local service providers. Think of it as **Urban Company** or **TaskRabbit** - a marketplace for home services, tutoring, fitness training, and more.

**For Customers:**
- Browse service categories
- Find rated, verified providers
- Book instantly, pay securely
- Leave reviews

**For Service Providers:**
- Create a professional profile
- Receive booking requests
- Manage schedule
- Build reputation

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              FRONTEND                                        │
│                     React + TypeScript + Tailwind CSS                        │
│                                                                             │
│  ┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐                  │
│  │  Pages   │   │Components│   │ Services │   │  Context │                  │
│  │ (Routes) │   │   (UI)   │   │  (API)   │   │ (State)  │                  │
│  └──────────┘   └──────────┘   └──────────┘   └──────────┘                  │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ HTTP (REST API)
                                    │ JWT Authentication
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              BACKEND                                         │
│                  Java + Spring Boot + Spring Security                        │
│                                                                             │
│  ┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐                  │
│  │Controller│──▶│ Service  │──▶│Repository│──▶│  Entity  │                  │
│  └──────────┘   └──────────┘   └──────────┘   └──────────┘                  │
│                                                                             │
│  ┌──────────────────────┐   ┌──────────────────────┐                        │
│  │      Security        │   │       Caching        │                        │
│  │   (JWT, Filters)     │   │  (Redis, @Cacheable) │                        │
│  └──────────────────────┘   └──────────────────────┘                        │
└─────────────────────────────────────────────────────────────────────────────┘
                    │                               │
                    ▼                               ▼
           ┌──────────────┐                ┌──────────────┐
           │  PostgreSQL  │                │    Redis     │
           │  (Database)  │                │   (Cache)    │
           └──────────────┘                └──────────────┘
```

---

## 📁 Project Structure

```
QuickServe/
│
├── backend/                         # Spring Boot application
│   ├── pom.xml                      # Maven dependencies
│   ├── README.md                    # Backend documentation
│   └── src/main/java/com/quickserve/
│       ├── controller/              # REST endpoints
│       ├── service/                 # Business logic
│       ├── repository/              # Data access
│       ├── model/                   # JPA entities
│       ├── dto/                     # Request/Response objects
│       ├── config/                  # Spring configuration
│       ├── security/                # JWT authentication
│       ├── cache/                   # Redis caching
│       ├── exception/               # Error handling
│       └── util/                    # Helpers
│
├── frontend/                        # React application
│   ├── package.json                 # NPM dependencies
│   ├── README.md                    # Frontend documentation
│   └── src/
│       ├── components/              # Reusable UI
│       ├── pages/                   # Page components
│       ├── services/                # API layer
│       ├── context/                 # Global state
│       ├── hooks/                   # Custom hooks
│       └── types/                   # TypeScript types
│
└── README.md                        # This file
```

---

## 🚀 Tech Stack

### Frontend
| Technology | Purpose |
|------------|---------|
| **React 18** | UI library |
| **TypeScript** | Type safety |
| **Vite** | Build tool (fast!) |
| **Tailwind CSS** | Styling |
| **React Router** | Client-side routing |
| **Axios** | HTTP client |

### Backend
| Technology | Purpose |
|------------|---------|
| **Java 17** | Programming language |
| **Spring Boot 3** | Application framework |
| **Spring Security** | Authentication |
| **Spring Data JPA** | Database access |
| **PostgreSQL** | Primary database |
| **Redis** | Caching & sessions |
| **JWT** | Token authentication |

---

## 🔧 Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Redis 6+
- Maven 3.8+

### Backend Setup

```bash
# Navigate to backend
cd backend

# Create database
createdb quickserve_dev

# Run application
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080/api`

### Frontend Setup

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Start dev server
npm run dev
```

Frontend runs at: `http://localhost:3000`

---

## 📖 Documentation

Each subfolder has its own detailed README:

- [**Backend README**](./backend/README.md) - Complete Spring Boot guide
- [**Frontend README**](./frontend/README.md) - Complete React/TypeScript guide

---

## 🎓 Learning Path

New to this project? Here's the recommended order:

1. **Start with the Backend README** - Understand Spring Boot concepts
2. **Read the package-info.java files** - Each package explains its purpose
3. **Move to the Frontend README** - Understand React patterns
4. **Study the types/ folder** - See how data flows
5. **Trace a request** - Follow a booking from UI to database

---

## 📋 Features (Planned)

### MVP (Current Phase)
- [ ] User authentication (JWT)
- [ ] Service provider profiles
- [ ] Category browsing
- [ ] Booking creation
- [ ] Basic dashboard

### Phase 2
- [ ] Real-time notifications
- [ ] Payment integration
- [ ] Reviews & ratings
- [ ] Search & filters

### Phase 3
- [ ] Admin dashboard
- [ ] Analytics
- [ ] Mobile app (React Native)

---

## 🤝 Contributing

This project is designed for learning. Feel free to:
- Ask questions via issues
- Suggest improvements
- Submit pull requests

---

## 📄 License

MIT License - see LICENSE file for details.