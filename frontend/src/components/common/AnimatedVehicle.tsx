import React from 'react';

/**
 * AnimatedVehicle: A pure monochrome vector illustration of a modern coupe.
 * Features subtle floating dynamics and rotating geometric wheel alloys.
 * Visual branding ONLY — not connected to fleet inventory or rental listings.
 */
export const AnimatedVehicle: React.FC = () => {
  return (
    <div className="relative w-full max-w-lg mx-auto flex flex-col items-center select-none" aria-hidden="true">
      {/* Ambient background glow & grid lines */}
      <div className="absolute inset-0 bg-gradient-to-b from-[#F5F5F5] to-transparent rounded-3xl -z-10 border border-[#EBEBEB]" />

      <div className="w-full px-6 pt-10 pb-6">
        {/* Floating Car Silhouette */}
        <div className="animate-vehicle-float">
          <svg
            viewBox="0 0 600 240"
            className="w-full h-auto drop-shadow-[0_12px_24px_rgba(0,0,0,0.12)]"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            {/* Aerodynamic Bodywork (Sleek Modern Silhouette) */}
            <path
              d="M 50 160 
                 C 55 140, 80 135, 110 135 
                 C 130 115, 175 80, 260 70 
                 C 370 58, 440 90, 480 120 
                 C 525 125, 555 140, 560 160 
                 C 560 170, 545 175, 520 175 
                 L 460 175 
                 C 455 140, 395 140, 390 175 
                 L 210 175 
                 C 205 140, 145 140, 140 175 
                 L 65 175 
                 C 50 175, 45 168, 50 160 Z"
              fill="#000000"
              stroke="#000000"
              strokeWidth="2"
            />

            {/* Greenhouse Roofline & Glass */}
            <path
              d="M 185 125 
                 C 215 90, 270 78, 350 78 
                 C 410 78, 445 100, 465 125 
                 Z"
              fill="#FFFFFF"
              stroke="#000000"
              strokeWidth="2"
            />

            {/* Pillar Separators (B-Pillar) */}
            <line x1="320" y1="78" x2="320" y2="125" stroke="#000000" strokeWidth="4" />

            {/* Headlight & Taillight Precision Strokes */}
            <path d="M 540 148 L 560 152" stroke="#FFFFFF" strokeWidth="4" strokeLinecap="round" />
            <path d="M 50 148 L 65 152" stroke="#FFFFFF" strokeWidth="3" strokeLinecap="round" />

            {/* Aerodynamic Character Line */}
            <path
              d="M 85 142 C 160 140, 260 132, 420 132 C 480 132, 520 145, 545 150"
              stroke="#FFFFFF"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeOpacity="0.4"
            />

            {/* Front Wheel Assembly */}
            <g transform="translate(425, 175)">
              {/* Outer Tyre */}
              <circle cx="0" cy="0" r="38" fill="#111111" stroke="#000000" strokeWidth="3" />
              {/* Rim Outer */}
              <circle cx="0" cy="0" r="28" fill="#FFFFFF" stroke="#000000" strokeWidth="2" />
              {/* Rotating Alloy Spokes */}
              <g className="animate-wheel-spin">
                <circle cx="0" cy="0" r="8" fill="#000000" />
                <line x1="0" y1="-26" x2="0" y2="26" stroke="#000000" strokeWidth="3" />
                <line x1="-26" y1="0" x2="26" y2="0" stroke="#000000" strokeWidth="3" />
                <line x1="-18" y1="-18" x2="18" y2="18" stroke="#000000" strokeWidth="2.5" />
                <line x1="-18" y1="18" x2="18" y2="-18" stroke="#000000" strokeWidth="2.5" />
              </g>
            </g>

            {/* Rear Wheel Assembly */}
            <g transform="translate(175, 175)">
              {/* Outer Tyre */}
              <circle cx="0" cy="0" r="38" fill="#111111" stroke="#000000" strokeWidth="3" />
              {/* Rim Outer */}
              <circle cx="0" cy="0" r="28" fill="#FFFFFF" stroke="#000000" strokeWidth="2" />
              {/* Rotating Alloy Spokes */}
              <g className="animate-wheel-spin">
                <circle cx="0" cy="0" r="8" fill="#000000" />
                <line x1="0" y1="-26" x2="0" y2="26" stroke="#000000" strokeWidth="3" />
                <line x1="-26" y1="0" x2="26" y2="0" stroke="#000000" strokeWidth="3" />
                <line x1="-18" y1="-18" x2="18" y2="18" stroke="#000000" strokeWidth="2.5" />
                <line x1="-18" y1="18" x2="18" y2="-18" stroke="#000000" strokeWidth="2.5" />
              </g>
            </g>
          </svg>
        </div>

        {/* Dynamic Road Underline */}
        <div className="w-full flex justify-center -mt-2">
          <svg viewBox="0 0 500 20" className="w-full max-w-md h-5" fill="none">
            {/* Base ground line */}
            <line x1="10" y1="10" x2="490" y2="10" stroke="#E5E5E5" strokeWidth="2" strokeLinecap="round" />
            {/* Animated velocity dash */}
            <line
              x1="30"
              y1="10"
              x2="470"
              y2="10"
              stroke="#000000"
              strokeWidth="2.5"
              strokeDasharray="14 18"
              strokeLinecap="round"
              className="animate-road"
            />
          </svg>
        </div>
      </div>

      {/* Subtle Monochrome Badge */}
      <div className="pb-4 text-center">
        <span className="text-[10px] uppercase font-bold tracking-widest text-[#777777] bg-white px-3 py-1 border border-[#E5E5E5] rounded-full">
          Precision Fleet Velocity
        </span>
      </div>
    </div>
  );
};
