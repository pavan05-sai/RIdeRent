import api from './api';
import {
  ApiResponse,
  Booking,
  BookingStatus,
  Coupon,
  DashboardAnalytics,
  DashboardStats,
  MaintenanceRecord,
  MaintenanceStatus,
  Payment,
  Review,
  User,
  Vehicle,
} from '../types';

export const adminService = {
  getStats: async (): Promise<DashboardStats> => {
    const res = await api.get<ApiResponse<DashboardStats>>('/admin/dashboard/stats');
    return res.data.data;
  },

  getAnalytics: async (): Promise<DashboardAnalytics> => {
    const res = await api.get<ApiResponse<DashboardAnalytics>>('/admin/dashboard/analytics');
    return res.data.data;
  },

  getUsers: async (search?: string, page = 0, size = 10) => {
    const res = await api.get('/admin/users', { params: { search, page, size } });
    return res.data.data;
  },

  toggleUserStatus: async (userId: number): Promise<User> => {
    const res = await api.put<ApiResponse<User>>(`/admin/users/${userId}/toggle-status`);
    return res.data.data;
  },

  createVehicle: async (data: Partial<Vehicle>): Promise<Vehicle> => {
    const res = await api.post<ApiResponse<Vehicle>>('/admin/vehicles', data);
    return res.data.data;
  },

  updateVehicle: async (id: number, data: Partial<Vehicle>): Promise<Vehicle> => {
    const res = await api.put<ApiResponse<Vehicle>>(`/admin/vehicles/${id}`, data);
    return res.data.data;
  },

  deleteVehicle: async (id: number): Promise<void> => {
    await api.delete(`/admin/vehicles/${id}`);
  },

  getBookings: async (search?: string, status?: BookingStatus, page = 0, size = 10) => {
    const res = await api.get('/admin/bookings', { params: { search, status, page, size } });
    return res.data.data;
  },

  updateBookingStatus: async (id: number, status: BookingStatus): Promise<Booking> => {
    const res = await api.put<ApiResponse<Booking>>(`/admin/bookings/${id}/status`, { status });
    return res.data.data;
  },

  getPayments: async (page = 0, size = 10) => {
    const res = await api.get('/admin/payments', { params: { page, size } });
    return res.data.data;
  },

  getCoupons: async (): Promise<Coupon[]> => {
    const res = await api.get<ApiResponse<Coupon[]>>('/admin/coupons');
    return res.data.data;
  },

  createCoupon: async (data: Partial<Coupon>): Promise<Coupon> => {
    const res = await api.post<ApiResponse<Coupon>>('/admin/coupons', data);
    return res.data.data;
  },

  deleteCoupon: async (id: number): Promise<void> => {
    await api.delete(`/admin/coupons/${id}`);
  },

  getMaintenanceRecords: async (status?: MaintenanceStatus, page = 0, size = 10) => {
    const res = await api.get('/admin/maintenance', { params: { status, page, size } });
    return res.data.data;
  },

  createMaintenanceRecord: async (data: any): Promise<MaintenanceRecord> => {
    const res = await api.post<ApiResponse<MaintenanceRecord>>('/admin/maintenance', data);
    return res.data.data;
  },

  completeMaintenance: async (id: number, notes?: string): Promise<MaintenanceRecord> => {
    const res = await api.put<ApiResponse<MaintenanceRecord>>(`/admin/maintenance/${id}/complete`, { notes });
    return res.data.data;
  },

  getReviews: async (page = 0, size = 10) => {
    const res = await api.get('/admin/reviews', { params: { page, size } });
    return res.data.data;
  },

  toggleReviewVisibility: async (id: number): Promise<Review> => {
    const res = await api.put<ApiResponse<Review>>(`/admin/reviews/${id}/toggle-visibility`);
    return res.data.data;
  },

  deleteReview: async (id: number): Promise<void> => {
    await api.delete(`/admin/reviews/${id}`);
  },
};
