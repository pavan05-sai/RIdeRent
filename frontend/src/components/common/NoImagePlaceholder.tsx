import React from 'react';
import { Car } from 'lucide-react';

interface NoImagePlaceholderProps {
  className?: string;
  vehicleType?: string;
}

export const NoImagePlaceholder: React.FC<NoImagePlaceholderProps> = ({
  className = 'w-full h-full',
  vehicleType,
}) => {
  return (
    <div
      className={`flex flex-col items-center justify-center bg-[#F9F9F9] text-[#111111] border-b border-[#E5E5E5] select-none p-4 text-center ${className}`}
      aria-label="No Image Available"
    >
      <div className="w-12 h-12 rounded-xl bg-white border border-[#E5E5E5] flex items-center justify-center shadow-xs mb-2.5">
        <Car className="w-6 h-6 text-[#333333]" strokeWidth={1.5} />
      </div>
      <span className="text-[11px] font-bold uppercase tracking-wider text-black">
        {vehicleType ? `${vehicleType} • RideRent` : 'RideRent Fleet'}
      </span>
      <span className="text-[10px] text-[#777777] font-medium tracking-normal mt-0.5">
        No Image Available
      </span>
    </div>
  );
};
