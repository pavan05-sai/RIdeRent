import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Car, Mail, ArrowRight, ArrowLeft, CheckCircle2 } from 'lucide-react';
import { authService } from '../../services/authService';
import { useToast } from '../../context/ToastContext';
import { Button } from '../../components/common/Button';

export const ForgotPassword: React.FC = () => {
  const [email, setEmail] = useState('');
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const { error } = useToast();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) return;

    setIsLoading(true);
    try {
      await authService.forgotPassword(email.trim());
      setIsSubmitted(true);
    } catch (err: any) {
      error(err.response?.data?.message || 'Email not found in our records');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-white flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md text-center space-y-3">
        <Link to="/" className="inline-flex items-center space-x-2.5">
          <div className="w-10 h-10 bg-black text-white flex items-center justify-center rounded-xl">
            <Car className="w-5 h-5 text-white" />
          </div>
          <span className="text-2xl font-bold tracking-tight text-black">RideRent</span>
        </Link>
        <h2 className="text-2xl font-black text-black tracking-tight">Password Reset</h2>
        <p className="text-xs text-[#666666]">
          Enter your registered email to receive access recovery instructions.
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md px-4">
        <div className="bg-[#FAFAFA] py-8 px-6 sm:px-10 border border-[#E5E5E5] rounded-2xl shadow-subtle space-y-6">
          {isSubmitted ? (
            <div className="text-center py-6 space-y-4">
              <div className="w-12 h-12 bg-black text-white rounded-full flex items-center justify-center mx-auto">
                <CheckCircle2 className="w-6 h-6 text-white" />
              </div>
              <h3 className="text-base font-bold text-black">Recovery Email Dispatched</h3>
              <p className="text-xs text-[#666666] leading-relaxed">
                If an account exists for <strong className="text-black">{email}</strong>, you will receive password reset instructions shortly.
              </p>
              <div className="pt-4">
                <Link to="/login">
                  <Button variant="primary" size="sm" className="w-full">
                    Return to Login
                  </Button>
                </Link>
              </div>
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="text-xs font-bold text-black block mb-1">Email Address</label>
                <div className="relative">
                  <Mail className="w-4 h-4 text-[#888888] absolute left-3 top-3" />
                  <input
                    type="email"
                    required
                    placeholder="name@example.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black font-medium"
                  />
                </div>
              </div>

              <Button
                type="submit"
                variant="primary"
                size="md"
                className="w-full"
                isLoading={isLoading}
              >
                <span>Transmit Reset Link</span>
                <ArrowRight className="w-4 h-4 ml-1" />
              </Button>

              <div className="pt-2 text-center">
                <Link to="/login" className="text-xs text-[#666666] hover:text-black flex items-center justify-center space-x-1">
                  <ArrowLeft className="w-3.5 h-3.5" />
                  <span>Back to Sign In</span>
                </Link>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};
