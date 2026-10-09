import React from 'react';

export const SkeletonCard: React.FC = () => {
  return (
    <div className="bg-white border border-[#E5E5E5] rounded-2xl overflow-hidden animate-pulse">
      <div className="h-48 bg-[#EEEEEE] w-full" />
      <div className="p-5 space-y-4">
        <div className="flex justify-between items-center">
          <div className="h-5 bg-[#EEEEEE] rounded w-1/2" />
          <div className="h-4 bg-[#EEEEEE] rounded w-1/4" />
        </div>
        <div className="h-4 bg-[#EEEEEE] rounded w-3/4" />
        <div className="grid grid-cols-3 gap-2 pt-2 border-t border-[#F0F0F0]">
          <div className="h-3 bg-[#EEEEEE] rounded" />
          <div className="h-3 bg-[#EEEEEE] rounded" />
          <div className="h-3 bg-[#EEEEEE] rounded" />
        </div>
        <div className="pt-3 border-t border-[#F0F0F0] flex justify-between items-center">
          <div className="h-6 bg-[#EEEEEE] rounded w-1/3" />
          <div className="h-9 bg-[#EEEEEE] rounded w-1/3" />
        </div>
      </div>
    </div>
  );
};

export const SkeletonRow: React.FC = () => {
  return (
    <div className="flex items-center space-x-4 py-4 animate-pulse border-b border-[#F0F0F0]">
      <div className="w-12 h-12 bg-[#EEEEEE] rounded-lg shrink-0" />
      <div className="flex-1 space-y-2">
        <div className="h-4 bg-[#EEEEEE] rounded w-1/3" />
        <div className="h-3 bg-[#EEEEEE] rounded w-1/2" />
      </div>
      <div className="w-24 h-8 bg-[#EEEEEE] rounded" />
    </div>
  );
};
