/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: '#FFFFFF',
        surface: '#F8F8F8',
        'surface-subtle': '#F2F2F2',
        border: '#E5E5E5',
        'border-dark': '#222222',
        primary: {
          DEFAULT: '#000000',
          hover: '#1F1F1F',
          light: '#F5F5F5',
        },
        dark: {
          DEFAULT: '#111111',
          card: '#181818',
          subtle: '#222222',
        },
        muted: {
          DEFAULT: '#666666',
          light: '#888888',
          dark: '#444444',
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'sans-serif'],
      },
      boxShadow: {
        'subtle': '0 1px 3px 0 rgba(0, 0, 0, 0.05), 0 1px 2px -1px rgba(0, 0, 0, 0.05)',
        'premium': '0 4px 20px -2px rgba(0, 0, 0, 0.08)',
        'hover': '0 10px 30px -5px rgba(0, 0, 0, 0.12)',
      },
      borderRadius: {
        'card': '12px',
        'button': '8px',
      }
    },
  },
  plugins: [],
}
