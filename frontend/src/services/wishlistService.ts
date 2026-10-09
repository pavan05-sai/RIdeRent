import api from './api';
import { ApiResponse, Vehicle } from '../types';

export const wishlistService = {
  getWishlist: async (): Promise<Vehicle[]> => {
    const res = await api.get<ApiResponse<Vehicle[]>>('/wishlist');
    return res.data.data;
  },

  addToWishlist: async (vehicleId: number): Promise<void> => {
    await api.post(`/wishlist/${vehicleId}`);
  },

  removeFromWishlist: async (vehicleId: number): Promise<void> => {
    await api.delete(`/wishlist/${vehicleId}`);
  },

  checkWishlist: async (vehicleId: number): Promise<boolean> => {
    const res = await api.get<ApiResponse<{ inWishlist: boolean }>>(`/wishlist/check/${vehicleId}`);
    return res.data.data.inWishlist;
  },
};
