import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { Vehicle } from '../types';
import { wishlistService } from '../services/wishlistService';
import { useAuth } from './AuthContext';
import { useToast } from './ToastContext';

interface WishlistContextType {
  wishlist: Vehicle[];
  wishlistCount: number;
  isInWishlist: (vehicleId: number) => boolean;
  toggleWishlist: (vehicle: Vehicle) => Promise<void>;
  removeFromWishlist: (vehicleId: number) => Promise<void>;
  refreshWishlist: () => Promise<void>;
}

const WishlistContext = createContext<WishlistContextType | undefined>(undefined);

export const WishlistProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [wishlist, setWishlist] = useState<Vehicle[]>([]);
  const { isAuthenticated } = useAuth();
  const { success, info } = useToast();

  const refreshWishlist = useCallback(async () => {
    if (isAuthenticated) {
      try {
        const items = await wishlistService.getWishlist();
        setWishlist(items);
      } catch (err) {
        console.error('Failed to load wishlist', err);
      }
    } else {
      setWishlist([]);
    }
  }, [isAuthenticated]);

  useEffect(() => {
    refreshWishlist();
  }, [refreshWishlist]);

  const isInWishlist = useCallback(
    (vehicleId: number) => {
      return wishlist.some((v) => v.id === vehicleId);
    },
    [wishlist]
  );

  const toggleWishlist = async (vehicle: Vehicle) => {
    if (!isAuthenticated) {
      info('Please sign in to save vehicles to your wishlist');
      return;
    }

    const inList = isInWishlist(vehicle.id);
    try {
      if (inList) {
        await wishlistService.removeFromWishlist(vehicle.id);
        setWishlist((prev) => prev.filter((v) => v.id !== vehicle.id));
        info(`${vehicle.brand} ${vehicle.model} removed from wishlist`);
      } else {
        await wishlistService.addToWishlist(vehicle.id);
        setWishlist((prev) => [vehicle, ...prev]);
        success(`${vehicle.brand} ${vehicle.model} saved to wishlist`);
      }
    } catch {
      info('Could not update wishlist. Please try again.');
    }
  };

  const removeFromWishlist = async (vehicleId: number) => {
    try {
      await wishlistService.removeFromWishlist(vehicleId);
      setWishlist((prev) => prev.filter((v) => v.id !== vehicleId));
      info('Vehicle removed from wishlist');
    } catch {
      info('Could not remove vehicle from wishlist');
    }
  };

  return (
    <WishlistContext.Provider
      value={{
        wishlist,
        wishlistCount: wishlist.length,
        isInWishlist,
        toggleWishlist,
        removeFromWishlist,
        refreshWishlist,
      }}
    >
      {children}
    </WishlistContext.Provider>
  );
};

export const useWishlist = () => {
  const context = useContext(WishlistContext);
  if (!context) {
    throw new Error('useWishlist must be used within a WishlistProvider');
  }
  return context;
};
