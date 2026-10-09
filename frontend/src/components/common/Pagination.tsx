import React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export const Pagination: React.FC<PaginationProps> = ({ currentPage, totalPages, onPageChange }) => {
  if (totalPages <= 1) return null;

  return (
    <div className="flex items-center justify-center space-x-2 my-8">
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0}
        className="p-2 border border-[#E5E5E5] rounded-lg text-black hover:bg-[#F5F5F5] disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
        aria-label="Previous Page"
      >
        <ChevronLeft className="w-4 h-4" />
      </button>

      {Array.from({ length: totalPages }, (_, i) => i).map((p) => {
        // Show first, last, and current +- 1
        if (p === 0 || p === totalPages - 1 || (p >= currentPage - 1 && p <= currentPage + 1)) {
          return (
            <button
              key={p}
              onClick={() => onPageChange(p)}
              className={`w-9 h-9 text-xs font-semibold rounded-lg transition-colors ${
                currentPage === p
                  ? 'bg-black text-white'
                  : 'border border-[#E5E5E5] text-[#444444] hover:bg-[#F5F5F5] hover:text-black'
              }`}
            >
              {p + 1}
            </button>
          );
        } else if (p === currentPage - 2 || p === currentPage + 2) {
          return <span key={p} className="text-[#888888] px-1">...</span>;
        }
        return null;
      })}

      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage >= totalPages - 1}
        className="p-2 border border-[#E5E5E5] rounded-lg text-black hover:bg-[#F5F5F5] disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
        aria-label="Next Page"
      >
        <ChevronRight className="w-4 h-4" />
      </button>
    </div>
  );
};
