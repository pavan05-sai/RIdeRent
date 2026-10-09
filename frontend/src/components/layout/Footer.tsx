import React from 'react';
import { Car } from 'lucide-react';

/**
 * RideRent Minimal Footer
 * Simple RideRent branding only.
 * No fake contact numbers, emails, addresses, or placeholder clutter.
 */
export const Footer: React.FC = () => {
  return (
    <footer className="bg-black text-white border-t border-[#222222] py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-[#888888]">
        <div className="flex items-center space-x-2.5">
          <div className="w-6 h-6 bg-white text-black flex items-center justify-center rounded">
            <Car className="w-3.5 h-3.5 text-black" />
          </div>
          <span className="font-bold text-white tracking-tight text-sm">RideRent</span>
          <span className="text-[#555555]">|</span>
          <span className="text-[#CCCCCC]">Rent. Ride. Return.</span>
        </div>
        <div>
          &copy; {new Date().getFullYear()} RideRent. All rights reserved.
        </div>
      </div>
    </footer>
  );
};
