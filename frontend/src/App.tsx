import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ToastProvider } from './context/ToastContext';
import { WishlistProvider } from './context/WishlistContext';
import { CompareProvider } from './context/CompareContext';

// Layout
import { Navbar } from './components/layout/Navbar';
import { Footer } from './components/layout/Footer';

// Public Pages
import { Home } from './pages/public/Home';
import { Vehicles } from './pages/public/Vehicles';
import { VehicleDetails } from './pages/public/VehicleDetails';
import { Compare } from './pages/public/Compare';
import { About } from './pages/public/About';
import { Contact } from './pages/public/Contact';

// Auth Pages
import { Login } from './pages/auth/Login';
import { Register } from './pages/auth/Register';
import { ForgotPassword } from './pages/auth/ForgotPassword';

// User & Admin Portals
import UserDashboard from './pages/user/UserDashboard';
import AdminDashboard from './pages/admin/AdminDashboard';
import { ProtectedRoute, AdminRoute } from './routes/RouteGuards';

function App() {
  return (
    <BrowserRouter>
      <ToastProvider>
        <AuthProvider>
          <WishlistProvider>
            <CompareProvider>
              <div className="flex flex-col min-h-screen bg-[#FAFAFA] text-[#111111] antialiased selection:bg-black selection:text-white">
                <Navbar />
                <main className="flex-grow">
                  <Routes>
                    {/* Public Routes */}
                    <Route path="/" element={<Home />} />
                    <Route path="/vehicles" element={<Vehicles />} />
                    <Route path="/vehicles/:id" element={<VehicleDetails />} />
                    <Route path="/compare" element={<Compare />} />
                    <Route path="/about" element={<About />} />
                    <Route path="/contact" element={<Contact />} />

                    {/* Auth Routes */}
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/forgot-password" element={<ForgotPassword />} />

                    {/* Customer Protected Routes */}
                    <Route element={<ProtectedRoute />}>
                      <Route path="/dashboard" element={<UserDashboard />} />
                    </Route>

                    {/* Admin Protected Routes */}
                    <Route element={<AdminRoute />}>
                      <Route path="/admin" element={<AdminDashboard />} />
                    </Route>

                    {/* Fallback */}
                    <Route path="*" element={<Navigate to="/" replace />} />
                  </Routes>
                </main>
                <Footer />
              </div>
            </CompareProvider>
          </WishlistProvider>
        </AuthProvider>
      </ToastProvider>
    </BrowserRouter>
  );
}

export default App;
