import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Car, Lock, Mail, User, Phone, ArrowRight } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useToast } from '../../context/ToastContext';
import { Button } from '../../components/common/Button';

export const Register: React.FC = () => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const { register } = useAuth();
  const { success, error } = useToast();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      error('Passwords do not match');
      return;
    }
    if (password.length < 8) {
      error('Password must be at least 8 characters long');
      return;
    }

    setIsLoading(true);
    try {
      await register(fullName.trim(), email.trim(), phone.trim(), password);
      success('Account created successfully! Welcome to RideRent.');
      navigate('/dashboard');
    } catch (err: any) {
      error(err.response?.data?.message || 'Registration failed. Please check your credentials.');
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
        <h2 className="text-2xl font-black text-black tracking-tight">Create Customer Profile</h2>
        <p className="text-xs text-[#666666]">
          Join RideRent to browse, reserve, and manage premium vehicle rentals.
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md px-4">
        <div className="bg-[#FAFAFA] py-8 px-6 sm:px-10 border border-[#E5E5E5] rounded-2xl shadow-subtle space-y-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="text-xs font-bold text-black block mb-1">Full Legal Name</label>
              <div className="relative">
                <User className="w-4 h-4 text-[#888888] absolute left-3 top-3" />
                <input
                  type="text"
                  required
                  placeholder="Johnathan Doe"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black font-medium"
                />
              </div>
            </div>

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
              <label className="text-xs font-bold text-black block mb-1">Contact Phone</label>
              <div className="relative">
                <Phone className="w-4 h-4 text-[#888888] absolute left-3 top-3" />
                <input
                  type="tel"
                  required
                  placeholder="Enter phone number"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black font-medium"
                />
              </div>
            </div>

            <div>
              <label className="text-xs font-bold text-black block mb-1">Password</label>
              <div className="relative">
                <Lock className="w-4 h-4 text-[#888888] absolute left-3 top-3" />
                <input
                  type="password"
                  required
                  placeholder="At least 8 characters"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black font-medium"
                />
              </div>
            </div>

            <div>
              <label className="text-xs font-bold text-black block mb-1">Confirm Password</label>
              <div className="relative">
                <Lock className="w-4 h-4 text-[#888888] absolute left-3 top-3" />
                <input
                  type="password"
                  required
                  placeholder="Re-enter password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black font-medium"
                />
              </div>
            </div>

            <Button
              type="submit"
              variant="primary"
              size="md"
              className="w-full pt-1"
              isLoading={isLoading}
            >
              <span>Create Account</span>
              <ArrowRight className="w-4 h-4 ml-1" />
            </Button>
          </form>

          <div className="pt-4 border-t border-[#E5E5E5] text-center text-xs text-[#666666]">
            Already have an account?{' '}
            <Link to="/login" className="font-bold text-black hover:underline">
              Sign In
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
