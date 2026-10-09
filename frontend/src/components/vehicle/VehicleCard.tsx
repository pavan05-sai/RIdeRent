import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Star, Fuel, Gauge, Users, Heart, ArrowRight, Check } from 'lucide-react';
import { Vehicle } from '../../types';
import { useWishlist } from '../../context/WishlistContext';
import { useCompare } from '../../context/CompareContext';
import { Badge } from '../common/Badge';
import { NoImagePlaceholder } from '../common/NoImagePlaceholder';

interface VehicleCardProps {
  vehicle: Vehicle;
  onBookNow?: (vehicle: Vehicle) => void;
}

export const VehicleCard: React.FC<VehicleCardProps> = ({ vehicle, onBookNow }) => {
  const { isInWishlist, toggleWishlist } = useWishlist();
  const { isInCompare, addToCompare, removeFromCompare } = useCompare();
  const navigate = useNavigate();

  const isSaved = isInWishlist(vehicle.id);
  const isCompared = isInCompare(vehicle.id);
  const isAvailable = vehicle.status === 'AVAILABLE';

  const handleCompareClick = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (isCompared) {
      removeFromCompare(vehicle.id);
    } else {
      addToCompare(vehicle);
    }
  };

  const handleWishlistClick = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    toggleWishlist(vehicle);
  };

  const handleBookClick = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (onBookNow) {
      onBookNow(vehicle);
    } else {
      navigate(`/vehicles/${vehicle.id}`);
    }
  };

  const [imgError, setImgError] = React.useState(false);
  const hasImage = Boolean(vehicle.primaryImageUrl) && !imgError;

  return (
    <div className="group bg-white border border-[#E5E5E5] rounded-2xl overflow-hidden hover:shadow-premium transition-all duration-300 flex flex-col justify-between hover:border-black">
      <div>
        {/* Image & Overlay Badges */}
        <div className="relative aspect-[16/10] overflow-hidden bg-[#F5F5F5] flex items-center justify-center">
          {hasImage ? (
            <img
              src={vehicle.primaryImageUrl}
              alt={`${vehicle.brand} ${vehicle.model}`}
              onError={() => setImgError(true)}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
              loading="lazy"
            />
          ) : (
            <NoImagePlaceholder vehicleType={vehicle.vehicleType} />
          )}

          {/* Top Floating Badges */}
          <div className="absolute top-3 left-3 flex flex-wrap gap-1.5 z-10">
            <span className="px-2.5 py-1 text-[11px] font-bold bg-black text-white rounded-md tracking-wider">
              {vehicle.vehicleType}
            </span>
            {!isAvailable && (
              <span className="px-2.5 py-1 text-[11px] font-bold bg-[#E5E5E5] text-[#222222] border border-[#CCCCCC] rounded-md">
                {vehicle.status}
              </span>
            )}
          </div>

          {/* Floating Actions */}
          <div className="absolute top-3 right-3 flex items-center space-x-1.5 z-10">
            {/* Compare Button */}
            <button
              onClick={handleCompareClick}
              className={`p-2 rounded-full backdrop-blur-md transition-colors ${
                isCompared
                  ? 'bg-black text-white'
                  : 'bg-white/90 text-[#444444] hover:text-black hover:bg-white'
              }`}
              title={isCompared ? 'Remove from compare' : 'Compare model'}
            >
              {isCompared ? <Check className="w-3.5 h-3.5" /> : <span className="text-[10px] font-bold px-0.5">VS</span>}
            </button>

            {/* Wishlist Button */}
            <button
              onClick={handleWishlistClick}
              className={`p-2 rounded-full backdrop-blur-md transition-colors ${
                isSaved
                  ? 'bg-black text-white'
                  : 'bg-white/90 text-[#444444] hover:text-black hover:bg-white'
              }`}
              title={isSaved ? 'Remove from wishlist' : 'Save to wishlist'}
            >
              <Heart className={`w-3.5 h-3.5 ${isSaved ? 'fill-white text-white' : ''}`} />
            </button>
          </div>
        </div>

        {/* Vehicle Metadata */}
        <div className="p-5">
          <div className="flex items-start justify-between">
            <div>
              <p className="text-xs uppercase tracking-wider text-[#666666] font-semibold">{vehicle.brand}</p>
              <Link to={`/vehicles/${vehicle.id}`}>
                <h3 className="text-lg font-bold text-black group-hover:underline line-clamp-1">
                  {vehicle.model}
                </h3>
              </Link>
            </div>
            <div className="flex items-center space-x-1 bg-[#F5F5F5] px-2 py-1 rounded-md">
              <Star className="w-3.5 h-3.5 fill-black text-black" />
              <span className="text-xs font-bold text-black">{vehicle.rating}</span>
              <span className="text-[10px] text-[#666666]">({vehicle.totalReviews})</span>
            </div>
          </div>

          {/* Specifications Pills */}
          <div className="grid grid-cols-3 gap-2 mt-4 pt-4 border-t border-[#F0F0F0] text-xs text-[#555555]">
            <div className="flex items-center space-x-1.5" title="Fuel Type">
              <Fuel className="w-3.5 h-3.5 text-black shrink-0" />
              <span className="truncate">{vehicle.fuelType}</span>
            </div>
            <div className="flex items-center space-x-1.5" title="Transmission">
              <Gauge className="w-3.5 h-3.5 text-black shrink-0" />
              <span className="truncate">{vehicle.transmission}</span>
            </div>
            <div className="flex items-center space-x-1.5" title="Seating Capacity">
              <Users className="w-3.5 h-3.5 text-black shrink-0" />
              <span>{vehicle.seatingCapacity} Seats</span>
            </div>
          </div>
        </div>
      </div>

      {/* Card Footer: Price & CTA */}
      <div className="px-5 pb-5 pt-2 border-t border-[#F5F5F5] flex items-center justify-between">
        <div>
          <span className="text-xs text-[#777777] block leading-none">Starting from</span>
          <span className="text-xl font-black text-black">
            ₹{vehicle.pricePerDay.toLocaleString('en-IN')}
          </span>
          <span className="text-xs text-[#777777]"> / day</span>
        </div>

        <button
          onClick={handleBookClick}
          disabled={!isAvailable}
          className="px-4 py-2 bg-black text-white hover:bg-[#222222] disabled:bg-[#E5E5E5] disabled:text-[#888888] disabled:cursor-not-allowed rounded-lg text-xs font-bold transition-colors flex items-center space-x-1.5"
        >
          <span>{isAvailable ? 'Book Now' : 'Unavailable'}</span>
          {isAvailable && <ArrowRight className="w-3.5 h-3.5" />}
        </button>
      </div>
    </div>
  );
};
