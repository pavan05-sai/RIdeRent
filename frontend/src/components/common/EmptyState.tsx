import React from 'react';
import { LucideIcon } from 'lucide-react';
import { Button } from './Button';

interface EmptyStateProps {
  icon: LucideIcon;
  title: string;
  description: string;
  actionText?: string;
  onAction?: () => void;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  icon: Icon,
  title,
  description,
  actionText,
  onAction,
}) => {
  return (
    <div className="flex flex-col items-center justify-center p-12 text-center bg-[#FBFBFB] border border-dashed border-[#DDDDDD] rounded-2xl my-6">
      <div className="w-14 h-14 bg-white border border-[#E5E5E5] rounded-full flex items-center justify-center shadow-subtle mb-4">
        <Icon className="w-6 h-6 text-black" />
      </div>
      <h3 className="text-base font-bold text-black tracking-tight">{title}</h3>
      <p className="text-sm text-[#666666] max-w-sm mt-1 mb-6 leading-relaxed">{description}</p>
      {actionText && onAction && (
        <Button onClick={onAction} variant="primary" size="sm">
          {actionText}
        </Button>
      )}
    </div>
  );
};
