import React, { useState } from 'react';
import { Send, CheckCircle2 } from 'lucide-react';
import { Button } from '../../components/common/Button';
import { useToast } from '../../context/ToastContext';

export const Contact: React.FC = () => {
  const { success } = useToast();
  const [submitted, setSubmitted] = useState(false);
  const [formData, setFormData] = useState({ name: '', email: '', subject: '', message: '' });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitted(true);
    success('Message dispatched to RideRent support.');
  };

  return (
    <div className="min-h-screen bg-white">
      <div className="bg-[#FBFBFB] border-b border-[#E5E5E5] py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center space-y-3">
          <span className="text-xs uppercase font-bold tracking-widest text-[#666666] block">
            Customer Support
          </span>
          <h1 className="text-4xl sm:text-5xl font-black text-black tracking-tight">
            Contact RideRent
          </h1>
          <p className="text-base text-[#666666]">
            Have a question or rental inquiry? Send our team a message below.
          </p>
        </div>
      </div>

      <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        {/* Inquiry Form */}
        <div className="bg-[#FAFAFA] border border-[#E5E5E5] rounded-2xl p-8">
          {submitted ? (
            <div className="text-center py-12 space-y-4">
              <div className="w-12 h-12 bg-black text-white rounded-full flex items-center justify-center mx-auto">
                <CheckCircle2 className="w-6 h-6 text-white" />
              </div>
              <h3 className="text-lg font-bold text-black">Message Received</h3>
              <p className="text-xs text-[#666666] max-w-xs mx-auto">
                Thank you for reaching out. We will review your inquiry and respond shortly.
              </p>
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="text-xs font-bold text-black block mb-1">Your Full Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>

              <div>
                <label className="text-xs font-bold text-black block mb-1">Email Address</label>
                <input
                  type="email"
                  required
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>

              <div>
                <label className="text-xs font-bold text-black block mb-1">Subject</label>
                <input
                  type="text"
                  required
                  value={formData.subject}
                  onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                  className="w-full px-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>

              <div>
                <label className="text-xs font-bold text-black block mb-1">Message</label>
                <textarea
                  rows={4}
                  required
                  value={formData.message}
                  onChange={(e) => setFormData({ ...formData, message: e.target.value })}
                  className="w-full px-3 py-2 text-sm bg-white border border-[#E5E5E5] rounded-lg focus:outline-none focus:border-black"
                />
              </div>

              <Button type="submit" variant="primary" size="md" className="w-full">
                <Send className="w-4 h-4 mr-2" />
                <span>Send Message</span>
              </Button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};
