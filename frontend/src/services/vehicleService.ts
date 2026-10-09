import api from './api';
import { ApiResponse, PageResponse, Vehicle, VehicleDetails, VehicleType } from '../types';

export interface VehicleFilterParams {
  search?: string;
  vehicleType?: VehicleType;
  fuelType?: string;
  transmission?: string;
  location?: string;
  minPrice?: number;
  maxPrice?: number;
  seats?: number;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
}

export const vehicleService = {
  getVehicles: async (params: VehicleFilterParams = {}): Promise<PageResponse<Vehicle>> => {
    const res = await api.get<ApiResponse<PageResponse<Vehicle>>>('/vehicles', { params });
    return res.data.data;
  },

  getVehicleById: async (id: number): Promise<Vehicle> => {
    const res = await api.get<ApiResponse<Vehicle>>(`/vehicles/${id}`);
    return res.data.data;
  },

  getVehicleDetails: async (id: number): Promise<VehicleDetails> => {
    const res = await api.get<ApiResponse<VehicleDetails>>(`/vehicles/${id}/details`);
    return res.data.data;
  },

  getFeaturedVehicles: async (): Promise<Vehicle[]> => {
    const res = await api.get<ApiResponse<Vehicle[]>>('/vehicles/featured');
    return res.data.data;
  },

  checkAvailability: async (vehicleId: number, pickupDate: string, returnDate: string): Promise<boolean> => {
    const res = await api.get<ApiResponse<{ available: boolean }>>(`/vehicles/${vehicleId}/availability`, {
      params: { pickupDate, returnDate },
    });
    return res.data.data.available;
  },

  compareVehicles: async (vehicleIds: number[]): Promise<Vehicle[]> => {
    const res = await api.post<ApiResponse<Vehicle[]>>('/vehicles/compare', { vehicleIds });
    return res.data.data;
  },

  subscribeAvailabilityAlert: async (vehicleId: number): Promise<void> => {
    await api.post(`/vehicles/${vehicleId}/notify-availability`);
  },
};
