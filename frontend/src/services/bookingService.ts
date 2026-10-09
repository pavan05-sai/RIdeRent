import api from './api';
import { ApiResponse, Booking, BookingQuote, PageResponse } from '../types';

export const bookingService = {
  calculateQuote: async (data: {
    vehicleId: number;
    pickupDate: string;
    returnDate: string;
    couponCode?: string;
  }): Promise<BookingQuote> => {
    const res = await api.post<ApiResponse<BookingQuote>>('/bookings/quote', data);
    return res.data.data;
  },

  createBooking: async (data: {
    vehicleId: number;
    pickupDate: string;
    returnDate: string;
    pickupLocation: string;
    returnLocation: string;
    couponCode?: string;
  }): Promise<Booking> => {
    const res = await api.post<ApiResponse<Booking>>('/bookings', data);
    return res.data.data;
  },

  getMyBookings: async (page = 0, size = 10): Promise<PageResponse<Booking>> => {
    const res = await api.get<ApiResponse<PageResponse<Booking>>>('/bookings/my', {
      params: { page, size },
    });
    return res.data.data;
  },

  getActiveRental: async (): Promise<Booking | null> => {
    const res = await api.get<ApiResponse<Booking | null>>('/bookings/my/active');
    return res.data.data;
  },

  getBookingById: async (id: number): Promise<Booking> => {
    const res = await api.get<ApiResponse<Booking>>(`/bookings/${id}`);
    return res.data.data;
  },

  modifyBooking: async (
    id: number,
    data: {
      pickupDate: string;
      returnDate: string;
      pickupLocation?: string;
      returnLocation?: string;
    }
  ): Promise<Booking> => {
    const res = await api.put<ApiResponse<Booking>>(`/bookings/${id}/modify`, data);
    return res.data.data;
  },

  extendBooking: async (id: number, newReturnDate: string): Promise<Booking> => {
    const res = await api.post<ApiResponse<Booking>>(`/bookings/${id}/extend`, { newReturnDate });
    return res.data.data;
  },

  cancelBooking: async (id: number, reason: string): Promise<Booking> => {
    const res = await api.post<ApiResponse<Booking>>(`/bookings/${id}/cancel`, { reason });
    return res.data.data;
  },

  downloadInvoicePdf: async (id: number): Promise<Blob> => {
    const res = await api.get(`/bookings/${id}/invoice/pdf`, { responseType: 'blob' });
    return res.data;
  },

  downloadAgreementPdf: async (id: number): Promise<Blob> => {
    const res = await api.get(`/bookings/${id}/agreement/pdf`, { responseType: 'blob' });
    return res.data;
  },
};
