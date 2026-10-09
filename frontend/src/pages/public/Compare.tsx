import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { X, ArrowRight, Check, Star, Car, Trash2 } from 'lucide-react';
import { useCompare } from '../../context/CompareContext';
import { Vehicle } from '../../types';
import { Button } from '../../components/common/Button';
import { EmptyState } from '../../components/common/EmptyState';
import { BookingModal } from '../../components/booking/BookingModal';

export const Compare: React.FC = () => {
  const { compareList, removeFromCompare, clearCompare } = useCompare();
  const [selectedVehicle, setSelectedVehicle] = useState<Vehicle | null>(null);
  const [bookingModalOpen, setBookingModalOpen] = useState(false);

  if (compareList.length === 0) {
    return (
      <div className="min-h-screen bg-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <EmptyState
            icon={Car}
            title="No vehicles in comparison"
            description="You can compare up to 3 models simultaneously to make an informed choice. Click 'VS' or 'Compare Model' on any vehicle card to begin."
            actionText="Browse Vehicle Fleet"
            onAction={() => window.location.assign('/vehicles')}
          />
        </div>
      </div>
    );
  }

  const specRows = [
    { label: 'Daily Rate', key: 'price', render: (v: Vehicle) => `₹${v.pricePerDay.toLocaleString('en-IN')} / day` },
    { label: 'Refundable Deposit', key: 'deposit', render: (v: Vehicle) => `₹${v.securityDeposit.toLocaleString('en-IN')}` },
    { label: 'Category', key: 'type', render: (v: Vehicle) => v.vehicleType },
    { label: 'Fuel Type', key: 'fuel', render: (v: Vehicle) => v.fuelType },
    { label: 'Transmission', key: 'transmission', render: (v: Vehicle) => v.transmission },
    { label: 'Seating Capacity', key: 'seats', render: (v: Vehicle) => `${v.seatingCapacity} Passengers` },
    { label: 'Customer Rating', key: 'rating', render: (v: Vehicle) => `★ ${v.rating} (${v.totalReviews} reviews)` },
    { label: 'Station Location', key: 'location', render: (v: Vehicle) => v.location },
    { label: 'Availability', key: 'status', render: (v: Vehicle) => (
      <span className={`inline-block px-2 py-0.5 text-xs font-bold rounded ${v.status === 'AVAILABLE' ? 'bg-black text-white' : 'bg-[#E5E5E5] text-[#444444]'}`}>
        {v.status}
      </span>
    )},
  ];

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <div className="bg-[#FBFBFB] border-b border-[#E5E5E5] py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row sm:items-end justify-between gap-4">
          <div>
            <span className="text-xs uppercase font-bold tracking-widest text-[#666666] block mb-1">
              Side-By-Side Evaluation
            </span>
            <h1 className="text-3xl sm:text-4xl font-black text-black tracking-tight">
              Compare Fleet Models
            </h1>
            <p className="text-sm text-[#666666] mt-1">
              Comparing {compareList.length} of 3 maximum allowable vehicle models.
            </p>
          </div>
          <button
            onClick={clearCompare}
            className="text-xs font-semibold text-[#888888] hover:text-black flex items-center space-x-1.5 transition-colors"
          >
            <Trash2 className="w-3.5 h-3.5" />
            <span>Clear Comparison</span>
          </button>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="overflow-x-auto pb-6">
          <table className="w-full border-collapse border border-[#E5E5E5] min-w-[700px]">
            {/* Vehicle Header Cards */}
            <thead>
              <tr className="border-b border-[#E5E5E5] bg-white">
                <th className="p-4 text-left w-1/4 bg-[#FAFAFA] border-r border-[#E5E5E5] text-xs font-bold uppercase text-[#888888]">
                  Feature / Model
                </th>
                {compareList.map((v) => (
                  <th key={v.id} className="p-6 text-left border-r border-[#E5E5E5] last:border-r-0 align-top">
                    <div className="relative space-y-3">
                      <button
                        onClick={() => removeFromCompare(v.id)}
                        className="absolute -top-2 -right-2 p-1.5 bg-[#F5F5F5] hover:bg-black hover:text-white rounded-full transition-colors"
                        title="Remove from comparison"
                      >
                        <X className="w-4 h-4" />
                      </button>

                      <div className="aspect-[16/10] rounded-xl overflow-hidden bg-[#F5F5F5] border border-[#E5E5E5]">
                        <img src={v.primaryImageUrl} alt={v.model} className="w-full h-full object-cover" />
                      </div>

                      <div>
                        <span className="text-[10px] uppercase font-bold text-[#777777]">{v.brand}</span>
                        <h4 className="text-base font-bold text-black">{v.model}</h4>
                      </div>

                      <Button
                        variant="primary"
                        size="sm"
                        className="w-full"
                        disabled={v.status !== 'AVAILABLE'}
                        onClick={() => {
                          setSelectedVehicle(v);
                          setBookingModalOpen(true);
                        }}
                      >
                        <span>{v.status === 'AVAILABLE' ? 'Book Now' : 'Unavailable'}</span>
                        {v.status === 'AVAILABLE' && <ArrowRight className="w-3.5 h-3.5 ml-1" />}
                      </Button>
                    </div>
                  </th>
                ))}
              </tr>
            </thead>

            {/* Spec Rows */}
            <tbody className="divide-y divide-[#E5E5E5] text-sm">
              {specRows.map((row) => (
                <tr key={row.key} className="hover:bg-[#FAFAFA] transition-colors">
                  <td className="p-4 font-bold text-xs uppercase tracking-wider text-black bg-[#FAFAFA] border-r border-[#E5E5E5]">
                    {row.label}
                  </td>
                  {compareList.map((v) => (
                    <td key={v.id} className="p-4 text-xs font-medium text-black border-r border-[#E5E5E5] last:border-r-0">
                      {row.render(v)}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Booking Modal */}
      <BookingModal
        vehicle={selectedVehicle}
        isOpen={bookingModalOpen}
        onClose={() => {
          setBookingModalOpen(false);
          setSelectedVehicle(null);
        }}
      />
    </div>
  );
};
