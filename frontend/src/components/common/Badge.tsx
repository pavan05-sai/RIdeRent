import React from 'react';

interface BadgeProps {
  children?: React.ReactNode;
  variant?: 'dark' | 'outline' | 'subtle' | string;
  size?: 'sm' | 'md';
}

export const Badge: React.FC<BadgeProps> = ({ children, variant = 'subtle', size = 'sm' }) => {
  const isDark = variant === 'dark' || variant === 'CONFIRMED' || variant === 'ACTIVE' || variant === 'AVAILABLE';
  const isSubtle = !isDark;

  const styleClass = isDark
    ? 'bg-black text-white border border-black'
    : 'bg-[#F2F2F2] text-[#222222] border border-[#E5E5E5]';

  const sizeClass = size === 'sm'
    ? 'text-[10px] px-2 py-0.5 font-bold uppercase tracking-wider'
    : 'text-xs px-2.5 py-1 font-semibold';

  return (
    <span className={`inline-flex items-center rounded-full ${styleClass} ${sizeClass}`}>
      {children || variant}
    </span>
  );
};

export default Badge;
