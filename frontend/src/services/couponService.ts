import api from './api';
import { ApiResponse, Coupon } from '../types';

export const couponService = {
  validateCoupon: async (code: string, bookingAmount: number): Promise<Coupon> => {
    const res = await api.post<ApiResponse<Coupon>>('/coupons/validate', { code, bookingAmount });
    return res.data.data;
  },
};
