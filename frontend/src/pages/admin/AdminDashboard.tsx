import React, { useState, useEffect } from 'react';
import { 
  Users, Car, Calendar, DollarSign, Wrench, Star, CheckCircle, 
  XCircle, Clock, Shield, Search, Plus, Edit, Trash2, Check, RefreshCw 
} from 'lucide-react';
import { adminService } from '../../services/adminService';
import { useToast } from '../../context/ToastContext';
import { 
  DashboardStats, DashboardAnalytics, Vehicle, Booking, User, 
  Coupon, MaintenanceRecord, Review, BookingStatus 
} from '../../types';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';

export const AdminDashboard: React.FC = () => {
  const { showToast } = useToast();
  const [activeTab, setActiveTab] = useState<
    'overview' | 'vehicles' | 'bookings' | 'users' | 'maintenance' | 'coupons' | 'reviews'
  >('overview');

  // Stats & Analytics
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [analytics, setAnalytics] = useState<DashboardAnalytics | null>(null);
  const [loading, setLoading] = useState(true);

  // Entities
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [coupons, setCoupons] = useState<Coupon[]>([]);
  const [maintenance, setMaintenance] = useState<MaintenanceRecord[]>([]);
  const [reviews, setReviews] = useState<Review[]>([]);

  // Modals & Forms
  const [showAddVehicleModal, setShowAddVehicleModal] = useState(false);
  const [vehicleForm, setVehicleForm] = useState<Partial<Vehicle>>({
    brand: '',
    model: '',
    year: new Date().getFullYear(),
    vehicleType: 'SEDAN',
    registrationNumber: '',
    description: '',
    pricePerDay: 0,
    securityDeposit: 0,
    fuelType: 'Petrol',
    transmission: 'Automatic',
    seatingCapacity: 5,
    location: '',
    featured: false,
    imageUrls: [],
  });

  const [showCouponModal, setShowCouponModal] = useState(false);
  const [couponForm, setCouponForm] = useState<Partial<Coupon>>({
    code: '',
    discountType: 'PERCENTAGE',
    discountValue: 0,
    minimumBookingAmount: 0,
    maximumDiscount: 0,
    startDate: new Date().toISOString().slice(0, 10),
    expiryDate: new Date(Date.now() + 30 * 86400000).toISOString().slice(0, 10),
    usageLimit: 0,
    active: true,
  });

  const [showMaintenanceModal, setShowMaintenanceModal] = useState(false);
  const [maintenanceForm, setMaintenanceForm] = useState({
    vehicleId: 0,
    maintenanceType: 'SERVICE',
    description: '',
    cost: 0,
    startDate: new Date().toISOString().slice(0, 16),
    endDate: new Date(Date.now() + 86400000).toISOString().slice(0, 16),
  });

  const loadAllAdminData = async () => {
    setLoading(true);
    try {
      const [statsRes, analyticsRes, usersRes, couponsRes, maintRes, revRes] = await Promise.allSettled([
        adminService.getStats(),
        adminService.getAnalytics(),
        adminService.getUsers(undefined, 0, 50),
        adminService.getCoupons(),
        adminService.getMaintenanceRecords(undefined, 0, 50),
        adminService.getReviews(0, 50),
      ]);

      if (statsRes.status === 'fulfilled') setStats(statsRes.value);
      if (analyticsRes.status === 'fulfilled') setAnalytics(analyticsRes.value);
      if (usersRes.status === 'fulfilled') setUsers(usersRes.value.content || []);
      if (couponsRes.status === 'fulfilled') setCoupons(couponsRes.value || []);
      if (maintRes.status === 'fulfilled') setMaintenance(maintRes.value.content || []);
      if (revRes.status === 'fulfilled') setReviews(revRes.value.content || []);

      // Load vehicles & bookings
      const [vRes, bRes] = await Promise.allSettled([
        fetch('http://localhost:8085/api/vehicles?page=0&size=50').then((r) => r.json()),
        adminService.getBookings(undefined, undefined, 0, 50),
      ]);
      if (vRes.status === 'fulfilled' && vRes.value?.data?.content) {
        setVehicles(vRes.value.data.content);
      }
      if (bRes.status === 'fulfilled') {
        setBookings(bRes.value.content || []);
      }
    } catch (err) {
      showToast('error', 'Error loading administrative records');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAllAdminData();
  }, []);

  // Action handlers
  const handleToggleUser = async (userId: number) => {
    try {
      await adminService.toggleUserStatus(userId);
      showToast('success', 'User access status updated');
      loadAllAdminData();
    } catch (err) {
      showToast('error', 'Failed to toggle user status');
    }
  };

  const handleUpdateBookingStatus = async (bookingId: number, status: BookingStatus) => {
    try {
      await adminService.updateBookingStatus(bookingId, status);
      showToast('success', `Booking marked as ${status}`);
      loadAllAdminData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Status update failed');
    }
  };

  const handleCreateVehicle = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await adminService.createVehicle(vehicleForm);
      showToast('success', 'Vehicle registered to fleet successfully');
      setShowAddVehicleModal(false);
      loadAllAdminData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to add vehicle');
    }
  };

  const handleDeleteVehicle = async (id: number) => {
    if (!window.confirm('Are you sure you want to deactivate and remove this vehicle from the fleet?')) return;
    try {
      await adminService.deleteVehicle(id);
      showToast('success', 'Vehicle removed from fleet');
      loadAllAdminData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to delete vehicle');
    }
  };

  const handleCreateCoupon = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await adminService.createCoupon(couponForm);
      showToast('success', 'Promotional coupon code created');
      setShowCouponModal(false);
      loadAllAdminData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to create coupon');
    }
  };

  const handleDeleteCoupon = async (id: number) => {
    try {
      await adminService.deleteCoupon(id);
      showToast('success', 'Coupon deleted');
      loadAllAdminData();
    } catch (err) {
      showToast('error', 'Failed to delete coupon');
    }
  };

  const handleScheduleMaintenance = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await adminService.createMaintenanceRecord(maintenanceForm);
      showToast('success', 'Vehicle scheduled for service. Status updated to MAINTENANCE.');
      setShowMaintenanceModal(false);
      loadAllAdminData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to schedule maintenance');
    }
  };

  const handleCompleteMaintenance = async (id: number) => {
    try {
      await adminService.completeMaintenance(id, 'Inspection completed and vehicle released back to active fleet.');
      showToast('success', 'Maintenance completed. Vehicle is now AVAILABLE and alert subscribers notified.');
      loadAllAdminData();
    } catch (err) {
      showToast('error', 'Failed to complete maintenance');
    }
  };

  const handleToggleReview = async (id: number) => {
    try {
      await adminService.toggleReviewVisibility(id);
      showToast('success', 'Review visibility toggled');
      loadAllAdminData();
    } catch (err) {
      showToast('error', 'Failed to toggle review');
    }
  };

  return (
    <div className="bg-[#FAFAFA] min-h-screen py-10">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between pb-8 border-b border-gray-200">
          <div>
            <div className="flex items-center space-x-2">
              <span className="px-2 py-0.5 text-[11px] font-bold bg-black text-white rounded">ADMIN COMMAND</span>
              <h1 className="text-3xl font-black text-black tracking-tight">Fleet & Operations Console</h1>
            </div>
            <p className="text-sm text-gray-500 mt-1">
              Live fleet management, booking dispatch, payment audit, revenue metrics, and servicing logs.
            </p>
          </div>
          <div className="mt-4 md:mt-0 flex items-center space-x-3">
            <Button variant="outline" size="sm" onClick={loadAllAdminData}>
              <RefreshCw className="w-3.5 h-3.5 mr-1" /> Refresh
            </Button>
            <Button variant="primary" size="sm" onClick={() => setShowAddVehicleModal(true)}>
              <Plus className="w-3.5 h-3.5 mr-1" /> Add Vehicle
            </Button>
          </div>
        </div>

        {/* Navigation Tabs */}
        <div className="flex overflow-x-auto border-b border-gray-200 mt-6 space-x-8 scrollbar-none">
          {[
            { id: 'overview', label: 'Executive Metrics' },
            { id: 'vehicles', label: `Fleet Inventory (${vehicles.length})` },
            { id: 'bookings', label: `Reservations (${bookings.length})` },
            { id: 'users', label: `Customers (${users.length})` },
            { id: 'maintenance', label: `Servicing & Logs (${maintenance.length})` },
            { id: 'coupons', label: `Coupons (${coupons.length})` },
            { id: 'reviews', label: `Customer Reviews (${reviews.length})` },
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id as any)}
              className={`pb-4 px-1 text-sm font-semibold whitespace-nowrap transition-all border-b-2 ${
                activeTab === tab.id
                  ? 'border-black text-black'
                  : 'border-transparent text-gray-500 hover:text-black hover:border-gray-300'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Tab 1: Executive Metrics */}
        {activeTab === 'overview' && (
          <div className="mt-8 space-y-8">
            {/* KPI Cards */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
              <div className="bg-white p-6 border border-gray-200 rounded-xl shadow-sm">
                <div className="flex items-center justify-between text-gray-500">
                  <span className="text-xs font-bold uppercase tracking-wider">Gross Fleet Revenue</span>
                  <DollarSign className="w-4 h-4 text-black" />
                </div>
                <p className="text-3xl font-black text-black mt-2">
                  ₹{(stats?.totalRevenue || 0).toLocaleString()}
                </p>
                <p className="text-xs text-gray-500 mt-1">Paid bookings & extensions</p>
              </div>

              <div className="bg-white p-6 border border-gray-200 rounded-xl shadow-sm">
                <div className="flex items-center justify-between text-gray-500">
                  <span className="text-xs font-bold uppercase tracking-wider">Active Rentals</span>
                  <Car className="w-4 h-4 text-black" />
                </div>
                <p className="text-3xl font-black text-black mt-2">{stats?.activeRentals || 0}</p>
                <p className="text-xs text-gray-500 mt-1">Currently on-road</p>
              </div>

              <div className="bg-white p-6 border border-gray-200 rounded-xl shadow-sm">
                <div className="flex items-center justify-between text-gray-500">
                  <span className="text-xs font-bold uppercase tracking-wider">Total Vehicles</span>
                  <Shield className="w-4 h-4 text-black" />
                </div>
                <p className="text-3xl font-black text-black mt-2">{stats?.totalVehicles || 0}</p>
                <p className="text-xs text-gray-500 mt-1">{stats?.vehiclesUnderMaintenance || 0} in servicing</p>
              </div>

              <div className="bg-white p-6 border border-gray-200 rounded-xl shadow-sm">
                <div className="flex items-center justify-between text-gray-500">
                  <span className="text-xs font-bold uppercase tracking-wider">Registered Users</span>
                  <Users className="w-4 h-4 text-black" />
                </div>
                <p className="text-3xl font-black text-black mt-2">{stats?.totalUsers || 0}</p>
                <p className="text-xs text-gray-500 mt-1">Customers & administrators</p>
              </div>
            </div>

            {/* Analytics Breakdown */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
              {/* Category Distribution */}
              <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm">
                <h3 className="text-base font-bold text-black mb-4">Fleet Category Distribution</h3>
                <div className="space-y-4">
                  {(analytics?.categoryDistribution || []).map((item) => {
                    const percentage = stats?.totalVehicles ? Math.round((item.count / stats.totalVehicles) * 100) : 0;
                    return (
                      <div key={item.category}>
                        <div className="flex justify-between text-xs font-semibold mb-1">
                          <span className="text-black">{item.category}</span>
                          <span className="text-gray-500">
                            {item.count} vehicles ({percentage}%)
                          </span>
                        </div>
                        <div className="w-full bg-gray-100 rounded-full h-2 overflow-hidden">
                          <div
                            className="bg-black h-2 rounded-full transition-all duration-500"
                            style={{ width: `${percentage}%` }}
                          />
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* Monthly Revenue Projection */}
              <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm">
                <h3 className="text-base font-bold text-black mb-4">Monthly Revenue Inflow (Past 6 Months)</h3>
                <div className="space-y-3">
                  {analytics?.revenueByMonth?.map((item) => (
                    <div key={item.month} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                      <span className="text-xs font-bold text-black">{item.month}</span>
                      <span className="text-sm font-extrabold text-black">₹{item.revenue.toLocaleString()}</span>
                    </div>
                  ))}
                  {(!analytics?.revenueByMonth || analytics.revenueByMonth.length === 0) && (
                    <p className="text-xs text-gray-500 py-6 text-center">No monthly historical transactions yet.</p>
                  )}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Tab 2: Fleet Inventory */}
        {activeTab === 'vehicles' && (
          <div className="mt-8 bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-gray-200 flex flex-col sm:flex-row justify-between sm:items-center gap-4">
              <div>
                <h3 className="text-lg font-bold text-black">Fleet Inventory</h3>
                <p className="text-xs text-gray-500">Add, edit pricing, manage maintenance lockouts, or deactivate vehicles.</p>
              </div>
              <div className="flex space-x-3">
                <Button size="sm" variant="outline" onClick={() => setShowMaintenanceModal(true)}>
                  <Wrench className="w-3.5 h-3.5 mr-1" /> Service Vehicle
                </Button>
                <Button size="sm" variant="primary" onClick={() => setShowAddVehicleModal(true)}>
                  <Plus className="w-3.5 h-3.5 mr-1" /> Add New
                </Button>
              </div>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                    <th className="py-3 px-6">Vehicle</th>
                    <th className="py-3 px-6">Type</th>
                    <th className="py-3 px-6">Plate Number</th>
                    <th className="py-3 px-6">Daily Rate</th>
                    <th className="py-3 px-6">Deposit</th>
                    <th className="py-3 px-6">Status</th>
                    <th className="py-3 px-6 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {vehicles.length === 0 && (
                    <tr>
                      <td colSpan={7} className="py-12 text-center text-xs text-gray-500 font-medium">
                        No vehicles available in fleet. Click &quot;Add Vehicle&quot; above to register your first vehicle.
                      </td>
                    </tr>
                  )}
                  {vehicles.map((v) => (
                    <tr key={v.id} className="hover:bg-gray-50/60 transition">
                      <td className="py-4 px-6">
                        <div className="flex items-center space-x-3">
                          {v.primaryImageUrl ? (
                            <img
                              src={v.primaryImageUrl}
                              alt={v.model}
                              className="w-12 h-9 object-cover rounded border border-gray-200"
                            />
                          ) : (
                            <div className="w-12 h-9 bg-gray-100 rounded border border-gray-200 flex items-center justify-center text-[8px] font-bold text-gray-500 uppercase">
                              No Img
                            </div>
                          )}
                          <div>
                            <p className="font-bold text-black">
                              {v.brand} {v.model}
                            </p>
                            <p className="text-xs text-gray-500">
                              {v.year} • {v.location}
                            </p>
                          </div>
                        </div>
                      </td>
                      <td className="py-4 px-6 text-xs font-semibold text-gray-600">{v.vehicleType}</td>
                      <td className="py-4 px-6 font-mono text-xs text-black">{v.registrationNumber}</td>
                      <td className="py-4 px-6 font-black text-black">₹{v.pricePerDay}</td>
                      <td className="py-4 px-6 text-xs text-gray-500">₹{v.securityDeposit}</td>
                      <td className="py-4 px-6">
                        <Badge variant={v.status as any} />
                      </td>
                      <td className="py-4 px-6 text-right">
                        <div className="flex items-center justify-end space-x-2">
                          <button
                            onClick={() => {
                              setMaintenanceForm((prev) => ({ ...prev, vehicleId: v.id }));
                              setShowMaintenanceModal(true);
                            }}
                            title="Schedule Maintenance"
                            className="p-1.5 border border-gray-300 rounded hover:bg-black hover:text-white transition text-xs"
                          >
                            Service
                          </button>
                          <button
                            onClick={() => handleDeleteVehicle(v.id)}
                            title="Delete Vehicle"
                            className="p-1.5 border border-red-200 text-red-600 rounded hover:bg-red-50 transition text-xs"
                          >
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Tab 3: Reservations */}
        {activeTab === 'bookings' && (
          <div className="mt-8 bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-gray-200">
              <h3 className="text-lg font-bold text-black">Fleet Reservations & Dispatches</h3>
              <p className="text-xs text-gray-500">Monitor trips, activate vehicle handovers, and close completed rentals.</p>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                    <th className="py-3 px-6">Booking Number</th>
                    <th className="py-3 px-6">Customer</th>
                    <th className="py-3 px-6">Vehicle</th>
                    <th className="py-3 px-6">Dates</th>
                    <th className="py-3 px-6">Amount</th>
                    <th className="py-3 px-6">Status</th>
                    <th className="py-3 px-6 text-right">Dispatch Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {bookings.map((b) => (
                    <tr key={b.id} className="hover:bg-gray-50/60 transition">
                      <td className="py-4 px-6 font-mono text-xs font-bold text-black">
                        {b.bookingNumber || b.bookingReference}
                      </td>
                      <td className="py-4 px-6">
                        <p className="font-semibold text-black">{b.userFullName || b.userName || 'Customer'}</p>
                        <p className="text-xs text-gray-500">{b.userEmail}</p>
                      </td>
                      <td className="py-4 px-6">
                        <p className="font-semibold text-black">
                          {b.vehicleBrand} {b.vehicleModel}
                        </p>
                        <p className="text-xs text-gray-500">{b.vehicleRegistrationNumber}</p>
                      </td>
                      <td className="py-4 px-6 text-xs text-gray-600">
                        {new Date(b.pickupDate).toLocaleDateString()} → {new Date(b.returnDate).toLocaleDateString()}
                      </td>
                      <td className="py-4 px-6 font-black text-black">
                        ₹{(b.finalAmount ?? b.totalAmount ?? 0).toLocaleString()}
                      </td>
                      <td className="py-4 px-6">
                        <Badge variant={b.status as any} />
                      </td>
                      <td className="py-4 px-6 text-right">
                        <div className="flex items-center justify-end space-x-1.5">
                          {b.status === 'CONFIRMED' && (
                            <button
                              onClick={() => handleUpdateBookingStatus(b.id, 'ACTIVE')}
                              className="px-2.5 py-1 bg-black text-white rounded text-xs font-semibold hover:bg-gray-800 transition"
                            >
                              Dispatch (Active)
                            </button>
                          )}
                          {b.status === 'ACTIVE' && (
                            <button
                              onClick={() => handleUpdateBookingStatus(b.id, 'COMPLETED')}
                              className="px-2.5 py-1 bg-black text-white rounded text-xs font-semibold hover:bg-gray-800 transition"
                            >
                              Close (Completed)
                            </button>
                          )}
                          {b.status === 'PENDING' && (
                            <button
                              onClick={() => handleUpdateBookingStatus(b.id, 'CONFIRMED')}
                              className="px-2.5 py-1 border border-gray-300 rounded text-xs font-semibold hover:bg-black hover:text-white transition"
                            >
                              Confirm
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                  {bookings.length === 0 && (
                    <tr>
                      <td colSpan={7} className="py-12 text-center text-gray-500">
                        No bookings found.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Tab 4: Customers */}
        {activeTab === 'users' && (
          <div className="mt-8 bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-gray-200">
              <h3 className="text-lg font-bold text-black">Registered Customers & Administrators</h3>
              <p className="text-xs text-gray-500">Account profiles, security status, and lock/unlock toggles.</p>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                    <th className="py-3 px-6">Name</th>
                    <th className="py-3 px-6">Email</th>
                    <th className="py-3 px-6">Phone</th>
                    <th className="py-3 px-6">Roles</th>
                    <th className="py-3 px-6">Account Status</th>
                    <th className="py-3 px-6 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {users.map((u) => (
                    <tr key={u.id} className="hover:bg-gray-50/60 transition">
                      <td className="py-4 px-6 font-semibold text-black">{u.fullName}</td>
                      <td className="py-4 px-6 text-gray-600">{u.email}</td>
                      <td className="py-4 px-6 text-xs text-gray-600">{u.phone}</td>
                      <td className="py-4 px-6">
                        <div className="flex gap-1">
                          {u.roles.map((r) => (
                            <span key={r} className="px-2 py-0.5 text-[10px] font-bold bg-gray-100 rounded text-black">
                              {r}
                            </span>
                          ))}
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <span
                          className={`inline-flex px-2 py-0.5 rounded text-xs font-semibold ${
                            u.isActive ? 'bg-black text-white' : 'bg-gray-200 text-gray-700'
                          }`}
                        >
                          {u.isActive ? 'Active' : 'Disabled'}
                        </span>
                      </td>
                      <td className="py-4 px-6 text-right">
                        <button
                          onClick={() => handleToggleUser(u.id)}
                          className="px-2.5 py-1 border border-gray-300 rounded text-xs font-semibold hover:bg-black hover:text-white transition"
                        >
                          {u.isActive ? 'Suspend' : 'Reactivate'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Tab 5: Servicing & Logs */}
        {activeTab === 'maintenance' && (
          <div className="mt-8 bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-gray-200 flex justify-between items-center">
              <div>
                <h3 className="text-lg font-bold text-black">Fleet Maintenance Records</h3>
                <p className="text-xs text-gray-500">Service logs, oil changes, repairs, and availability releases.</p>
              </div>
              <Button size="sm" variant="primary" onClick={() => setShowMaintenanceModal(true)}>
                <Plus className="w-3.5 h-3.5 mr-1" /> Schedule Service
              </Button>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                    <th className="py-3 px-6">Vehicle</th>
                    <th className="py-3 px-6">Service Type</th>
                    <th className="py-3 px-6">Description</th>
                    <th className="py-3 px-6">Cost</th>
                    <th className="py-3 px-6">Status</th>
                    <th className="py-3 px-6 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {maintenance.map((m) => (
                    <tr key={m.id} className="hover:bg-gray-50/60 transition">
                      <td className="py-4 px-6 font-semibold text-black">
                        {m.vehicleBrand} {m.vehicleModel}
                        <span className="block font-mono text-xs text-gray-500">{m.vehicleRegistrationNumber}</span>
                      </td>
                      <td className="py-4 px-6 text-xs font-bold text-gray-700">{m.maintenanceType}</td>
                      <td className="py-4 px-6 text-xs text-gray-600 max-w-xs">{m.description}</td>
                      <td className="py-4 px-6 font-extrabold text-black">₹{m.cost?.toLocaleString() || '0'}</td>
                      <td className="py-4 px-6">
                        <span
                          className={`inline-block px-2 py-0.5 rounded text-xs font-semibold ${
                            m.status === 'COMPLETED' ? 'bg-gray-100 text-gray-800' : 'bg-black text-white'
                          }`}
                        >
                          {m.status}
                        </span>
                      </td>
                      <td className="py-4 px-6 text-right">
                        {m.status !== 'COMPLETED' && (
                          <button
                            onClick={() => handleCompleteMaintenance(m.id)}
                            className="px-2.5 py-1 bg-black text-white rounded text-xs font-semibold hover:bg-gray-800 transition"
                          >
                            Complete & Release
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                  {maintenance.length === 0 && (
                    <tr>
                      <td colSpan={6} className="py-12 text-center text-gray-500">
                        No maintenance records found.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Tab 6: Coupons */}
        {activeTab === 'coupons' && (
          <div className="mt-8 bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-gray-200 flex justify-between items-center">
              <div>
                <h3 className="text-lg font-bold text-black">Promotional Discount Coupons</h3>
                <p className="text-xs text-gray-500">Manage campaign codes, discount limits, and validity dates.</p>
              </div>
              <Button size="sm" variant="primary" onClick={() => setShowCouponModal(true)}>
                <Plus className="w-3.5 h-3.5 mr-1" /> Create Coupon
              </Button>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                    <th className="py-3 px-6">Code</th>
                    <th className="py-3 px-6">Discount</th>
                    <th className="py-3 px-6">Min Booking</th>
                    <th className="py-3 px-6">Max Discount</th>
                    <th className="py-3 px-6">Validity</th>
                    <th className="py-3 px-6">Status</th>
                    <th className="py-3 px-6 text-right">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {coupons.map((c) => (
                    <tr key={c.id} className="hover:bg-gray-50/60 transition">
                      <td className="py-4 px-6 font-mono font-bold text-black">{c.code}</td>
                      <td className="py-4 px-6 text-xs font-semibold">
                        {c.discountType === 'PERCENTAGE' ? `${c.discountValue}% OFF` : `₹${c.discountValue} OFF`}
                      </td>
                      <td className="py-4 px-6 text-xs text-gray-600">₹{c.minimumBookingAmount}</td>
                      <td className="py-4 px-6 text-xs text-gray-600">
                        {c.maximumDiscount ? `₹${c.maximumDiscount}` : 'None'}
                      </td>
                      <td className="py-4 px-6 text-xs text-gray-500">
                        {c.startDate} → {c.expiryDate}
                      </td>
                      <td className="py-4 px-6">
                        <span
                          className={`inline-block px-2 py-0.5 rounded text-xs font-semibold ${
                            c.active ? 'bg-black text-white' : 'bg-gray-200 text-gray-700'
                          }`}
                        >
                          {c.active ? 'Active' : 'Disabled'}
                        </span>
                      </td>
                      <td className="py-4 px-6 text-right">
                        <button
                          onClick={() => handleDeleteCoupon(c.id)}
                          className="p-1.5 border border-red-200 text-red-600 rounded hover:bg-red-50 transition text-xs"
                          title="Delete Coupon"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Tab 7: Customer Reviews */}
        {activeTab === 'reviews' && (
          <div className="mt-8 bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-gray-200">
              <h3 className="text-lg font-bold text-black">Customer Reviews Moderation</h3>
              <p className="text-xs text-gray-500">Review verified renter feedback and manage content visibility.</p>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                    <th className="py-3 px-6">Customer</th>
                    <th className="py-3 px-6">Vehicle</th>
                    <th className="py-3 px-6">Rating</th>
                    <th className="py-3 px-6">Comment</th>
                    <th className="py-3 px-6">Visibility</th>
                    <th className="py-3 px-6 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {reviews.map((r) => (
                    <tr key={r.id} className="hover:bg-gray-50/60 transition">
                      <td className="py-4 px-6 font-semibold text-black">{r.userName}</td>
                      <td className="py-4 px-6 text-xs text-gray-600">Vehicle #{r.vehicleId}</td>
                      <td className="py-4 px-6">
                        <div className="flex items-center text-black font-bold text-xs">
                          <Star className="w-3.5 h-3.5 fill-black mr-1" /> {r.rating} / 5
                        </div>
                      </td>
                      <td className="py-4 px-6 text-xs text-gray-600 max-w-sm">{r.comment}</td>
                      <td className="py-4 px-6">
                        <span
                          className={`inline-block px-2 py-0.5 rounded text-xs font-semibold ${
                            !r.isHidden ? 'bg-black text-white' : 'bg-gray-200 text-gray-700'
                          }`}
                        >
                          {!r.isHidden ? 'Public' : 'Hidden'}
                        </span>
                      </td>
                      <td className="py-4 px-6 text-right">
                        <button
                          onClick={() => handleToggleReview(r.id)}
                          className="px-2.5 py-1 border border-gray-300 rounded text-xs font-semibold hover:bg-black hover:text-white transition"
                        >
                          {!r.isHidden ? 'Hide' : 'Show'}
                        </button>
                      </td>
                    </tr>
                  ))}
                  {reviews.length === 0 && (
                    <tr>
                      <td colSpan={6} className="py-12 text-center text-gray-500">
                        No reviews submitted yet.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}

      </div>

      {/* ADD VEHICLE MODAL */}
      {showAddVehicleModal && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 overflow-y-auto">
          <div className="bg-white rounded-xl max-w-2xl w-full p-6 shadow-2xl border border-gray-200 my-8">
            <h3 className="text-xl font-bold text-black">Register New Fleet Vehicle</h3>
            <p className="text-xs text-gray-500 mt-1">Add specs, daily rental tariffs, and registration details.</p>

            <form onSubmit={handleCreateVehicle} className="mt-6 space-y-4">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Brand
                  </label>
                  <input
                    type="text"
                    required
                    value={vehicleForm.brand}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, brand: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Model
                  </label>
                  <input
                    type="text"
                    required
                    value={vehicleForm.model}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, model: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Category
                  </label>
                  <select
                    value={vehicleForm.vehicleType}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, vehicleType: e.target.value as any })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none bg-white"
                  >
                    <option value="CAR">CAR</option>
                    <option value="SEDAN">SEDAN</option>
                    <option value="SUV">SUV</option>
                    <option value="LUXURY">LUXURY</option>
                    <option value="BIKE">BIKE</option>
                    <option value="HATCHBACK">HATCHBACK</option>
                    <option value="VAN">VAN</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Plate Number
                  </label>
                  <input
                    type="text"
                    required
                    value={vehicleForm.registrationNumber}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, registrationNumber: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none uppercase font-mono"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Model Year
                  </label>
                  <input
                    type="number"
                    value={vehicleForm.year}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, year: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Daily Rental Rate (₹)
                  </label>
                  <input
                    type="number"
                    required
                    value={vehicleForm.pricePerDay}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, pricePerDay: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Security Deposit (₹)
                  </label>
                  <input
                    type="number"
                    required
                    value={vehicleForm.securityDeposit}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, securityDeposit: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Fuel
                  </label>
                  <input
                    type="text"
                    value={vehicleForm.fuelType}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, fuelType: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Transmission
                  </label>
                  <input
                    type="text"
                    value={vehicleForm.transmission}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, transmission: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Location
                  </label>
                  <input
                    type="text"
                    value={vehicleForm.location}
                    onChange={(e) => setVehicleForm({ ...vehicleForm, location: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Description
                </label>
                <textarea
                  rows={2}
                  value={vehicleForm.description}
                  onChange={(e) => setVehicleForm({ ...vehicleForm, description: e.target.value })}
                  className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Primary Image URL
                </label>
                <input
                  type="url"
                  value={vehicleForm.imageUrls?.[0] || ''}
                  onChange={(e) => setVehicleForm({ ...vehicleForm, imageUrls: [e.target.value] })}
                  className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>

              <div className="flex items-center space-x-2 pt-2">
                <input
                  type="checkbox"
                  id="featuredCheck"
                  checked={vehicleForm.featured}
                  onChange={(e) => setVehicleForm({ ...vehicleForm, featured: e.target.checked })}
                  className="w-4 h-4 text-black focus:ring-black border-gray-300 rounded"
                />
                <label htmlFor="featuredCheck" className="text-xs font-semibold text-black cursor-pointer">
                  Feature this vehicle on public home hero carousel
                </label>
              </div>

              <div className="mt-6 flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <Button variant="outline" type="button" onClick={() => setShowAddVehicleModal(false)}>
                  Cancel
                </Button>
                <Button variant="primary" type="submit">
                  Save Vehicle
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* CREATE COUPON MODAL */}
      {showCouponModal && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6 shadow-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-black">Create Promotional Coupon</h3>
            <form onSubmit={handleCreateCoupon} className="mt-4 space-y-4">
              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Coupon Code
                </label>
                <input
                  type="text"
                  required
                  value={couponForm.code}
                  onChange={(e) => setCouponForm({ ...couponForm, code: e.target.value.toUpperCase() })}
                  className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none font-mono uppercase"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Discount Type
                  </label>
                  <select
                    value={couponForm.discountType}
                    onChange={(e) => setCouponForm({ ...couponForm, discountType: e.target.value as any })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none bg-white"
                  >
                    <option value="PERCENTAGE">PERCENTAGE (%)</option>
                    <option value="FIXED">FIXED (₹)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Value
                  </label>
                  <input
                    type="number"
                    required
                    value={couponForm.discountValue}
                    onChange={(e) => setCouponForm({ ...couponForm, discountValue: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Min Booking (₹)
                  </label>
                  <input
                    type="number"
                    value={couponForm.minimumBookingAmount}
                    onChange={(e) => setCouponForm({ ...couponForm, minimumBookingAmount: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Max Discount (₹)
                  </label>
                  <input
                    type="number"
                    value={couponForm.maximumDiscount}
                    onChange={(e) => setCouponForm({ ...couponForm, maximumDiscount: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Start Date
                  </label>
                  <input
                    type="date"
                    value={couponForm.startDate}
                    onChange={(e) => setCouponForm({ ...couponForm, startDate: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Expiry Date
                  </label>
                  <input
                    type="date"
                    value={couponForm.expiryDate}
                    onChange={(e) => setCouponForm({ ...couponForm, expiryDate: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div className="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <Button variant="outline" type="button" onClick={() => setShowCouponModal(false)}>
                  Cancel
                </Button>
                <Button variant="primary" type="submit">
                  Create Coupon
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* SCHEDULE MAINTENANCE MODAL */}
      {showMaintenanceModal && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6 shadow-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-black">Schedule Fleet Servicing</h3>
            <p className="text-xs text-gray-500 mt-1">
              Vehicle will be set to MAINTENANCE and prevented from receiving customer bookings.
            </p>

            <form onSubmit={handleScheduleMaintenance} className="mt-4 space-y-4">
              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Vehicle
                </label>
                <select
                  value={maintenanceForm.vehicleId}
                  onChange={(e) => setMaintenanceForm({ ...maintenanceForm, vehicleId: Number(e.target.value) })}
                  className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none bg-white"
                >
                  {vehicles.map((v) => (
                    <option key={v.id} value={v.id}>
                      {v.brand} {v.model} ({v.registrationNumber})
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Service Type
                  </label>
                  <select
                    value={maintenanceForm.maintenanceType}
                    onChange={(e) => setMaintenanceForm({ ...maintenanceForm, maintenanceType: e.target.value })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none bg-white"
                  >
                    <option value="SERVICE">Periodic Service</option>
                    <option value="OIL_CHANGE">Oil Change</option>
                    <option value="TYRE_CHANGE">Tyres & Alignment</option>
                    <option value="INSPECTION">Safety Inspection</option>
                    <option value="REPAIR">Mechanical Repair</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                    Cost (₹)
                  </label>
                  <input
                    type="number"
                    value={maintenanceForm.cost}
                    onChange={(e) => setMaintenanceForm({ ...maintenanceForm, cost: Number(e.target.value) })}
                    className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Service Notes / Reason
                </label>
                <textarea
                  rows={2}
                  required
                  placeholder="Brake pad renewal and seasonal general checkup..."
                  value={maintenanceForm.description}
                  onChange={(e) => setMaintenanceForm({ ...maintenanceForm, description: e.target.value })}
                  className="w-full p-2 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>

              <div className="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <Button variant="outline" type="button" onClick={() => setShowMaintenanceModal(false)}>
                  Cancel
                </Button>
                <Button variant="primary" type="submit">
                  Confirm Servicing
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};

export default AdminDashboard;
