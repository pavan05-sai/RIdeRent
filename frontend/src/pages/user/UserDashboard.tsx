import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { 
  Calendar, Clock, ShieldCheck, Tag, CreditCard, CheckCircle, 
  MapPin, AlertCircle, FileText, ChevronRight, X, Heart, Star, Bell
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useToast } from '../../context/ToastContext';
import { useWishlist } from '../../context/WishlistContext';
import { bookingService } from '../../services/bookingService';
import { couponService } from '../../services/couponService';
import { notificationService } from '../../services/notificationService';
import { Booking, BookingQuote, Vehicle } from '../../types';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import { NoImagePlaceholder } from '../../components/common/NoImagePlaceholder';

interface UserDashboardProps {
  initialTab?: 'overview' | 'bookings' | 'active' | 'wishlist' | 'profile' | 'notifications';
}

export const UserDashboard: React.FC<UserDashboardProps> = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const activeTab = searchParams.get('tab') || 'overview';
  const { user, updateProfile } = useAuth();
  const { showToast } = useToast();
  const { wishlist, removeFromWishlist } = useWishlist();

  // Data states
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [activeRental, setActiveRental] = useState<Booking | null>(null);
  const [notifications, setNotifications] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  // Modal states
  const [selectedBooking, setSelectedBooking] = useState<Booking | null>(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [cancelReason, setCancelReason] = useState('');
  const [cancelLoading, setCancelLoading] = useState(false);

  const [showExtendModal, setShowExtendModal] = useState(false);
  const [extendDate, setExtendDate] = useState('');
  const [extendLoading, setExtendLoading] = useState(false);

  const [showModifyModal, setShowModifyModal] = useState(false);
  const [modifyData, setModifyData] = useState({ pickupDate: '', returnDate: '', pickupLocation: '', returnLocation: '' });
  const [modifyLoading, setModifyLoading] = useState(false);

  // Profile edit state
  const [profileForm, setProfileForm] = useState({
    fullName: user?.fullName || '',
    phone: user?.phone || '',
    profilePhoto: user?.profilePhoto || '',
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [profileSaving, setProfileSaving] = useState(false);

  const setTab = (tabName: string) => {
    setSearchParams({ tab: tabName });
  };

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const [bookingsRes, activeRes, notifRes] = await Promise.allSettled([
        bookingService.getMyBookings(0, 50),
        bookingService.getActiveRental(),
        notificationService.getNotifications(0, 50),
      ]);

      if (bookingsRes.status === 'fulfilled') {
        setBookings(bookingsRes.value.content || []);
      }
      if (activeRes.status === 'fulfilled') {
        setActiveRental(activeRes.value);
      }
      if (notifRes.status === 'fulfilled') {
        setNotifications(notifRes.value.content || []);
      }
    } catch (err: any) {
      showToast('error', 'Failed to refresh dashboard data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboardData();
  }, []);

  // Action Handlers
  const handleCancelBooking = async () => {
    if (!selectedBooking || !cancelReason.trim()) {
      showToast('error', 'Please enter a cancellation reason');
      return;
    }
    setCancelLoading(true);
    try {
      await bookingService.cancelBooking(selectedBooking.id, cancelReason);
      showToast('success', 'Booking cancelled successfully. Refund initiated.');
      setShowCancelModal(false);
      setCancelReason('');
      setSelectedBooking(null);
      loadDashboardData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to cancel booking');
    } finally {
      setCancelLoading(false);
    }
  };

  const handleExtendBooking = async () => {
    if (!selectedBooking || !extendDate) {
      showToast('error', 'Please select a new return date');
      return;
    }
    setExtendLoading(true);
    try {
      await bookingService.extendBooking(selectedBooking.id, extendDate);
      showToast('success', 'Rental extended successfully!');
      setShowExtendModal(false);
      setExtendDate('');
      setSelectedBooking(null);
      loadDashboardData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to extend rental');
    } finally {
      setExtendLoading(false);
    }
  };

  const handleModifyBooking = async () => {
    if (!selectedBooking || !modifyData.pickupDate || !modifyData.returnDate) {
      showToast('error', 'Please fill in required dates');
      return;
    }
    setModifyLoading(true);
    try {
      await bookingService.modifyBooking(selectedBooking.id, modifyData);
      showToast('success', 'Booking updated successfully!');
      setShowModifyModal(false);
      setSelectedBooking(null);
      loadDashboardData();
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to modify booking');
    } finally {
      setModifyLoading(false);
    }
  };

  const handleDownloadInvoice = async (id: number, bookingNumber: string) => {
    try {
      const blob = await bookingService.downloadInvoicePdf(id);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `Invoice-${bookingNumber}.pdf`;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      showToast('error', 'Failed to generate invoice PDF');
    }
  };

  const handleDownloadAgreement = async (id: number, bookingNumber: string) => {
    try {
      const blob = await bookingService.downloadAgreementPdf(id);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `RentalAgreement-${bookingNumber}.pdf`;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      showToast('error', 'Failed to generate rental agreement PDF');
    }
  };

  const handleProfileSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (profileForm.newPassword && profileForm.newPassword !== profileForm.confirmPassword) {
      showToast('error', 'New passwords do not match');
      return;
    }
    setProfileSaving(true);
    try {
      await updateProfile({
        fullName: profileForm.fullName,
        phone: profileForm.phone,
        profilePhoto: profileForm.profilePhoto,
        currentPassword: profileForm.currentPassword || undefined,
        newPassword: profileForm.newPassword || undefined,
      });
      showToast('success', 'Profile updated successfully');
      setProfileForm((prev) => ({ ...prev, currentPassword: '', newPassword: '', confirmPassword: '' }));
    } catch (err: any) {
      showToast('error', err.response?.data?.message || 'Failed to update profile');
    } finally {
      setProfileSaving(false);
    }
  };

  const markAllNotificationsRead = async () => {
    try {
      await notificationService.markAllAsRead();
      setNotifications((prev) => prev.map((n) => ({ ...n, isRead: true })));
      showToast('success', 'All notifications marked as read');
    } catch (err) {
      showToast('error', 'Failed to update notifications');
    }
  };

  const unreadNotifCount = notifications.filter((n) => !n.isRead).length;

  return (
    <div className="bg-[#FAFAFA] min-h-screen py-10">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between pb-8 border-b border-gray-200">
          <div>
            <h1 className="text-3xl font-extrabold text-black tracking-tight">Customer Portal</h1>
            <p className="text-sm text-gray-500 mt-1">
              Welcome back, <span className="font-semibold text-black">{user?.fullName}</span>. Manage your fleet bookings and rentals.
            </p>
          </div>
          <div className="mt-4 md:mt-0 flex items-center space-x-3">
            <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-black text-white">
              Verified Member
            </span>
            <span className="text-xs text-gray-500">{user?.email}</span>
          </div>
        </div>

        {/* Navigation Tabs */}
        <div className="flex overflow-x-auto border-b border-gray-200 mt-6 space-x-8 scrollbar-none">
          {[
            { id: 'overview', label: 'Overview' },
            { id: 'bookings', label: `My Bookings (${bookings.length})` },
            { id: 'active', label: 'Active Rental' },
            { id: 'wishlist', label: `Wishlist (${wishlist.length})` },
            { id: 'notifications', label: `Notifications ${unreadNotifCount > 0 ? `(${unreadNotifCount})` : ''}` },
            { id: 'profile', label: 'Profile Settings' },
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setTab(tab.id)}
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

        {/* Tab Content */}
        <div className="mt-8">
          
          {/* TAB 1: OVERVIEW */}
          {activeTab === 'overview' && (
            <div className="space-y-8">
              {/* Quick Stat Cards */}
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                <div className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm">
                  <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">Active Rental</p>
                  <p className="text-2xl font-black text-black mt-2">
                    {activeRental ? activeRental.vehicleBrand + ' ' + activeRental.vehicleModel : 'None'}
                  </p>
                  <p className="text-xs text-gray-500 mt-1">
                    {activeRental ? `Due back: ${activeRental.returnDate.split('T')[0]}` : 'No ongoing trip'}
                  </p>
                </div>

                <div className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm">
                  <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">Upcoming Trips</p>
                  <p className="text-2xl font-black text-black mt-2">
                    {bookings.filter((b) => b.status === 'CONFIRMED' || b.status === 'PENDING').length}
                  </p>
                  <p className="text-xs text-gray-500 mt-1">Confirmed reservations</p>
                </div>

                <div className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm">
                  <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">Total Bookings</p>
                  <p className="text-2xl font-black text-black mt-2">{bookings.length}</p>
                  <p className="text-xs text-gray-500 mt-1">All-time rentals</p>
                </div>

                <div className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm">
                  <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">Saved Vehicles</p>
                  <p className="text-2xl font-black text-black mt-2">{wishlist.length}</p>
                  <p className="text-xs text-gray-500 mt-1">In your wishlist</p>
                </div>
              </div>

              {/* Active Rental Spotlight if exists */}
              {activeRental && (
                <div className="bg-black text-white p-6 rounded-xl shadow-lg border border-black flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
                  <div>
                    <span className="inline-block bg-white text-black text-xs font-bold px-2.5 py-0.5 rounded-full uppercase tracking-wider">
                      Current On-Road Rental
                    </span>
                    <h3 className="text-2xl font-extrabold mt-3">
                      {activeRental.vehicleBrand} {activeRental.vehicleModel}
                    </h3>
                    <p className="text-sm text-gray-300 mt-1">
                      Registration: <span className="font-mono">{activeRental.vehicleRegistrationNumber}</span> • Return Due:{' '}
                      <span className="font-semibold text-white">{new Date(activeRental.returnDate).toLocaleString()}</span>
                    </p>
                    <p className="text-xs text-gray-400 mt-1">
                      Drop-off Location: {activeRental.returnLocation}
                    </p>
                  </div>
                  <div className="flex flex-wrap gap-3">
                    <Button
                      variant="outline"
                      className="border-white text-white hover:bg-white hover:text-black"
                      onClick={() => {
                        setSelectedBooking(activeRental);
                        setShowExtendModal(true);
                      }}
                    >
                      Extend Trip
                    </Button>
                    <Button
                      variant="primary"
                      className="bg-white text-black hover:bg-gray-200"
                      onClick={() => handleDownloadAgreement(activeRental.id, activeRental.bookingNumber)}
                    >
                      Digital Agreement
                    </Button>
                  </div>
                </div>
              )}

              {/* Recent Bookings Snapshot */}
              <div className="bg-white border border-gray-200 rounded-xl overflow-hidden shadow-sm">
                <div className="p-6 border-b border-gray-200 flex items-center justify-between">
                  <h3 className="text-lg font-bold text-black">Recent Bookings</h3>
                  <button
                    onClick={() => setTab('bookings')}
                    className="text-xs font-semibold text-black hover:underline flex items-center"
                  >
                    View all <ChevronRight className="w-3.5 h-3.5 ml-1" />
                  </button>
                </div>
                <div className="divide-y divide-gray-100">
                  {bookings.slice(0, 3).map((b) => (
                    <div key={b.id} className="p-6 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                      <div>
                        <div className="flex items-center space-x-3">
                          <span className="text-sm font-bold text-black">
                            {b.vehicleBrand} {b.vehicleModel}
                          </span>
                          <Badge variant={b.status as any} />
                        </div>
                        <p className="text-xs text-gray-500 mt-1">
                          Booking #{b.bookingNumber} • {new Date(b.pickupDate).toLocaleDateString()} to{' '}
                          {new Date(b.returnDate).toLocaleDateString()}
                        </p>
                      </div>
                      <div className="flex items-center space-x-4">
                        <span className="text-base font-extrabold text-black">
                          ₹{(b.finalAmount ?? b.totalAmount ?? 0).toLocaleString()}
                        </span>
                        <Button
                          size="sm"
                          variant="outline"
                          onClick={() => handleDownloadInvoice(b.id, b.bookingNumber || b.bookingReference)}
                        >
                          Invoice PDF
                        </Button>
                      </div>
                    </div>
                  ))}
                  {bookings.length === 0 && (
                    <div className="p-8 text-center text-gray-500 text-sm flex flex-col items-center justify-center space-y-3">
                      <p>No bookings yet. Browse our available fleet to start your trip.</p>
                      <Link to="/vehicles">
                        <Button size="sm" variant="primary">
                          Explore Available Vehicles
                        </Button>
                      </Link>
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}

          {/* TAB 2: MY BOOKINGS */}
          {activeTab === 'bookings' && (
            <div className="bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
              <div className="p-6 border-b border-gray-200 flex justify-between items-center">
                <div>
                  <h3 className="text-lg font-bold text-black">All Reservations & Rentals</h3>
                  <p className="text-xs text-gray-500">Manage dates, modifications, cancellations, and invoices.</p>
                </div>
                <span className="text-xs font-semibold text-gray-500">{bookings.length} Total Bookings</span>
              </div>

              <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse text-sm">
                  <thead>
                    <tr className="bg-gray-50 text-gray-500 text-xs uppercase tracking-wider border-b border-gray-200">
                      <th className="py-3 px-6">Booking ID</th>
                      <th className="py-3 px-6">Vehicle</th>
                      <th className="py-3 px-6">Dates</th>
                      <th className="py-3 px-6">Total Amount</th>
                      <th className="py-3 px-6">Status</th>
                      <th className="py-3 px-6 text-right">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-100">
                    {bookings.map((booking) => {
                      const canCancel = ['PENDING', 'CONFIRMED'].includes(booking.status);
                      const canModify = ['PENDING', 'CONFIRMED'].includes(booking.status);
                      const canExtend = ['ACTIVE', 'CONFIRMED'].includes(booking.status);

                      return (
                        <tr key={booking.id} className="hover:bg-gray-50/60 transition">
                          <td className="py-4 px-6 font-mono text-xs font-bold text-black">
                            {booking.bookingNumber}
                          </td>
                          <td className="py-4 px-6">
                            <p className="font-semibold text-black">
                              {booking.vehicleBrand} {booking.vehicleModel}
                            </p>
                            <p className="text-xs text-gray-500">{booking.vehicleRegistrationNumber}</p>
                          </td>
                          <td className="py-4 px-6 text-xs text-gray-600">
                            <div>
                              <span className="font-semibold text-black">Pickup:</span>{' '}
                              {new Date(booking.pickupDate).toLocaleDateString()}
                            </div>
                            <div>
                              <span className="font-semibold text-black">Return:</span>{' '}
                              {new Date(booking.returnDate).toLocaleDateString()}
                            </div>
                          </td>
                          <td className="py-4 px-6">
                            <span className="font-extrabold text-black">
                              ₹{(booking.finalAmount ?? booking.totalAmount ?? 0).toLocaleString()}
                            </span>
                            {booking.securityDeposit > 0 && (
                              <p className="text-[11px] text-gray-500">Deposit: ₹{booking.securityDeposit}</p>
                            )}
                          </td>
                          <td className="py-4 px-6">
                            <Badge variant={booking.status as any} />
                          </td>
                          <td className="py-4 px-6 text-right">
                            <div className="flex items-center justify-end space-x-2">
                              {/* Invoice PDF */}
                              <button
                                onClick={() =>
                                  handleDownloadInvoice(
                                    booking.id,
                                    booking.bookingNumber || booking.bookingReference
                                  )
                                }
                                title="Download Invoice"
                                className="p-1.5 border border-gray-300 rounded hover:bg-black hover:text-white transition text-xs"
                              >
                                Invoice
                              </button>

                              {/* Rental Agreement */}
                              <button
                                onClick={() =>
                                  handleDownloadAgreement(
                                    booking.id,
                                    booking.bookingNumber || booking.bookingReference
                                  )
                                }
                                title="Download Agreement"
                                className="p-1.5 border border-gray-300 rounded hover:bg-black hover:text-white transition text-xs"
                              >
                                Agreement
                              </button>

                              {/* Modify */}
                              {canModify && (
                                <button
                                  onClick={() => {
                                    setSelectedBooking(booking);
                                    setModifyData({
                                      pickupDate: booking.pickupDate.slice(0, 16),
                                      returnDate: booking.returnDate.slice(0, 16),
                                      pickupLocation: booking.pickupLocation,
                                      returnLocation: booking.returnLocation,
                                    });
                                    setShowModifyModal(true);
                                  }}
                                  className="p-1.5 border border-gray-300 rounded hover:bg-black hover:text-white transition text-xs font-semibold"
                                >
                                  Modify
                                </button>
                              )}

                              {/* Extend */}
                              {canExtend && (
                                <button
                                  onClick={() => {
                                    setSelectedBooking(booking);
                                    setExtendDate(booking.returnDate.slice(0, 16));
                                    setShowExtendModal(true);
                                  }}
                                  className="p-1.5 border border-black bg-black text-white rounded hover:bg-gray-800 transition text-xs font-semibold"
                                >
                                  Extend
                                </button>
                              )}

                              {/* Cancel */}
                              {canCancel && (
                                <button
                                  onClick={() => {
                                    setSelectedBooking(booking);
                                    setShowCancelModal(true);
                                  }}
                                  className="p-1.5 border border-red-200 text-red-600 rounded hover:bg-red-50 transition text-xs font-semibold"
                                >
                                  Cancel
                                </button>
                              )}
                            </div>
                          </td>
                        </tr>
                      );
                    })}
                    {bookings.length === 0 && (
                      <tr>
                        <td colSpan={6} className="py-12 text-center text-gray-500">
                          <p className="mb-3">No reservations found yet.</p>
                          <Link to="/vehicles">
                            <Button size="sm" variant="primary">
                              Browse All Vehicles
                            </Button>
                          </Link>
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 3: ACTIVE RENTAL */}
          {activeTab === 'active' && (
            <div>
              {activeRental ? (
                <div className="bg-white border border-gray-200 rounded-xl p-8 shadow-sm">
                  <div className="flex flex-col md:flex-row md:items-center justify-between pb-6 border-b border-gray-200 gap-4">
                    <div>
                      <span className="inline-block bg-black text-white text-xs font-bold px-3 py-1 rounded-full uppercase">
                        Current On-Road Journey
                      </span>
                      <h2 className="text-3xl font-black text-black mt-3">
                        {activeRental.vehicleBrand} {activeRental.vehicleModel}
                      </h2>
                      <p className="text-sm text-gray-500 font-mono mt-1">
                        Reg: {activeRental.vehicleRegistrationNumber} • Booking: #{activeRental.bookingNumber}
                      </p>
                    </div>
                    <div className="text-right">
                      <span className="text-xs text-gray-400 uppercase tracking-widest block">Total Charges</span>
                      <span className="text-3xl font-black text-black">
                        ₹{(activeRental.finalAmount ?? activeRental.totalAmount ?? 0).toLocaleString()}
                      </span>
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                    <div className="p-5 border border-gray-200 rounded-lg">
                      <h4 className="text-xs font-bold text-gray-500 uppercase tracking-wider mb-3">Pickup Information</h4>
                      <p className="text-sm font-semibold text-black">
                        {new Date(activeRental.pickupDate).toLocaleString()}
                      </p>
                      <p className="text-xs text-gray-500 mt-1 flex items-center">
                        <MapPin className="w-3.5 h-3.5 mr-1" /> {activeRental.pickupLocation}
                      </p>
                    </div>

                    <div className="p-5 border border-gray-200 rounded-lg">
                      <h4 className="text-xs font-bold text-gray-500 uppercase tracking-wider mb-3">Return Information</h4>
                      <p className="text-sm font-semibold text-black">
                        {new Date(activeRental.returnDate).toLocaleString()}
                      </p>
                      <p className="text-xs text-gray-500 mt-1 flex items-center">
                        <MapPin className="w-3.5 h-3.5 mr-1" /> {activeRental.returnLocation}
                      </p>
                    </div>
                  </div>

                  <div className="mt-8 pt-6 border-t border-gray-200 flex flex-wrap gap-4">
                    <Button
                      variant="primary"
                      onClick={() => {
                        setSelectedBooking(activeRental);
                        setExtendDate(activeRental.returnDate.slice(0, 16));
                        setShowExtendModal(true);
                      }}
                    >
                      Extend Rental Return Date
                    </Button>
                    <Button
                      variant="outline"
                      onClick={() => handleDownloadAgreement(activeRental.id, activeRental.bookingNumber)}
                    >
                      Download Rental Agreement
                    </Button>
                    <Button
                      variant="outline"
                      onClick={() => handleDownloadInvoice(activeRental.id, activeRental.bookingNumber)}
                    >
                      Download Invoice PDF
                    </Button>
                  </div>
                </div>
              ) : (
                <div className="bg-white border border-gray-200 rounded-xl p-12 text-center shadow-sm">
                  <h3 className="text-lg font-bold text-black">No Active Rental</h3>
                  <p className="text-sm text-gray-500 mt-1">You do not currently have any vehicles on the road.</p>
                  <Button variant="primary" className="mt-5" onClick={() => (window.location.href = '/vehicles')}>
                    Explore Vehicles
                  </Button>
                </div>
              )}
            </div>
          )}

          {/* TAB 4: WISHLIST */}
          {activeTab === 'wishlist' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <h3 className="text-lg font-bold text-black">Saved Vehicles ({wishlist.length})</h3>
                <p className="text-xs text-gray-500">Easily book your favorite fleet vehicles.</p>
              </div>

              {wishlist.length > 0 ? (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                  {wishlist.map((item) => (
                    <div
                      key={item.id}
                      className="bg-white border border-gray-200 rounded-xl overflow-hidden shadow-sm hover:shadow-md transition flex flex-col justify-between"
                    >
                      <div>
                        <div className="relative h-48 bg-gray-100 flex items-center justify-center overflow-hidden">
                          {item.primaryImageUrl ? (
                            <img
                              src={item.primaryImageUrl}
                              alt={item.model}
                              className="w-full h-full object-cover"
                            />
                          ) : (
                            <NoImagePlaceholder vehicleType={item.vehicleType} />
                          )}
                          <button
                            onClick={() => removeFromWishlist(item.id)}
                            className="absolute top-3 right-3 p-2 bg-white/90 rounded-full hover:bg-black hover:text-white transition"
                            title="Remove from wishlist"
                          >
                            <X className="w-4 h-4" />
                          </button>
                        </div>
                        <div className="p-5">
                          <span className="text-[11px] font-bold text-gray-500 uppercase tracking-wider">
                            {item.vehicleType}
                          </span>
                          <h4 className="text-base font-bold text-black mt-1">
                            {item.brand} {item.model}
                          </h4>
                          <p className="text-xs text-gray-500 mt-1">{item.location}</p>
                          <div className="mt-4 flex items-baseline justify-between">
                            <span className="text-lg font-black text-black">₹{item.pricePerDay}</span>
                            <span className="text-xs text-gray-500">/ day</span>
                          </div>
                        </div>
                      </div>
                      <div className="p-5 pt-0">
                        <Button
                          variant="primary"
                          className="w-full"
                          onClick={() => (window.location.href = `/vehicles/${item.id}`)}
                        >
                          Book Now
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="bg-white border border-gray-200 rounded-xl p-12 text-center shadow-sm">
                  <Heart className="w-12 h-12 text-gray-300 mx-auto mb-3" />
                  <h3 className="text-base font-bold text-black">Your wishlist is empty</h3>
                  <p className="text-xs text-gray-500 mt-1">Save vehicles to quickly access and rent them later.</p>
                  <Button variant="primary" className="mt-4" onClick={() => (window.location.href = '/vehicles')}>
                    Browse Fleet
                  </Button>
                </div>
              )}
            </div>
          )}

          {/* TAB 5: NOTIFICATIONS */}
          {activeTab === 'notifications' && (
            <div className="bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
              <div className="p-6 border-b border-gray-200 flex justify-between items-center">
                <div>
                  <h3 className="text-lg font-bold text-black">Notifications</h3>
                  <p className="text-xs text-gray-500">Real-time status updates on rentals, alerts, and billing.</p>
                </div>
                {unreadNotifCount > 0 && (
                  <Button variant="outline" size="sm" onClick={markAllNotificationsRead}>
                    Mark All as Read
                  </Button>
                )}
              </div>

              <div className="divide-y divide-gray-100">
                {notifications.map((notif) => (
                  <div
                    key={notif.id}
                    className={`p-6 flex items-start space-x-4 transition ${
                      notif.isRead ? 'bg-white' : 'bg-gray-50/70'
                    }`}
                  >
                    <div className="p-2 bg-black text-white rounded-full mt-0.5">
                      <Bell className="w-4 h-4" />
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center justify-between">
                        <h4 className="text-sm font-bold text-black">{notif.title}</h4>
                        <span className="text-xs text-gray-400">
                          {new Date(notif.createdAt).toLocaleDateString()}
                        </span>
                      </div>
                      <p className="text-xs text-gray-600 mt-1">{notif.message}</p>
                    </div>
                  </div>
                ))}
                {notifications.length === 0 && (
                  <div className="p-12 text-center text-gray-500 text-sm">No notifications right now.</div>
                )}
              </div>
            </div>
          )}

          {/* TAB 6: PROFILE */}
          {activeTab === 'profile' && (
            <div className="bg-white border border-gray-200 rounded-xl p-8 shadow-sm max-w-2xl">
              <h3 className="text-lg font-bold text-black mb-6">Account & Profile Settings</h3>
              <form onSubmit={handleProfileSubmit} className="space-y-6">
                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-2">
                    Full Name
                  </label>
                  <input
                    type="text"
                    value={profileForm.fullName}
                    onChange={(e) => setProfileForm({ ...profileForm, fullName: e.target.value })}
                    required
                    className="w-full px-4 py-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-2">
                    Email Address
                  </label>
                  <input
                    type="email"
                    value={user?.email || ''}
                    disabled
                    className="w-full px-4 py-2.5 border border-gray-200 bg-gray-50 rounded text-sm text-gray-500 cursor-not-allowed"
                  />
                  <p className="text-[11px] text-gray-400 mt-1">Email cannot be changed.</p>
                </div>

                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-2">
                    Phone Number
                  </label>
                  <input
                    type="text"
                    value={profileForm.phone}
                    onChange={(e) => setProfileForm({ ...profileForm, phone: e.target.value })}
                    required
                    className="w-full px-4 py-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-2">
                    Profile Photo URL
                  </label>
                  <input
                    type="url"
                    value={profileForm.profilePhoto}
                    onChange={(e) => setProfileForm({ ...profileForm, profilePhoto: e.target.value })}
                    placeholder="https://example.com/avatar.jpg"
                    className="w-full px-4 py-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                  />
                </div>

                <div className="pt-6 border-t border-gray-200">
                  <h4 className="text-sm font-bold text-black mb-4">Change Password</h4>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                        Current Password
                      </label>
                      <input
                        type="password"
                        value={profileForm.currentPassword}
                        onChange={(e) => setProfileForm({ ...profileForm, currentPassword: e.target.value })}
                        className="w-full px-4 py-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                        New Password (min 8 chars)
                      </label>
                      <input
                        type="password"
                        value={profileForm.newPassword}
                        onChange={(e) => setProfileForm({ ...profileForm, newPassword: e.target.value })}
                        className="w-full px-4 py-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                        Confirm New Password
                      </label>
                      <input
                        type="password"
                        value={profileForm.confirmPassword}
                        onChange={(e) => setProfileForm({ ...profileForm, confirmPassword: e.target.value })}
                        className="w-full px-4 py-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                      />
                    </div>
                  </div>
                </div>

                <div className="pt-4">
                  <Button variant="primary" type="submit" isLoading={profileSaving}>
                    Save Changes
                  </Button>
                </div>
              </form>
            </div>
          )}

        </div>
      </div>

      {/* CANCEL MODAL */}
      {showCancelModal && selectedBooking && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6 shadow-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-black">Cancel Reservation #{selectedBooking.bookingNumber}</h3>
            <p className="text-xs text-gray-500 mt-1">
              Please enter the reason for cancellation. Refund rules will apply and eligible amounts will be reimbursed.
            </p>
            <div className="mt-4">
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Cancellation Reason
              </label>
              <textarea
                value={cancelReason}
                onChange={(e) => setCancelReason(e.target.value)}
                placeholder="Change of schedule, booked another vehicle, etc."
                rows={3}
                className="w-full p-3 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
              />
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <Button variant="outline" onClick={() => setShowCancelModal(false)}>
                Back
              </Button>
              <Button variant="danger" isLoading={cancelLoading} onClick={handleCancelBooking}>
                Confirm Cancellation
              </Button>
            </div>
          </div>
        </div>
      )}

      {/* EXTEND MODAL */}
      {showExtendModal && selectedBooking && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6 shadow-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-black">Extend Rental Duration</h3>
            <p className="text-xs text-gray-500 mt-1">
              {selectedBooking.vehicleBrand} {selectedBooking.vehicleModel} (#{selectedBooking.bookingNumber})
            </p>
            <div className="mt-4">
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                New Return Date & Time
              </label>
              <input
                type="datetime-local"
                value={extendDate}
                onChange={(e) => setExtendDate(e.target.value)}
                className="w-full p-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
              />
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <Button variant="outline" onClick={() => setShowExtendModal(false)}>
                Cancel
              </Button>
              <Button variant="primary" isLoading={extendLoading} onClick={handleExtendBooking}>
                Confirm Extension
              </Button>
            </div>
          </div>
        </div>
      )}

      {/* MODIFY MODAL */}
      {showModifyModal && selectedBooking && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6 shadow-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-black">Modify Booking #{selectedBooking.bookingNumber}</h3>
            <p className="text-xs text-gray-500 mt-1">Change dates or pickup/return locations.</p>
            <div className="mt-4 space-y-4">
              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Pickup Date & Time
                </label>
                <input
                  type="datetime-local"
                  value={modifyData.pickupDate}
                  onChange={(e) => setModifyData({ ...modifyData, pickupDate: e.target.value })}
                  className="w-full p-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Return Date & Time
                </label>
                <input
                  type="datetime-local"
                  value={modifyData.returnDate}
                  onChange={(e) => setModifyData({ ...modifyData, returnDate: e.target.value })}
                  className="w-full p-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Pickup Location
                </label>
                <input
                  type="text"
                  value={modifyData.pickupLocation}
                  onChange={(e) => setModifyData({ ...modifyData, pickupLocation: e.target.value })}
                  className="w-full p-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                  Return Location
                </label>
                <input
                  type="text"
                  value={modifyData.returnLocation}
                  onChange={(e) => setModifyData({ ...modifyData, returnLocation: e.target.value })}
                  className="w-full p-2.5 border border-gray-300 rounded text-sm focus:border-black focus:outline-none"
                />
              </div>
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <Button variant="outline" onClick={() => setShowModifyModal(false)}>
                Cancel
              </Button>
              <Button variant="primary" isLoading={modifyLoading} onClick={handleModifyBooking}>
                Save Changes
              </Button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default UserDashboard;
