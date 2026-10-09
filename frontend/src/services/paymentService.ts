import api from './api';
import { ApiResponse, Payment, PaymentMethod } from '../types';

export const paymentService = {
  processDemoPayment: async (data: {
    bookingId: number;
    paymentMethod: PaymentMethod;
    simulatedCardNumber?: string;
    simulatedUpiId?: string;
  }): Promise<Payment> => {
    const res = await api.post<ApiResponse<Payment>>('/payments/demo', data);
    return res.data.data;
  },

  getPaymentForBooking: async (bookingId: number): Promise<Payment> => {
    const res = await api.get<ApiResponse<Payment>>(`/payments/booking/${bookingId}`);
    return res.data.data;
  },
};
