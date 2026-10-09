import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Car, Lock, Mail, ArrowRight } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useToast } from '../../context/ToastContext';
import { Button } from '../../components/common/Button';

export const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const { login } = useAuth();
  const { success, error } = useToast();
  const navigate = useNavigate();
  const location = useLocation();

  const from = (location.state as any)?.from?.pathname || '/dashboard';

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) return;

    setIsLoading(true);
    try {
      await login(email.trim(), password);
      success('Authentication successful! Welcome back.');
      if (email.toLowerCase().includes('admin')) {
        navigate('/admin');
      } else {
        navigate(from, { replace: true });
      }
    } catch (err: any) {
      error(err.response?.data?.message || 'Invalid email or password. Please try again.');
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
        <h2 className="text-2xl font-black text-black tracking-tight">Access Your Account</h2>
        <p className="text-xs text-[#666666]">
          Rent, extend, and review premium vehicles in your personalized dashboard.
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md px-4">
        <div className="bg-[#FAFAFA] py-8 px-6 sm:px-10 border border-[#E5E5E5] rounded-2xl shadow-subtle space-y-6">
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

            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="text-xs font-bold text-black">Password</label>
                <Link
                  to="/forgot-password"
                  className="text-xs text-[#666666] hover:text-black transition-colors font-medium"
                >
                  Forgot password?
                </Link>
              </div>
              <div className="relative">
                <Lock className="w-4 h-4 text-[#888888] absolute left-3 top-3" />
                <input
                  type="password"
                  required
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
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
              <span>Sign In</span>
              <ArrowRight className="w-4 h-4 ml-1" />
            </Button>
          </form>

          <div className="pt-4 border-t border-[#E5E5E5] text-center text-xs text-[#666666]">
            Don't have an account?{' '}
            <Link to="/register" className="font-bold text-black hover:underline">
              Create an account
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
