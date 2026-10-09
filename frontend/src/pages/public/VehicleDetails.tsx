import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import {
  Star,
  Fuel,
  Gauge,
  Users,
  Calendar,
  MapPin,
  Heart,
  Check,
  ShieldCheck,
  Bell,
  ArrowRight,
  ArrowLeft,
  Share2,
} from 'lucide-react';
import { vehicleService } from '../../services/vehicleService';
import { VehicleDetails as VehicleDetailsType } from '../../types';
import { useWishlist } from '../../context/WishlistContext';
import { useCompare } from '../../context/CompareContext';
import { useAuth } from '../../context/AuthContext';
import { useToast } from '../../context/ToastContext';
import { BookingModal } from '../../components/booking/BookingModal';
import { Button } from '../../components/common/Button';
import { Badge } from '../../components/common/Badge';
import { NoImagePlaceholder } from '../../components/common/NoImagePlaceholder';

export const VehicleDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isInWishlist, toggleWishlist } = useWishlist();
  const { isInCompare, addToCompare, removeFromCompare } = useCompare();
  const { isAuthenticated } = useAuth();
  const { success, info } = useToast();

  const [details, setDetails] = useState<VehicleDetailsType | null>(null);
  const [activeImage, setActiveImage] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [bookingModalOpen, setBookingModalOpen] = useState<boolean>(false);
  const [subscribedAlert, setSubscribedAlert] = useState<boolean>(false);

  useEffect(() => {
    if (id) {
      const fetchDetails = async () => {
        setIsLoading(true);
        try {
          const res = await vehicleService.getVehicleDetails(Number(id));
          setDetails(res);
          setActiveImage(res.vehicle.primaryImageUrl);
        } catch (err) {
          console.error('Failed to load vehicle', err);
        } finally {
          setIsLoading(false);
        }
      };
      fetchDetails();
    }
  }, [id]);

  const handleNotifyMe = async () => {
    if (!isAuthenticated) {
      info('Please sign in to subscribe for availability notifications');
      navigate('/login');
      return;
    }
    if (!details) return;
    try {
      await vehicleService.subscribeAvailabilityAlert(details.vehicle.id);
      setSubscribedAlert(true);
      success('You will be notified as soon as this vehicle returns to service!');
    } catch {
      info('Alert subscription updated');
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="animate-spin w-8 h-8 border-2 border-black border-t-transparent rounded-full" />
      </div>
    );
  }

  if (!details) {
    return (
      <div className="min-h-screen bg-white flex flex-col items-center justify-center p-6 text-center">
        <h2 className="text-2xl font-black text-black">Vehicle Not Found</h2>
        <p className="text-sm text-[#666666] mt-2 mb-6">The requested vehicle ID does not exist in our fleet records.</p>
        <Link to="/vehicles">
          <Button variant="primary">Return to Fleet</Button>
        </Link>
      </div>
    );
  }

  const { vehicle, recentReviews, isAvailableNow } = details;
  const isSaved = isInWishlist(vehicle.id);
  const isCompared = isInCompare(vehicle.id);

  return (
    <div className="min-h-screen bg-white">
      {/* Breadcrumb Bar */}
      <div className="border-b border-[#E5E5E5] py-4 bg-[#FBFBFB]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between">
          <Link
            to="/vehicles"
            className="text-xs font-semibold text-[#666666] hover:text-black flex items-center space-x-1.5 transition-colors"
          >
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Back to All Vehicles</span>
          </Link>
          <div className="flex items-center space-x-2">
            <Badge variant="dark">{vehicle.vehicleType}</Badge>
            <span className="text-xs font-mono text-[#888888]">{vehicle.registrationNumber}</span>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12">
          {/* Left Column: Image Gallery & Reviews */}
          <div className="lg:col-span-7 space-y-8">
            {/* Main Image Viewer */}
            <div className="relative aspect-[16/10] bg-[#F5F5F5] rounded-2xl overflow-hidden border border-[#E5E5E5] flex items-center justify-center">
              {activeImage ? (
                <img
                  src={activeImage}
                  alt={`${vehicle.brand} ${vehicle.model}`}
                  className="w-full h-full object-cover"
                />
              ) : (
                <NoImagePlaceholder vehicleType={vehicle.vehicleType} />
              )}
              {!isAvailableNow && (
                <div className="absolute top-4 left-4 bg-black text-white px-3 py-1 rounded-md text-xs font-bold tracking-wider uppercase">
                  {vehicle.status}
                </div>
              )}
            </div>

            {/* Thumbnail Carousel */}
            {vehicle.imageUrls && vehicle.imageUrls.length > 1 && (
              <div className="flex gap-3 overflow-x-auto pb-2">
                {vehicle.imageUrls.map((url, i) => (
                  <button
                    key={i}
                    onClick={() => setActiveImage(url)}
                    className={`relative w-24 h-16 rounded-xl overflow-hidden border-2 shrink-0 transition-all ${
                      activeImage === url ? 'border-black ring-2 ring-black/20' : 'border-[#E5E5E5] opacity-70 hover:opacity-100'
                    }`}
                  >
                    <img src={url} alt={`Thumbnail ${i}`} className="w-full h-full object-cover" />
                  </button>
                ))}
              </div>
            )}

            {/* Vehicle Description */}
            <div className="pt-6 border-t border-[#F0F0F0] space-y-4">
              <h3 className="text-lg font-bold text-black">Vehicle Overview</h3>
              <p className="text-sm text-[#444444] leading-relaxed whitespace-pre-line">
                {vehicle.description || 'Pristine commercial vehicle tuned to perfection and ready for executive or adventure travel.'}
              </p>
            </div>

            {/* Customer Reviews Section */}
            <div className="pt-8 border-t border-[#F0F0F0] space-y-6">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-lg font-bold text-black">Verified Renter Reviews</h3>
                  <p className="text-xs text-[#666666]">Only completed renters can submit verified ratings</p>
                </div>
                <div className="flex items-center space-x-2 bg-[#F5F5F5] px-3 py-1.5 rounded-lg">
                  <Star className="w-4 h-4 fill-black text-black" />
                  <span className="text-sm font-bold text-black">{vehicle.rating}</span>
                  <span className="text-xs text-[#666666]">({vehicle.totalReviews} reviews)</span>
                </div>
              </div>

              {recentReviews.length === 0 ? (
                <div className="p-8 text-center bg-[#FAFAFA] border border-[#E5E5E5] rounded-xl text-xs text-[#666666]">
                  No reviews yet for this vehicle model. Be the first verified customer to rent and review!
                </div>
              ) : (
                <div className="space-y-4">
                  {recentReviews.map((rev) => (
                    <div key={rev.id} className="p-4 bg-[#FAFAFA] border border-[#E5E5E5] rounded-xl space-y-2">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                          {rev.userPhoto ? (
                            <img
                              src={rev.userPhoto}
                              alt={rev.userName}
                              className="w-7 h-7 rounded-full object-cover border border-[#E5E5E5]"
                            />
                          ) : (
                            <div className="w-7 h-7 rounded-full bg-black text-white text-[10px] font-bold flex items-center justify-center">
                              {rev.userName ? rev.userName.charAt(0).toUpperCase() : 'U'}
                            </div>
                          )}
                          <span className="text-xs font-bold text-black">{rev.userName}</span>
                        </div>
                        <div className="flex items-center space-x-1">
                          {Array.from({ length: rev.rating }).map((_, i) => (
                            <Star key={i} className="w-3.5 h-3.5 fill-black text-black" />
                          ))}
                        </div>
                      </div>
                      <p className="text-xs text-[#555555] leading-relaxed italic">"{rev.comment}"</p>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>

          {/* Right Column: Pricing, Specs & Booking Actions */}
          <div className="lg:col-span-5 space-y-6">
            <div className="bg-[#FAFAFA] border border-[#E5E5E5] rounded-2xl p-6 sm:p-8 space-y-6">
              {/* Header Info */}
              <div>
                <span className="text-xs uppercase font-bold tracking-widest text-[#666666] block mb-1">
                  {vehicle.brand} • {vehicle.year}
                </span>
                <h1 className="text-2xl sm:text-3xl font-black text-black tracking-tight">
                  {vehicle.model}
                </h1>
                <div className="flex items-center space-x-2 text-xs text-[#666666] mt-2">
                  <MapPin className="w-3.5 h-3.5 text-black" />
                  <span>Stationed at {vehicle.location}</span>
                </div>
              </div>

              {/* Price Banner */}
              <div className="p-4 bg-white border border-[#E5E5E5] rounded-xl flex items-baseline justify-between">
                <div>
                  <span className="text-xs text-[#777777] block">Daily Rental Rate</span>
                  <span className="text-3xl font-black text-black">
                    ₹{vehicle.pricePerDay.toLocaleString('en-IN')}
                  </span>
                  <span className="text-xs text-[#777777]"> / day</span>
                </div>
                <div className="text-right">
                  <span className="text-[10px] text-[#777777] block">Refundable Deposit</span>
                  <span className="text-sm font-bold text-black">
                    ₹{vehicle.securityDeposit.toLocaleString('en-IN')}
                  </span>
                </div>
              </div>

              {/* Specs Grid */}
              <div className="grid grid-cols-2 gap-3 pt-2">
                <div className="p-3 bg-white border border-[#E5E5E5] rounded-xl flex items-center space-x-3">
                  <Fuel className="w-5 h-5 text-black shrink-0" />
                  <div>
                    <span className="text-[10px] text-[#777777] block uppercase font-bold">Fuel Type</span>
                    <span className="text-xs font-semibold text-black">{vehicle.fuelType}</span>
                  </div>
                </div>

                <div className="p-3 bg-white border border-[#E5E5E5] rounded-xl flex items-center space-x-3">
                  <Gauge className="w-5 h-5 text-black shrink-0" />
                  <div>
                    <span className="text-[10px] text-[#777777] block uppercase font-bold">Transmission</span>
                    <span className="text-xs font-semibold text-black">{vehicle.transmission}</span>
                  </div>
                </div>

                <div className="p-3 bg-white border border-[#E5E5E5] rounded-xl flex items-center space-x-3">
                  <Users className="w-5 h-5 text-black shrink-0" />
                  <div>
                    <span className="text-[10px] text-[#777777] block uppercase font-bold">Seating</span>
                    <span className="text-xs font-semibold text-black">{vehicle.seatingCapacity} Passengers</span>
                  </div>
                </div>

                <div className="p-3 bg-white border border-[#E5E5E5] rounded-xl flex items-center space-x-3">
                  <ShieldCheck className="w-5 h-5 text-black shrink-0" />
                  <div>
                    <span className="text-[10px] text-[#777777] block uppercase font-bold">Insurance</span>
                    <span className="text-xs font-semibold text-black">Comprehensive</span>
                  </div>
                </div>
              </div>

              {/* Booking & Notification Actions */}
              <div className="pt-2 space-y-3">
                {isAvailableNow ? (
                  <Button
                    variant="primary"
                    size="lg"
                    className="w-full shadow-premium"
                    onClick={() => setBookingModalOpen(true)}
                  >
                    <span>Reserve Vehicle Now</span>
                    <ArrowRight className="w-5 h-5 ml-1" />
                  </Button>
                ) : (
                  <div className="space-y-2">
                    <Button
                      variant="secondary"
                      size="lg"
                      className="w-full opacity-60 cursor-not-allowed"
                      disabled
                    >
                      Currently {vehicle.status}
                    </Button>
                    <Button
                      variant="outline"
                      size="md"
                      className="w-full flex items-center justify-center space-x-2"
                      onClick={handleNotifyMe}
                      disabled={subscribedAlert}
                    >
                      <Bell className="w-4 h-4" />
                      <span>{subscribedAlert ? 'Alert Subscribed!' : 'Notify Me When Available'}</span>
                    </Button>
                  </div>
                )}

                {/* Secondary Actions (Wishlist & Compare) */}
                <div className="grid grid-cols-2 gap-3 pt-2">
                  <button
                    onClick={() => toggleWishlist(vehicle)}
                    className={`p-2.5 rounded-lg border text-xs font-semibold flex items-center justify-center space-x-1.5 transition-colors ${
                      isSaved ? 'bg-black text-white border-black' : 'bg-white border-[#E5E5E5] text-black hover:border-black'
                    }`}
                  >
                    <Heart className={`w-4 h-4 ${isSaved ? 'fill-white' : ''}`} />
                    <span>{isSaved ? 'In Wishlist' : 'Add Wishlist'}</span>
                  </button>

                  <button
                    onClick={() => {
                      if (isCompared) removeFromCompare(vehicle.id);
                      else addToCompare(vehicle);
                    }}
                    className={`p-2.5 rounded-lg border text-xs font-semibold flex items-center justify-center space-x-1.5 transition-colors ${
                      isCompared ? 'bg-black text-white border-black' : 'bg-white border-[#E5E5E5] text-black hover:border-black'
                    }`}
                  >
                    <Check className={`w-4 h-4 ${isCompared ? 'opacity-100' : 'opacity-0'}`} />
                    <span>{isCompared ? 'Compared' : 'Compare Model'}</span>
                  </button>
                </div>
              </div>

              {/* Guarantees List */}
              <div className="pt-4 border-t border-[#E5E5E5] space-y-2 text-xs text-[#555555]">
                <div className="flex items-center space-x-2">
                  <Check className="w-3.5 h-3.5 text-black" />
                  <span>Free cancellation up to 48 hours before pickup</span>
                </div>
                <div className="flex items-center space-x-2">
                  <Check className="w-3.5 h-3.5 text-black" />
                  <span>Spotless sanitization & full-tank handover guarantee</span>
                </div>
                <div className="flex items-center space-x-2">
                  <Check className="w-3.5 h-3.5 text-black" />
                  <span>Instant PDF tax invoice upon confirmation</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Booking Modal */}
      <BookingModal
        vehicle={vehicle}
        isOpen={bookingModalOpen}
        onClose={() => setBookingModalOpen(false)}
      />
    </div>
  );
};
