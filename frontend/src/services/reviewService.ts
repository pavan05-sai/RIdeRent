import api from './api';
import { ApiResponse, Review } from '../types';

export const reviewService = {
  createReview: async (data: {
    vehicleId: number;
    bookingId: number;
    rating: number;
    comment: string;
  }): Promise<Review> => {
    const res = await api.post<ApiResponse<Review>>('/reviews', data);
    return res.data.data;
  },

  getVehicleReviews: async (vehicleId: number): Promise<Review[]> => {
    const res = await api.get<ApiResponse<Review[]>>(`/reviews/vehicle/${vehicleId}`);
    return res.data.data;
  },

  deleteReview: async (id: number): Promise<void> => {
    await api.delete(`/reviews/${id}`);
  },
};
