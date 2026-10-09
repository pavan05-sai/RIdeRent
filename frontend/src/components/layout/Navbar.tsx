import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Car, Heart, Bell, User as UserIcon, Menu, X, ArrowRight, Shield, Layers, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useWishlist } from '../../context/WishlistContext';
import { useCompare } from '../../context/CompareContext';
import { notificationService } from '../../services/notificationService';
import { NotificationItem } from '../../types';

export const Navbar: React.FC = () => {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const { wishlistCount } = useWishlist();
  const { compareList } = useCompare();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [notifDropdownOpen, setNotifDropdownOpen] = useState(false);
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);
  const [unreadCount, setUnreadCount] = useState<number>(0);

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (isAuthenticated) {
      const fetchNotifs = async () => {
        try {
          const count = await notificationService.getUnreadCount();
          setUnreadCount(count);
          if (notifDropdownOpen) {
            const pageData = await notificationService.getNotifications(0, 5);
            setNotifications(pageData.content);
          }
        } catch {
          // ignore
        }
      };
      fetchNotifs();
      const interval = setInterval(fetchNotifs, 30000);
      return () => clearInterval(interval);
    }
  }, [isAuthenticated, notifDropdownOpen]);

  const handleMarkAllRead = async () => {
    try {
      await notificationService.markAllAsRead();
      setUnreadCount(0);
      setNotifications((prev) => prev.map((n) => ({ ...n, isRead: true })));
    } catch {
      // ignore
    }
  };

  const navLinks: { label: string; path: string; badge?: number }[] = [
    { label: 'Home', path: '/' },
    { label: 'Vehicles', path: '/vehicles' },
  ];

  const isActive = (path: string) => location.pathname === path;

  return (
    <header className="sticky top-0 z-40 w-full bg-white/95 backdrop-blur-md border-b border-[#E5E5E5] transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20">
          {/* Logo & Brand */}
          <Link to="/" className="flex items-center space-x-3 group">
            <div className="w-10 h-10 bg-black text-white flex items-center justify-center rounded-lg group-hover:scale-105 transition-transform duration-200">
              <Car className="w-5 h-5 text-white" />
            </div>
            <div>
              <span className="text-xl font-bold tracking-tight text-black flex items-center gap-1.5">
                RideRent
                <span className="w-1.5 h-1.5 bg-black rounded-full inline-block"></span>
              </span>
              <span className="text-[10px] tracking-widest text-[#666666] uppercase block font-medium">
                Rent. Ride. Return.
              </span>
            </div>
          </Link>

          {/* Desktop Nav Links */}
          <nav className="hidden md:flex items-center space-x-8">
            {navLinks.map((link) => (
              <Link
                key={link.path}
                to={link.path}
                className={`text-sm font-medium transition-colors relative py-1 ${
                  isActive(link.path)
                    ? 'text-black font-semibold'
                    : 'text-[#666666] hover:text-black'
                }`}
              >
                {link.label}
                {link.badge !== undefined && (
                  <span className="ml-1.5 px-1.5 py-0.5 text-[10px] font-bold bg-black text-white rounded-full">
                    {link.badge}
                  </span>
                )}
                {isActive(link.path) && (
                  <span className="absolute bottom-0 left-0 w-full h-[2px] bg-black rounded-full" />
                )}
              </Link>
            ))}
          </nav>

          {/* Action Icons & User Menu */}
          <div className="hidden md:flex items-center space-x-4">
            {/* Wishlist Button */}
            <Link
              to={isAuthenticated ? '/dashboard/wishlist' : '/login'}
              className="p-2 text-[#444444] hover:text-black hover:bg-[#F5F5F5] rounded-lg transition-colors relative"
              title="Saved Wishlist"
            >
              <Heart className="w-5 h-5" />
              {wishlistCount > 0 && (
                <span className="absolute top-1 right-1 w-4 h-4 bg-black text-white text-[10px] font-bold rounded-full flex items-center justify-center">
                  {wishlistCount}
                </span>
              )}
            </Link>

            {/* Notification Dropdown */}
            {isAuthenticated && (
              <div className="relative">
                <button
                  onClick={() => setNotifDropdownOpen(!notifDropdownOpen)}
                  className="p-2 text-[#444444] hover:text-black hover:bg-[#F5F5F5] rounded-lg transition-colors relative"
                  title="Notifications"
                >
                  <Bell className="w-5 h-5" />
                  {unreadCount > 0 && (
                    <span className="absolute top-1 right-1 w-4 h-4 bg-black text-white text-[10px] font-bold rounded-full flex items-center justify-center">
                      {unreadCount}
                    </span>
                  )}
                </button>

                {notifDropdownOpen && (
                  <div className="absolute right-0 mt-2 w-80 bg-white border border-[#E5E5E5] rounded-xl shadow-xl py-3 z-50 animate-in fade-in zoom-in-95 duration-150">
                    <div className="flex items-center justify-between px-4 pb-2 border-b border-[#F0F0F0]">
                      <span className="text-xs font-bold text-black uppercase tracking-wider">Notifications</span>
                      {unreadCount > 0 && (
                        <button
                          onClick={handleMarkAllRead}
                          className="text-[11px] text-[#666666] hover:text-black underline"
                        >
                          Mark all as read
                        </button>
                      )}
                    </div>
                    <div className="max-h-72 overflow-y-auto divide-y divide-[#F5F5F5]">
                      {notifications.length === 0 ? (
                        <div className="py-6 text-center text-xs text-[#888888]">No notifications yet</div>
                      ) : (
                        notifications.map((n) => (
                          <div
                            key={n.id}
                            className={`p-3 text-left transition-colors ${
                              !n.isRead ? 'bg-[#FAFAFA]' : 'bg-white'
                            }`}
                          >
                            <p className="text-xs font-semibold text-black">{n.title}</p>
                            <p className="text-[11px] text-[#666666] mt-0.5">{n.message}</p>
                            <span className="text-[9px] text-[#999999] block mt-1">
                              {new Date(n.createdAt).toLocaleDateString()}
                            </span>
                          </div>
                        ))
                      )}
                    </div>
                    <div className="px-4 pt-2 border-t border-[#F0F0F0] text-center">
                      <Link
                        to="/dashboard/notifications"
                        onClick={() => setNotifDropdownOpen(false)}
                        className="text-xs font-medium text-black hover:underline"
                      >
                        View all notifications
                      </Link>
                    </div>
                  </div>
                )}
              </div>
            )}

            {/* Admin Badge/Link */}
            {isAdmin && (
              <Link
                to="/admin"
                className="px-3 py-1.5 text-xs font-semibold bg-[#111111] text-white rounded-md hover:bg-black transition-colors flex items-center space-x-1.5"
              >
                <Shield className="w-3.5 h-3.5 text-white" />
                <span>Admin Portal</span>
              </Link>
            )}

            {/* Auth CTA / Profile Dropdown */}
            {isAuthenticated ? (
              <div className="flex items-center space-x-2 pl-2 border-l border-[#E5E5E5]">
                <Link
                  to="/dashboard"
                  className="flex items-center space-x-2 p-1.5 pr-3 hover:bg-[#F5F5F5] rounded-lg transition-colors group"
                >
                  {user?.profilePhoto ? (
                    <img
                      src={user.profilePhoto}
                      alt={user.fullName}
                      className="w-8 h-8 rounded-full object-cover border border-[#E5E5E5]"
                    />
                  ) : (
                    <div className="w-8 h-8 rounded-full bg-black text-white text-xs font-bold flex items-center justify-center border border-black">
                      {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
                    </div>
                  )}
                  <div className="text-left hidden lg:block">
                    <span className="text-xs font-semibold text-black block leading-none">
                      {user?.fullName ? user.fullName.split(' ')[0] : 'Account'}
                    </span>
                    <span className="text-[10px] text-[#666666] leading-none">Dashboard</span>
                  </div>
                </Link>
                <button
                  onClick={() => {
                    logout();
                    navigate('/');
                  }}
                  className="p-2 text-[#666666] hover:text-black hover:bg-[#F5F5F5] rounded-lg transition-colors"
                  title="Sign Out"
                >
                  <LogOut className="w-4 h-4" />
                </button>
              </div>
            ) : (
              <div className="flex items-center space-x-3">
                <Link
                  to="/login"
                  className="text-sm font-medium text-black hover:text-[#555555] transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="px-4 py-2 text-sm font-semibold text-white bg-black rounded-lg hover:bg-[#222222] transition-colors shadow-subtle flex items-center space-x-1.5"
                >
                  <span>Get Started</span>
                  <ArrowRight className="w-4 h-4" />
                </Link>
              </div>
            )}
          </div>

          {/* Mobile Menu Button */}
          <div className="flex items-center md:hidden space-x-3">
            <Link
              to={isAuthenticated ? '/dashboard/wishlist' : '/login'}
              className="p-2 text-[#444444] relative"
            >
              <Heart className="w-5 h-5" />
              {wishlistCount > 0 && (
                <span className="absolute top-1 right-1 w-3.5 h-3.5 bg-black text-white text-[9px] font-bold rounded-full flex items-center justify-center">
                  {wishlistCount}
                </span>
              )}
            </Link>
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 text-black hover:bg-[#F5F5F5] rounded-lg"
            >
              {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu Dropdown */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-[#E5E5E5] bg-white px-4 pt-3 pb-6 space-y-3">
          <div className="space-y-1">
            {navLinks.map((link) => (
              <Link
                key={link.path}
                to={link.path}
                onClick={() => setMobileMenuOpen(false)}
                className={`block px-3 py-2 rounded-lg text-base font-medium ${
                  isActive(link.path) ? 'bg-[#F5F5F5] text-black font-semibold' : 'text-[#666666]'
                }`}
              >
                <div className="flex items-center justify-between">
                  <span>{link.label}</span>
                  {link.badge !== undefined && (
                    <span className="px-2 py-0.5 text-xs bg-black text-white rounded-full">
                      {link.badge}
                    </span>
                  )}
                </div>
              </Link>
            ))}
          </div>

          {isAdmin && (
            <div className="pt-2">
              <Link
                to="/admin"
                onClick={() => setMobileMenuOpen(false)}
                className="flex items-center space-x-2 px-3 py-2 bg-black text-white rounded-lg text-sm font-semibold"
              >
                <Shield className="w-4 h-4 text-white" />
                <span>Admin Management Portal</span>
              </Link>
            </div>
          )}

          <div className="pt-4 border-t border-[#E5E5E5]">
            {isAuthenticated ? (
              <div className="space-y-2">
                <Link
                  to="/dashboard"
                  onClick={() => setMobileMenuOpen(false)}
                  className="flex items-center space-x-3 px-3 py-2 rounded-lg bg-[#F8F8F8]"
                >
                  <UserIcon className="w-5 h-5 text-black" />
                  <div className="text-left">
                    <p className="text-sm font-bold text-black">{user?.fullName}</p>
                    <p className="text-xs text-[#666666]">View Customer Dashboard</p>
                  </div>
                </Link>
                <button
                  onClick={() => {
                    logout();
                    setMobileMenuOpen(false);
                    navigate('/');
                  }}
                  className="w-full text-left px-3 py-2 text-sm text-[#888888] hover:text-black flex items-center space-x-2"
                >
                  <LogOut className="w-4 h-4" />
                  <span>Sign Out</span>
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-2 gap-3">
                <Link
                  to="/login"
                  onClick={() => setMobileMenuOpen(false)}
                  className="px-4 py-2.5 text-center text-sm font-semibold border border-black text-black rounded-lg"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  onClick={() => setMobileMenuOpen(false)}
                  className="px-4 py-2.5 text-center text-sm font-semibold bg-black text-white rounded-lg"
                >
                  Register
                </Link>
              </div>
            )}
          </div>
        </div>
      )}
    </header>
  );
};
