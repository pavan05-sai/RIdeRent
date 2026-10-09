import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Calendar, MapPin, Tag, CheckCircle2, AlertCircle, ArrowRight, ShieldCheck, CreditCard, Smartphone, Banknote } from 'lucide-react';
import { Vehicle, BookingQuote, Booking, PaymentMethod } from '../../types';
import { bookingService } from '../../services/bookingService';
import { paymentService } from '../../services/paymentService';
import { useAuth } from '../../context/AuthContext';
import { useToast } from '../../context/ToastContext';
import { Modal } from '../common/Modal';
import { Button } from '../common/Button';

interface BookingModalProps {
  vehicle: Vehicle | null;
  isOpen: boolean;
  onClose: () => void;
  onBookingSuccess?: (booking: Booking) => void;
}

export const BookingModal: React.FC<BookingModalProps> = ({
  vehicle,
  isOpen,
  onClose,
  onBookingSuccess,
}) => {
  const { isAuthenticated } = useAuth();
  const { success, error, info } = useToast();
  const navigate = useNavigate();

  // Booking step: 1: Details & Quote, 2: Demo Payment, 3: Confirmed
  const [step, setStep] = useState<1 | 2 | 3>(1);

  // Form State
  const defaultPickup = new Date(Date.now() + 86400000).toISOString().slice(0, 16);
  const defaultReturn = new Date(Date.now() + 86400000 * 3).toISOString().slice(0, 16);

  const [pickupDate, setPickupDate] = useState<string>(defaultPickup);
  const [returnDate, setReturnDate] = useState<string>(defaultReturn);
  const [pickupLocation, setPickupLocation] = useState<string>('');
  const [returnLocation, setReturnLocation] = useState<string>('');
  const [couponCode, setCouponCode] = useState<string>('');

  // Quote & Booking State
  const [quote, setQuote] = useState<BookingQuote | null>(null);
  const [isQuoting, setIsQuoting] = useState<boolean>(false);
  const [quoteError, setQuoteError] = useState<string | null>(null);
  const [createdBooking, setCreatedBooking] = useState<Booking | null>(null);
  const [isBooking, setIsBooking] = useState<boolean>(false);

  // Demo Payment State
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('CARD');
  const [simulatedCard, setSimulatedCard] = useState<string>('4111 •••• •••• 1111');
  const [simulatedUpi, setSimulatedUpi] = useState<string>('user@okaxis');
  const [isPaying, setIsPaying] = useState<boolean>(false);

  useEffect(() => {
    if (vehicle && isOpen) {
      setStep(1);
      setPickupLocation(vehicle.location || '');
      setReturnLocation(vehicle.location || '');
      fetchQuote(couponCode);
    }
  }, [vehicle, isOpen, pickupDate, returnDate]);

  const fetchQuote = async (appliedCoupon?: string) => {
    if (!vehicle) return;
    setIsQuoting(true);
    setQuoteError(null);
    const formattedPickup = pickupDate.length === 16 ? `${pickupDate}:00` : pickupDate;
    const formattedReturn = returnDate.length === 16 ? `${returnDate}:00` : returnDate;

    try {
      const q = await bookingService.calculateQuote({
        vehicleId: vehicle.id,
        pickupDate: formattedPickup,
        returnDate: formattedReturn,
        couponCode: appliedCoupon || undefined,
      });
      setQuote(q);
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Could not calculate quote for dates';
      setQuoteError(msg);
      setQuote(null);
    } finally {
      setIsQuoting(false);
    }
  };

  const handleApplyCoupon = (e: React.FormEvent) => {
    e.preventDefault();
    if (!couponCode.trim()) return;
    fetchQuote(couponCode.trim());
  };

  const handleProceedToPayment = async () => {
    if (!isAuthenticated) {
      info('Please sign in to confirm your booking');
      navigate('/login');
      return;
    }
    if (!vehicle || !quote) return;

    const formattedPickup = pickupDate.length === 16 ? `${pickupDate}:00` : pickupDate;
    const formattedReturn = returnDate.length === 16 ? `${returnDate}:00` : returnDate;

    setIsBooking(true);
    try {
      const newBooking = await bookingService.createBooking({
        vehicleId: vehicle.id,
        pickupDate: formattedPickup,
        returnDate: formattedReturn,
        pickupLocation,
        returnLocation,
        couponCode: quote.couponCode,
      });
      setCreatedBooking(newBooking);
      setStep(2);
      success('Booking created! Please complete demo payment.');
    } catch (err: any) {
      error(err.response?.data?.message || 'Failed to create booking');
    } finally {
      setIsBooking(false);
    }
  };

  const handleConfirmDemoPayment = async () => {
    if (!createdBooking) return;
    setIsPaying(true);
    try {
      await paymentService.processDemoPayment({
        bookingId: createdBooking.id,
        paymentMethod,
        simulatedCardNumber: simulatedCard,
        simulatedUpiId: simulatedUpi,
      });
      setStep(3);
      success('Demo payment confirmed! Your booking is activated.');
      if (onBookingSuccess) {
        onBookingSuccess(createdBooking);
      }
    } catch (err: any) {
      error(err.response?.data?.message || 'Payment simulation failed');
    } finally {
      setIsPaying(false);
    }
  };

  if (!vehicle) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} maxWidth="xl" title={`Reserve ${vehicle.brand} ${vehicle.model}`}>
      {/* Step Indicator */}
      <div className="flex items-center justify-between pb-6 mb-6 border-b border-[#E5E5E5] text-xs font-semibold">
        <div className={`flex items-center space-x-2 ${step >= 1 ? 'text-black font-bold' : 'text-[#888888]'}`}>
          <span className={`w-5 h-5 rounded-full flex items-center justify-center text-[10px] ${step >= 1 ? 'bg-black text-white' : 'bg-[#E5E5E5]'}`}>1</span>
          <span>Rental Details</span>
        </div>
        <div className="h-0.5 flex-1 mx-3 bg-[#E5E5E5]" />
        <div className={`flex items-center space-x-2 ${step >= 2 ? 'text-black font-bold' : 'text-[#888888]'}`}>
          <span className={`w-5 h-5 rounded-full flex items-center justify-center text-[10px] ${step >= 2 ? 'bg-black text-white' : 'bg-[#E5E5E5]'}`}>2</span>
          <span>Demo Payment</span>
        </div>
        <div className="h-0.5 flex-1 mx-3 bg-[#E5E5E5]" />
        <div className={`flex items-center space-x-2 ${step === 3 ? 'text-black font-bold' : 'text-[#888888]'}`}>
          <span className={`w-5 h-5 rounded-full flex items-center justify-center text-[10px] ${step === 3 ? 'bg-black text-white' : 'bg-[#E5E5E5]'}`}>3</span>
          <span>Confirmed</span>
        </div>
      </div>

      {/* STEP 1: Details & Dynamic Quote */}
      {step === 1 && (
        <div className="space-y-6">
          {/* Pickup & Return Dates */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="text-xs font-semibold text-black block mb-1">Pickup Date & Time</label>
              <div className="relative">
                <input
                  type="datetime-local"
                  value={pickupDate}
                  onChange={(e) => setPickupDate(e.target.value)}
                  className="w-full px-3 py-2 text-xs bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>
            </div>
            <div>
              <label className="text-xs font-semibold text-black block mb-1">Return Date & Time</label>
              <div className="relative">
                <input
                  type="datetime-local"
                  value={returnDate}
                  onChange={(e) => setReturnDate(e.target.value)}
                  className="w-full px-3 py-2 text-xs bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>
            </div>
          </div>

          {/* Locations */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="text-xs font-semibold text-black block mb-1">Pickup Location</label>
              <div className="relative">
                <MapPin className="w-3.5 h-3.5 absolute left-3 top-3 text-[#888888]" />
                <input
                  type="text"
                  placeholder="Enter pickup location"
                  value={pickupLocation}
                  onChange={(e) => setPickupLocation(e.target.value)}
                  className="w-full pl-8 pr-3 py-2 text-xs bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>
            </div>
            <div>
              <label className="text-xs font-semibold text-black block mb-1">Return Location</label>
              <div className="relative">
                <MapPin className="w-3.5 h-3.5 absolute left-3 top-3 text-[#888888]" />
                <input
                  type="text"
                  placeholder="Enter return location"
                  value={returnLocation}
                  onChange={(e) => setReturnLocation(e.target.value)}
                  className="w-full pl-8 pr-3 py-2 text-xs bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>
            </div>
          </div>

          {/* Coupon Code Input */}
          <div>
            <label className="text-xs font-semibold text-black block mb-1">Promotional Coupon</label>
            <div className="flex space-x-2">
              <div className="relative flex-1">
                <Tag className="w-3.5 h-3.5 absolute left-3 top-3 text-[#888888]" />
                <input
                  type="text"
                  placeholder="Enter coupon code (optional)"
                  value={couponCode}
                  onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
                  className="w-full pl-8 pr-3 py-2 text-xs uppercase font-mono bg-[#FBFBFB] border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>
              <Button type="button" onClick={handleApplyCoupon} variant="secondary" size="sm">
                Apply
              </Button>
            </div>
          </div>

          {/* Error Message */}
          {quoteError && (
            <div className="p-3 bg-[#F8F8F8] border border-[#CCCCCC] rounded-lg flex items-center space-x-2 text-xs text-black">
              <AlertCircle className="w-4 h-4 text-black shrink-0" />
              <span>{quoteError}</span>
            </div>
          )}

          {/* Dynamic Pricing Breakdown */}
          {quote && (
            <div className="p-4 bg-[#F8F8F8] border border-[#E5E5E5] rounded-xl space-y-2.5">
              <div className="flex justify-between items-center text-xs text-[#555555]">
                <span>
                  Base Rate (₹{quote.pricePerDay.toLocaleString()} × {quote.rentalDays} {quote.rentalDays === 1 ? 'day' : 'days'})
                </span>
                <span className="font-semibold text-black">₹{quote.baseAmount.toLocaleString('en-IN')}</span>
              </div>

              {quote.discountAmount > 0 && (
                <div className="flex justify-between items-center text-xs text-black font-medium">
                  <span>Discount ({quote.couponCode})</span>
                  <span>-₹{quote.discountAmount.toLocaleString('en-IN')}</span>
                </div>
              )}

              <div className="flex justify-between items-center text-xs text-[#555555]">
                <span>Applicable Goods & Services Tax (GST 18%)</span>
                <span className="font-semibold text-black">₹{quote.taxAmount.toLocaleString('en-IN')}</span>
              </div>

              <div className="flex justify-between items-center text-xs text-[#555555]">
                <span>Refundable Security Deposit</span>
                <span className="font-semibold text-black">₹{quote.securityDeposit.toLocaleString('en-IN')}</span>
              </div>

              <div className="pt-3 border-t border-[#E5E5E5] flex justify-between items-center">
                <div>
                  <span className="text-xs font-bold text-black uppercase tracking-wider block">Estimated Total</span>
                  <span className="text-[10px] text-[#777777]">Includes tax & refundable deposit</span>
                </div>
                <span className="text-xl font-black text-black">
                  ₹{quote.totalAmount.toLocaleString('en-IN')}
                </span>
              </div>
            </div>
          )}

          {/* Action Buttons */}
          <div className="flex justify-end space-x-3 pt-2">
            <Button variant="ghost" onClick={onClose}>
              Cancel
            </Button>
            <Button
              variant="primary"
              disabled={!quote || isQuoting}
              isLoading={isBooking}
              onClick={handleProceedToPayment}
            >
              <span>Continue to Payment</span>
              <ArrowRight className="w-4 h-4 ml-1" />
            </Button>
          </div>
        </div>
      )}

      {/* STEP 2: Demo Payment Flow */}
      {step === 2 && createdBooking && (
        <div className="space-y-6">
          <div className="p-4 bg-[#F8F8F8] border border-[#E5E5E5] rounded-xl flex items-center justify-between">
            <div>
              <span className="text-xs text-[#666666] block">Booking Reference</span>
              <span className="text-sm font-mono font-bold text-black">{createdBooking.bookingReference}</span>
            </div>
            <div className="text-right">
              <span className="text-xs text-[#666666] block">Payable Amount</span>
              <span className="text-lg font-black text-black">₹{createdBooking.totalAmount.toLocaleString('en-IN')}</span>
            </div>
          </div>

          <div className="p-3 bg-black text-white rounded-lg text-xs flex items-center space-x-2">
            <ShieldCheck className="w-4 h-4 text-white shrink-0" />
            <span>This is a simulated demo environment. No real bank or card transaction will take place.</span>
          </div>

          {/* Payment Method Selector */}
          <div>
            <label className="text-xs font-semibold text-black block mb-2">Select Payment Method</label>
            <div className="grid grid-cols-3 gap-3">
              {[
                { method: 'CARD' as PaymentMethod, label: 'Credit/Debit Card', icon: CreditCard },
                { method: 'UPI' as PaymentMethod, label: 'UPI / QR', icon: Smartphone },
                { method: 'CASH' as PaymentMethod, label: 'Pay at Counter', icon: Banknote },
              ].map((m) => {
                const Icon = m.icon;
                const isSelected = paymentMethod === m.method;
                return (
                  <button
                    key={m.method}
                    type="button"
                    onClick={() => setPaymentMethod(m.method)}
                    className={`p-3 rounded-xl border text-center transition-all flex flex-col items-center justify-center space-y-1.5 ${
                      isSelected
                        ? 'border-black bg-black text-white shadow-subtle'
                        : 'border-[#E5E5E5] bg-white text-[#444444] hover:border-black'
                    }`}
                  >
                    <Icon className="w-5 h-5" />
                    <span className="text-xs font-bold leading-tight">{m.label}</span>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Simulated Credentials */}
          {paymentMethod === 'CARD' && (
            <div className="space-y-3 p-4 bg-[#FBFBFB] border border-[#E5E5E5] rounded-xl">
              <div>
                <label className="text-[11px] font-semibold text-black block mb-1">Demo Card Number</label>
                <input
                  type="text"
                  value={simulatedCard}
                  onChange={(e) => setSimulatedCard(e.target.value)}
                  className="w-full px-3 py-2 text-xs font-mono bg-white border border-[#E5E5E5] rounded-lg"
                />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-[11px] font-semibold text-black block mb-1">Expiry</label>
                  <input
                    type="text"
                    defaultValue="12/28"
                    className="w-full px-3 py-2 text-xs font-mono bg-white border border-[#E5E5E5] rounded-lg"
                  />
                </div>
                <div>
                  <label className="text-[11px] font-semibold text-black block mb-1">CVV</label>
                  <input
                    type="text"
                    defaultValue="999"
                    className="w-full px-3 py-2 text-xs font-mono bg-white border border-[#E5E5E5] rounded-lg"
                  />
                </div>
              </div>
            </div>
          )}

          {paymentMethod === 'UPI' && (
            <div className="p-4 bg-[#FBFBFB] border border-[#E5E5E5] rounded-xl space-y-2">
              <label className="text-[11px] font-semibold text-black block">Virtual UPI ID</label>
              <input
                type="text"
                value={simulatedUpi}
                onChange={(e) => setSimulatedUpi(e.target.value)}
                className="w-full px-3 py-2 text-xs font-mono bg-white border border-[#E5E5E5] rounded-lg"
              />
            </div>
          )}

          {paymentMethod === 'CASH' && (
            <div className="p-4 bg-[#FBFBFB] border border-[#E5E5E5] rounded-xl text-xs text-[#555555]">
              You can settle the payment at our mobility pickup counter when verifying your identification and driving credentials.
            </div>
          )}

          {/* Action Buttons */}
          <div className="flex justify-end space-x-3 pt-2">
            <Button variant="ghost" onClick={() => setStep(1)}>
              Back
            </Button>
            <Button
              variant="primary"
              isLoading={isPaying}
              onClick={handleConfirmDemoPayment}
            >
              <span>Confirm Demo Payment (₹{createdBooking.totalAmount.toLocaleString('en-IN')})</span>
            </Button>
          </div>
        </div>
      )}

      {/* STEP 3: Confirmed & Success */}
      {step === 3 && createdBooking && (
        <div className="text-center py-6 space-y-4">
          <div className="w-16 h-16 bg-black text-white rounded-full flex items-center justify-center mx-auto">
            <CheckCircle2 className="w-8 h-8 text-white" />
          </div>
          <div>
            <h3 className="text-xl font-bold text-black">Booking Confirmed!</h3>
            <p className="text-xs text-[#666666] mt-1 max-w-sm mx-auto">
              Your reservation reference is{' '}
              <span className="font-mono font-bold text-black">{createdBooking.bookingReference}</span>.
              A confirmation notification and digital invoice have been issued.
            </p>
          </div>

          <div className="flex flex-col sm:flex-row justify-center gap-3 pt-4">
            <Button
              variant="secondary"
              onClick={() => {
                onClose();
                navigate('/dashboard/bookings');
              }}
            >
              View in My Bookings
            </Button>
            <Button
              variant="primary"
              onClick={() => {
                onClose();
                navigate('/dashboard');
              }}
            >
              Go to Dashboard
            </Button>
          </div>
        </div>
      )}
    </Modal>
  );
};
