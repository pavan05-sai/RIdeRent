import React, { createContext, useContext, useState, useCallback } from 'react';
import { CheckCircle2, AlertCircle, Info, X } from 'lucide-react';

type ToastType = 'success' | 'error' | 'info';

interface Toast {
  id: string;
  type: ToastType;
  message: string;
}

interface ToastContextType {
  showToast: (first: string, second?: ToastType | string) => void;
  success: (message: string) => void;
  error: (message: string) => void;
  info: (message: string) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const ToastProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [toasts, setToasts] = useState<Toast[]>([]);

  const removeToast = useCallback((id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const showToast = useCallback((first: string, second?: ToastType | string) => {
    let message = first;
    let type: ToastType = 'info';

    if (second === 'success' || second === 'error' || second === 'info') {
      type = second;
    } else if (first === 'success' || first === 'error' || first === 'info') {
      type = first as ToastType;
      message = (second as string) || '';
    }

    const id = Math.random().toString(36).substring(2, 9);
    setToasts((prev) => [...prev, { id, type, message }]);

    setTimeout(() => {
      removeToast(id);
    }, 4000);
  }, [removeToast]);

  const success = useCallback((message: string) => showToast(message, 'success'), [showToast]);
  const error = useCallback((message: string) => showToast(message, 'error'), [showToast]);
  const info = useCallback((message: string) => showToast(message, 'info'), [showToast]);

  return (
    <ToastContext.Provider value={{ showToast, success, error, info }}>
      {children}
      <div className="fixed bottom-6 right-6 z-50 flex flex-col space-y-3 pointer-events-none max-w-md w-full px-4">
        {toasts.map((toast) => (
          <div
            key={toast.id}
            className="pointer-events-auto flex items-center justify-between p-4 bg-[#111111] text-white border border-[#333333] rounded-lg shadow-xl animate-in fade-in slide-in-from-bottom-5 duration-200"
          >
            <div className="flex items-center space-x-3 pr-2">
              {toast.type === 'success' && <CheckCircle2 className="w-5 h-5 text-white shrink-0" />}
              {toast.type === 'error' && <AlertCircle className="w-5 h-5 text-white shrink-0" />}
              {toast.type === 'info' && <Info className="w-5 h-5 text-white shrink-0" />}
              <span className="text-sm font-medium leading-tight">{toast.message}</span>
            </div>
            <button
              onClick={() => removeToast(toast.id)}
              className="p-1 hover:bg-[#222222] rounded text-neutral-400 hover:text-white transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};
