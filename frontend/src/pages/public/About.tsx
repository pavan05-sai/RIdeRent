import React from 'react';
import { ShieldCheck, Award, Users, CheckCircle2 } from 'lucide-react';

export const About: React.FC = () => {
  return (
    <div className="min-h-screen bg-white">
      <div className="bg-[#FBFBFB] border-b border-[#E5E5E5] py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center space-y-3">
          <span className="text-xs uppercase font-bold tracking-widest text-[#666666] block">
            About RideRent
          </span>
          <h1 className="text-4xl sm:text-5xl font-black text-black tracking-tight">
            Redefining Commercial Vehicle Mobility
          </h1>
          <p className="text-base text-[#666666] leading-relaxed">
            Founded with a commitment to pure transparency, zero AI gimmicks, and strict automotive precision.
          </p>
        </div>
      </div>

      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-16 space-y-12">
        <div className="space-y-4">
          <h2 className="text-2xl font-bold text-black">Our Philosophy</h2>
          <p className="text-sm text-[#444444] leading-relaxed">
            RideRent was engineered to solve the most pervasive issues in vehicle rental: bait-and-switch car models, surprise deposit withholding, and opaque pricing. Our fleet consists exclusively of enterprise-maintained, company-owned vehicles verified before every reservation.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-4">
          <div className="p-6 bg-[#FAFAFA] border border-[#E5E5E5] rounded-xl space-y-2">
            <ShieldCheck className="w-6 h-6 text-black" />
            <h4 className="text-sm font-bold text-black">100% Owned Fleet</h4>
            <p className="text-xs text-[#666666]">Zero third-party broker listings. Every vehicle is inspected by certified technicians.</p>
          </div>
          <div className="p-6 bg-[#FAFAFA] border border-[#E5E5E5] rounded-xl space-y-2">
            <Award className="w-6 h-6 text-black" />
            <h4 className="text-sm font-bold text-black">Transparent Pricing</h4>
            <p className="text-xs text-[#666666]">Base rate, GST/Tax, and refundable security deposits are clearly calculated upfront.</p>
          </div>
          <div className="p-6 bg-[#FAFAFA] border border-[#E5E5E5] rounded-xl space-y-2">
            <Users className="w-6 h-6 text-black" />
            <h4 className="text-sm font-bold text-black">Executive Service</h4>
            <p className="text-xs text-[#666666]">Contactless check-ins, immediate PDF invoices, and rapid deposit refunds within 24h.</p>
          </div>
        </div>

        <div className="pt-8 border-t border-[#E5E5E5] space-y-4">
          <h2 className="text-2xl font-bold text-black">Our Fleet Standards</h2>
          <div className="space-y-3">
            {[
              'Comprehensive mechanical & diagnostic evaluation before every customer dispatch.',
              'Original equipment manufacturer (OEM) service schedule strictly enforced.',
              '100% sanitized cabins, odor-free, full tank fuel policy.',
              'Dual-channel safety assistance and 24/7 breakdown helpline on all highways.',
            ].map((p, idx) => (
              <div key={idx} className="flex items-start space-x-3 text-sm text-[#444444]">
                <CheckCircle2 className="w-4 h-4 text-black shrink-0 mt-0.5" />
                <span>{p}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
