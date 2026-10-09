import React, { createContext, useContext, useState } from 'react';
import { Vehicle } from '../types';
import { useToast } from './ToastContext';

interface CompareContextType {
  compareList: Vehicle[];
  addToCompare: (vehicle: Vehicle) => void;
  removeFromCompare: (vehicleId: number) => void;
  clearCompare: () => void;
  isInCompare: (vehicleId: number) => boolean;
}

const CompareContext = createContext<CompareContextType | undefined>(undefined);

export const CompareProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [compareList, setCompareList] = useState<Vehicle[]>([]);
  const { info, error } = useToast();

  const addToCompare = (vehicle: Vehicle) => {
    if (compareList.some((v) => v.id === vehicle.id)) {
      info('Vehicle already in comparison');
      return;
    }
    if (compareList.length >= 3) {
      error('You can compare a maximum of 3 vehicles at once');
      return;
    }
    setCompareList((prev) => [...prev, vehicle]);
    info(`Added ${vehicle.brand} ${vehicle.model} to comparison`);
  };

  const removeFromCompare = (vehicleId: number) => {
    setCompareList((prev) => prev.filter((v) => v.id !== vehicleId));
  };

  const clearCompare = () => {
    setCompareList([]);
  };

  const isInCompare = (vehicleId: number) => {
    return compareList.some((v) => v.id === vehicleId);
  };

  return (
    <CompareContext.Provider
      value={{
        compareList,
        addToCompare,
        removeFromCompare,
        clearCompare,
        isInCompare,
      }}
    >
      {children}
    </CompareContext.Provider>
  );
};

export const useCompare = () => {
  const context = useContext(CompareContext);
  if (!context) {
    throw new Error('useCompare must be used within a CompareProvider');
  }
  return context;
};
