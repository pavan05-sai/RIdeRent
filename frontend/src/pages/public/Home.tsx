import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { AnimatedVehicle } from '../../components/common/AnimatedVehicle';
import Button from '../../components/common/Button';

/**
 * RideRent Homepage
 * Clean, minimal, premium landing page.
 * Hero section with RideRent, "Rent. Ride. Return.", short description,
 * [Explore Vehicles] button, and monochrome animated vector car visual.
 */
export const Home: React.FC = () => {
  return (
    <div className="min-h-screen bg-white text-[#111111] flex flex-col justify-center">
      {/* HERO SECTION */}
      <section className="relative py-20 md:py-32 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 lg:gap-8 items-center">
            {/* Left Copy */}
            <div className="lg:col-span-7 space-y-6 text-left">
              <div className="inline-flex items-center space-x-2 px-3 py-1 bg-[#F8F8F8] border border-[#E5E5E5] rounded-full text-xs font-semibold tracking-wider uppercase text-[#555555]">
                <span className="w-1.5 h-1.5 rounded-full bg-black"></span>
                <span>RideRent</span>
              </div>

              <h1 className="text-5xl sm:text-6xl lg:text-7xl font-black tracking-tight leading-[1.05] text-black">
                Rent. Ride.<br />
                <span className="underline decoration-2 underline-offset-8">Return.</span>
              </h1>

              <p className="text-lg sm:text-xl text-[#555555] max-w-xl font-normal leading-relaxed">
                Find the right ride for every journey.
              </p>

              {/* Action Button */}
              <div className="pt-4">
                <Link to="/vehicles">
                  <Button variant="primary" size="lg" className="shadow-lg">
                    <span>Explore Vehicles</span>
                    <ArrowRight className="w-4 h-4 ml-1.5" />
                  </Button>
                </Link>
              </div>
            </div>

            {/* Right Side: Animated Vehicle Visual Branding (Visual only) */}
            <div className="lg:col-span-5 w-full flex justify-center">
              <AnimatedVehicle />
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};
