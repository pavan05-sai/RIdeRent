import React, { createContext, useContext, useEffect, useState } from 'react';
import { AuthResponse, User } from '../types';
import { authService } from '../services/authService';

interface AuthContextType {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  isLoading: boolean;
  login: (email: string, pass: string) => Promise<void>;
  register: (fullName: string, email: string, phone: string, pass: string) => Promise<void>;
  logout: () => void;
  updateUser: (updated: User) => void;
  updateProfile: (data: {
    fullName: string;
    phone?: string;
    profilePhoto?: string;
    currentPassword?: string;
    newPassword?: string;
  }) => Promise<User>;
  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(() => {
    const saved = localStorage.getItem('user');
    return saved ? JSON.parse(saved) : null;
  });
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('token'));
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const initAuth = async () => {
      if (token) {
        try {
          const profile = await authService.getCurrentUser();
          setUser(profile);
          localStorage.setItem('user', JSON.stringify(profile));
        } catch {
          logout();
        }
      }
      setIsLoading(false);
    };
    initAuth();
  }, [token]);

  const login = async (email: string, pass: string) => {
    const authData: AuthResponse = await authService.login(email, pass);
    localStorage.setItem('token', authData.token);
    const userInfo: User = {
      id: authData.id,
      email: authData.email,
      fullName: authData.fullName,
      phone: authData.phone,
      profilePhoto: authData.profilePhoto,
      isActive: true,
      roles: authData.roles,
    };
    localStorage.setItem('user', JSON.stringify(userInfo));
    setToken(authData.token);
    setUser(userInfo);
  };

  const register = async (fullName: string, email: string, phone: string, pass: string) => {
    await authService.register(fullName, email, phone, pass);
    await login(email, pass);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };

  const updateUser = (updated: User) => {
    setUser(updated);
    localStorage.setItem('user', JSON.stringify(updated));
  };

  const updateProfile = async (data: {
    fullName: string;
    phone?: string;
    profilePhoto?: string;
    currentPassword?: string;
    newPassword?: string;
  }) => {
    const updated = await authService.updateProfile(data);
    updateUser(updated);
    return updated;
  };

  const refreshUser = async () => {
    if (token) {
      const profile = await authService.getCurrentUser();
      setUser(profile);
      localStorage.setItem('user', JSON.stringify(profile));
    }
  };

  const isAdmin = user?.roles?.includes('ROLE_ADMIN') ?? false;

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token && !!user,
        isAdmin,
        isLoading,
        login,
        register,
        logout,
        updateUser,
        updateProfile,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
