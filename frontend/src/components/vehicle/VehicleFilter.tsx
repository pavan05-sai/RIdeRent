import React from 'react';
import { Search, RotateCcw, Filter } from 'lucide-react';
import { VehicleFilterParams } from '../../services/vehicleService';
import { VehicleType } from '../../types';

interface VehicleFilterProps {
  filters: VehicleFilterParams;
  onChange: (updated: VehicleFilterParams) => void;
  onReset: () => void;
}

export const VehicleFilter: React.FC<VehicleFilterProps> = ({ filters, onChange, onReset }) => {
  const vehicleTypes: { label: string; value: VehicleType | undefined }[] = [
    { label: 'All Fleet', value: undefined },
    { label: 'Cars', value: 'CAR' },
    { label: 'Sedans', value: 'SEDAN' },
    { label: 'SUVs', value: 'SUV' },
    { label: 'Hatchbacks', value: 'HATCHBACK' },
    { label: 'Bikes', value: 'BIKE' },
    { label: 'Luxury', value: 'LUXURY' },
  ];

  return (
    <div className="bg-white border border-[#E5E5E5] rounded-2xl p-6 shadow-subtle space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between pb-4 border-b border-[#F0F0F0]">
        <div className="flex items-center space-x-2">
          <Filter className="w-4 h-4 text-black" />
          <h3 className="text-sm font-bold text-black uppercase tracking-wider">Refine Search</h3>
        </div>
        <button
          onClick={onReset}
          className="text-xs text-[#666666] hover:text-black flex items-center space-x-1 transition-colors"
        >
          <RotateCcw className="w-3 h-3" />
          <span>Reset</span>
        </button>
      </div>

      {/* Search Input */}
      <div>
        <label className="text-xs font-semibold text-black block mb-2">Search Vehicle or City</label>
        <div className="relative">
          <Search className="w-4 h-4 absolute left-3 top-3 text-[#888888]" />
          <input
            type="text"
            placeholder="Search brand, model, city..."
            value={filters.search || ''}
            onChange={(e) => onChange({ ...filters, search: e.target.value, page: 0 })}
            className="w-full pl-9 pr-4 py-2 text-sm bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black transition-colors"
          />
        </div>
      </div>

      {/* Vehicle Category Pills */}
      <div>
        <label className="text-xs font-semibold text-black block mb-2">Vehicle Category</label>
        <div className="flex flex-wrap gap-1.5">
          {vehicleTypes.map((t) => {
            const isSelected = filters.vehicleType === t.value;
            return (
              <button
                key={t.label}
                onClick={() => onChange({ ...filters, vehicleType: t.value, page: 0 })}
                className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
                  isSelected
                    ? 'bg-black text-white font-semibold'
                    : 'bg-[#F5F5F5] text-[#444444] hover:bg-[#EAEAEA] hover:text-black'
                }`}
              >
                {t.label}
              </button>
            );
          })}
        </div>
      </div>

      {/* Transmission Dropdown */}
      <div>
        <label className="text-xs font-semibold text-black block mb-2">Transmission</label>
        <div className="grid grid-cols-3 gap-2">
          {['', 'Automatic', 'Manual'].map((tr) => (
            <button
              key={tr || 'all'}
              onClick={() => onChange({ ...filters, transmission: tr || undefined, page: 0 })}
              className={`py-2 text-xs font-medium rounded-lg border transition-colors ${
                (filters.transmission === tr) || (!filters.transmission && !tr)
                  ? 'border-black bg-black text-white font-semibold'
                  : 'border-[#E5E5E5] bg-white text-[#555555] hover:border-black'
              }`}
            >
              {tr || 'All'}
            </button>
          ))}
        </div>
      </div>

      {/* Fuel Type */}
      <div>
        <label className="text-xs font-semibold text-black block mb-2">Fuel Type</label>
        <select
          value={filters.fuelType || ''}
          onChange={(e) => onChange({ ...filters, fuelType: e.target.value || undefined, page: 0 })}
          className="w-full px-3 py-2 text-sm bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
        >
          <option value="">All Fuel Types</option>
          <option value="Petrol">Petrol</option>
          <option value="Diesel">Diesel</option>
          <option value="Electric">Electric</option>
          <option value="Hybrid">Hybrid</option>
        </select>
      </div>

      {/* Price Range */}
      <div>
        <div className="flex justify-between items-center mb-2">
          <label className="text-xs font-semibold text-black">Max Daily Budget</label>
          <span className="text-xs font-bold text-black">
            {filters.maxPrice ? `₹${filters.maxPrice.toLocaleString('en-IN')}` : 'Any'}
          </span>
        </div>
        <input
          type="range"
          min="1000"
          max="25000"
          step="500"
          value={filters.maxPrice || 25000}
          onChange={(e) => onChange({ ...filters, maxPrice: Number(e.target.value), page: 0 })}
          className="w-full accent-black cursor-pointer"
        />
        <div className="flex justify-between text-[10px] text-[#888888] mt-1">
          <span>₹1,000</span>
          <span>₹25,000+</span>
        </div>
      </div>

      {/* Seating Capacity */}
      <div>
        <label className="text-xs font-semibold text-black block mb-2">Seating Capacity</label>
        <div className="flex gap-2">
          {[undefined, 2, 5, 7].map((s) => (
            <button
              key={s || 'all'}
              onClick={() => onChange({ ...filters, seats: s, page: 0 })}
              className={`flex-1 py-1.5 text-xs font-medium rounded-lg border transition-colors ${
                filters.seats === s
                  ? 'border-black bg-black text-white font-semibold'
                  : 'border-[#E5E5E5] bg-white text-[#555555] hover:border-black'
              }`}
            >
              {s ? `${s} Seats` : 'Any'}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};
