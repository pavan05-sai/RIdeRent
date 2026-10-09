import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Car, SlidersHorizontal, ArrowUpDown } from 'lucide-react';
import { vehicleService, VehicleFilterParams } from '../../services/vehicleService';
import { Vehicle, PageResponse, VehicleType } from '../../types';
import { VehicleCard } from '../../components/vehicle/VehicleCard';
import { VehicleFilter } from '../../components/vehicle/VehicleFilter';
import { BookingModal } from '../../components/booking/BookingModal';
import { Pagination } from '../../components/common/Pagination';
import { EmptyState } from '../../components/common/EmptyState';
import { SkeletonCard } from '../../components/common/SkeletonLoader';

export const Vehicles: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  // Read initial params from URL if available
  const [filters, setFilters] = useState<VehicleFilterParams>({
    search: searchParams.get('search') || undefined,
    vehicleType: (searchParams.get('vehicleType') as VehicleType) || undefined,
    location: searchParams.get('location') || undefined,
    page: 0,
    size: 9,
    sortBy: 'pricePerDay',
    sortDirection: 'asc',
  });

  const [pageData, setPageData] = useState<PageResponse<Vehicle> | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [selectedVehicle, setSelectedVehicle] = useState<Vehicle | null>(null);
  const [bookingModalOpen, setBookingModalOpen] = useState<boolean>(false);
  const [mobileFilterOpen, setMobileFilterOpen] = useState<boolean>(false);

  useEffect(() => {
    const fetchVehicles = async () => {
      setIsLoading(true);
      try {
        const data = await vehicleService.getVehicles(filters);
        setPageData(data);
      } catch (err) {
        console.error('Failed to load vehicles', err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchVehicles();
  }, [filters]);

  const handleFilterChange = (updated: VehicleFilterParams) => {
    setFilters(updated);
  };

  const handleResetFilters = () => {
    setFilters({
      page: 0,
      size: 9,
      sortBy: 'pricePerDay',
      sortDirection: 'asc',
    });
    setSearchParams({});
  };

  const handleSortChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const val = e.target.value;
    if (val === 'price_asc') {
      setFilters({ ...filters, sortBy: 'pricePerDay', sortDirection: 'asc', page: 0 });
    } else if (val === 'price_desc') {
      setFilters({ ...filters, sortBy: 'pricePerDay', sortDirection: 'desc', page: 0 });
    } else if (val === 'rating') {
      setFilters({ ...filters, sortBy: 'rating', sortDirection: 'desc', page: 0 });
    } else if (val === 'newest') {
      setFilters({ ...filters, sortBy: 'createdAt', sortDirection: 'desc', page: 0 });
    }
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Header Banner */}
      <div className="bg-[#FBFBFB] border-b border-[#E5E5E5] py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <span className="text-xs uppercase font-bold tracking-widest text-[#666666] block mb-1">
            Commercial Fleet
          </span>
          <h1 className="text-3xl sm:text-5xl font-black text-black tracking-tight">
            Explore All Vehicles
          </h1>
          <p className="text-sm text-[#666666] mt-2 max-w-xl">
            Choose from high-performance sedans, electric cruisers, rugged SUVs, and adrenaline-charged superbikes.
          </p>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Desktop Filter Sidebar */}
          <div className="hidden lg:block lg:col-span-1">
            <div className="sticky top-28">
              <VehicleFilter
                filters={filters}
                onChange={handleFilterChange}
                onReset={handleResetFilters}
              />
            </div>
          </div>

          {/* Vehicle Grid & Controls */}
          <div className="lg:col-span-3 space-y-6">
            {/* Top Toolbar */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-[#F0F0F0]">
              <div className="flex items-center space-x-3">
                <button
                  onClick={() => setMobileFilterOpen(!mobileFilterOpen)}
                  className="lg:hidden px-3 py-2 text-xs font-bold border border-[#E5E5E5] rounded-lg flex items-center space-x-1.5"
                >
                  <SlidersHorizontal className="w-3.5 h-3.5" />
                  <span>Filters</span>
                </button>
                <span className="text-xs text-[#666666]">
                  Showing <strong className="text-black font-semibold">{pageData?.totalElements || 0}</strong> vehicles
                </span>
              </div>

              {/* Sort Selector */}
              <div className="flex items-center space-x-2">
                <ArrowUpDown className="w-3.5 h-3.5 text-[#888888]" />
                <span className="text-xs font-semibold text-black">Sort by:</span>
                <select
                  onChange={handleSortChange}
                  defaultValue="price_asc"
                  className="text-xs bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg px-2.5 py-1.5 focus:outline-none focus:border-black font-medium"
                >
                  <option value="price_asc">Price: Low to High</option>
                  <option value="price_desc">Price: High to Low</option>
                  <option value="rating">Highest Rated</option>
                  <option value="newest">Newest Arrivals</option>
                </select>
              </div>
            </div>

            {/* Mobile Filters Dropdown */}
            {mobileFilterOpen && (
              <div className="lg:hidden mb-6">
                <VehicleFilter
                  filters={filters}
                  onChange={handleFilterChange}
                  onReset={handleResetFilters}
                />
              </div>
            )}

            {/* Vehicle Grid */}
            {isLoading ? (
              <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
                {[1, 2, 3, 4, 5, 6].map((n) => (
                  <SkeletonCard key={n} />
                ))}
              </div>
            ) : pageData?.content && pageData.content.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
                {pageData.content.map((vehicle) => (
                  <VehicleCard
                    key={vehicle.id}
                    vehicle={vehicle}
                    onBookNow={(v) => {
                      setSelectedVehicle(v);
                      setBookingModalOpen(true);
                    }}
                  />
                ))}
              </div>
            ) : (
              <EmptyState
                icon={Car}
                title="No vehicles available"
                description={
                  filters.search || filters.vehicleType || filters.location
                    ? "No vehicles match the selected criteria. Try adjusting or clearing your filters."
                    : "No vehicles are currently available in the fleet."
                }
                actionText={filters.search || filters.vehicleType || filters.location ? "Reset All Filters" : undefined}
                onAction={filters.search || filters.vehicleType || filters.location ? handleResetFilters : undefined}
              />
            )}

            {/* Pagination */}
            {pageData && pageData.totalPages > 1 && (
              <Pagination
                currentPage={pageData.number}
                totalPages={pageData.totalPages}
                onPageChange={(page) => setFilters({ ...filters, page })}
              />
            )}
          </div>
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
