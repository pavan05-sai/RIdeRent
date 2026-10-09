import api from './api';
import { ApiResponse, AuthResponse, User } from '../types';

export const authService = {
  login: async (email: string, password: string):Promise<AuthResponse> => {
    const res = await api.post<ApiResponse<AuthResponse>>('/auth/login', { email, password });
    return res.data.data;
  },

  register: async (fullName: string, email: string, phone: string, password: string): Promise<User> => {
    const res = await api.post<ApiResponse<User>>('/auth/register', { fullName, email, phone, password });
    return res.data.data;
  },

  getCurrentUser: async (): Promise<User> => {
    const res = await api.get<ApiResponse<User>>('/auth/me');
    return res.data.data;
  },

  updateProfile: async (data: {
    fullName: string;
    phone?: string;
    profilePhoto?: string;
    currentPassword?: string;
    newPassword?: string;
  }): Promise<User> => {
    const res = await api.put<ApiResponse<User>>('/auth/profile', data);
    return res.data.data;
  },

  forgotPassword: async (email: string): Promise<void> => {
    await api.post('/auth/forgot-password', { email });
  },

  resetPassword: async (token: string, newPassword: string): Promise<void> => {
    await api.post('/auth/reset-password', { token, newPassword });
  },
};
