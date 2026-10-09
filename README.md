# 🚗 RideRent — Vehicle Rental & Fleet Management System

> **"Rent. Ride. Return."**  
> An enterprise-grade, commercial-ready, full-stack vehicle rental and fleet management platform built with modern architectural standards.

---

## 🌟 Key Highlights & Philosophy

* **Monochrome Precision**: Strict **Black + White** visual design system (`#000000`, `#FFFFFF`, `#F8F8F8`, `#111111`, `#666666`, `#E5E5E5`). Clean typography with modern aesthetics, zero arbitrary colors or rainbow gradients.
* **Pure Automotive & Fleet Operations**: Focused 100% on real-world vehicle leasing, reservations, telemetry specs, maintenance scheduling, and business analytics. **Zero AI gimmicks**.
* **Enterprise Layered Architecture**:
  ```text
  React 19 + TypeScript + Vite + Tailwind CSS (Port 5173)
                            ↓  RESTful JSON / JWT
  Spring Boot 3.3.4 (Port 8085)
     ├── Controller  (REST Endpoints, DTO Validation)
     ├── Service     (Business Logic, Double-Booking Shield, Financial Formulae)
     ├── Repository  (Spring Data JPA / JPQL Queries)
     └── Database    (MySQL / Embedded H2 Dialect)
  ```
* **Source of Truth Pricing**: All quotes, taxes (GST 18%), refundable security deposits, and promotional coupon discounts are dynamically evaluated by the backend. Client submissions are verified and recalculated server-side.
* **Digital Paperwork**: Automated PDF generation for both **Official Invoices** and **Digital Rental Agreements** via OpenPDF.

---

## 🛠️ Technology Stack

### Frontend
- **Framework**: React 18 / 19 + TypeScript
- **Build Tool**: Vite (Lightning fast HMR & Rollup bundling)
- **Styling**: Tailwind CSS (Monochrome design system, customized utility tokens)
- **Icons**: Lucide React
- **Routing**: React Router DOM v6 (Protected & Admin Route Guards)
- **Networking**: Axios with request/response interceptors and automatic JWT injection

### Backend
- **Platform**: Java 21+ / Spring Boot 3.3.4
- **Security**: Spring Security 6 with stateless JWT authentication (HMAC-SHA512)
- **Persistence**: Spring Data JPA + Hibernate ORM
- **Validation**: Jakarta Bean Validation (`@Valid`, custom error response wrapping)
- **PDF Engine**: OpenPDF 1.3.39 (vector tables, official letterheads, digital receipts)
- **Profiles**: Zero-config in-memory embedded DB (`dev`) and production MySQL (`mysql`)

---

## 🚀 Quick Start Guide

### 1. Prerequisites
- Java JDK 21 or higher installed
- Node.js 18+ and npm installed
- Maven 3.9+ (A portable Maven binary is included at `apache-maven-3.9.6\bin\mvn.cmd`)

---

### 2. Backend Setup & Run

1. Open a terminal in `./backend`:
   ```powershell
   cd backend
   ```
2. Run the Spring Boot server with the portable Maven runner:
   ```powershell
   & "..\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
   ```
   *The backend will automatically start on **http://localhost:8085**, launch the embedded in-memory database, and automatically seed sample vehicles, users, active rentals, and coupons.*

---

### 3. Frontend Setup & Run

1. Open a separate terminal in `./frontend`:
   ```powershell
   cd frontend
   npm install
   npm run dev
   ```
2. Access the application in your browser:
   👉 **http://localhost:5173**

---

---

## 👥 Initial Administrator Account

| Role | Email | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **System Admin** | `admin@example.com` | `admin123` | Full access to Fleet inventory, booking dispatches, customer account controls, maintenance logs, coupons, and revenue analytics. |

> New customers can register directly through `/register`. Real vehicles can be added immediately via the `/admin` portal. Zero fake/demo vehicles or mock bookings are seeded.

---

## 📁 System Architecture & Directory Structure

```text
Vehcile_Rental_System/
├── ARCHITECTURE.md                 # Detailed architecture specification & schema design
├── README.md                       # Comprehensive setup and operations manual
├── apache-maven-3.9.6/             # Portable Maven distribution
├── backend/
│   ├── src/main/java/com/vehiclerental/
│   │   ├── config/                 # Security beans, CORS filter, DataSeeder
│   │   ├── controller/             # REST API controllers
│   │   ├── dto/                    # Request and response transfer objects
│   │   ├── entity/                 # JPA Entities (Vehicle, Booking, User, Payment, etc.)
│   │   ├── repository/             # Spring Data repositories with custom JPQL queries
│   │   ├── security/               # JWT filters, UserDetails, and auth entry points
│   │   ├── service/                # Business logic interfaces & implementations
│   │   └── exception/              # Global exception handler & custom exceptions
│   └── src/main/resources/
│       └── application.yml         # Dual profile configuration (dev & mysql)
└── frontend/
    ├── src/
    │   ├── components/             # Reusable UI (common, layout, vehicle, booking)
    │   ├── context/                # Auth, Toast, Wishlist, Compare contexts
    │   ├── pages/
    │   │   ├── public/             # Home, Vehicles catalog, VehicleDetails, Compare
    │   │   ├── auth/               # Login, Register, ForgotPassword
    │   │   ├── user/               # UserDashboard (Bookings, Active Rental, Wishlist)
    │   │   └── admin/              # AdminDashboard (Metrics, Fleet, Dispatches, Servicing)
    │   ├── routes/                 # RouteGuards (ProtectedRoute, AdminRoute)
    │   ├── services/               # Axios API abstraction services
    │   └── types/                  # Strict TypeScript interfaces
    └── tailwind.config.js          # Monochrome design system tokens
```

---

## 🔒 Security & Robustness

- **Password Hashing**: BCrypt with salted rounds. Passwords are never stored or logged in plain text.
- **Double Booking Protection**: Server-side JPQL overlap queries guarantee that no two overlapping reservations can be booked for the same vehicle.
- **Maintenance Lock**: Any vehicle placed under servicing (`MAINTENANCE`) is automatically locked from bookings until released by an administrator.
- **Digital Signatures & Invoices**: Downloadable PDF receipts and binding rental agreements generated dynamically with immutable booking references.

---

## 📄 License
This project was developed as a clean, commercial-standard portfolio demonstration.
Built with clean code principles, DRY & SOLID software design.
