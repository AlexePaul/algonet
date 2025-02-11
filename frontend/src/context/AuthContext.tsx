import { createContext, useState, useEffect, ReactNode } from 'react';
import { user } from '../types';
import { getUser } from '../services/authService.tsx';

// Define the context type
interface AuthContextType {
  user: user | null;
  token: string | null;
  setToken: (token: string | null) => void;
}

// Create context with default values
export const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem('token')
  );
  const [user, setUser] = useState<user | null>(null);

  async function getAuthUser() {
    if (!token) {
      return;
    }
    try {
      var data = await getUser(token);
      setUser(data);
    } catch (err) {
      console.error(err);
    }
  }

  useEffect(() => {
    if (token) {
      getAuthUser();
    } else {
      setUser(null);
    }
  }, [token]);

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    setToken(storedToken);
  }, []);

  const updateToken = (newToken: string | null) => {
    if (newToken) {
      localStorage.setItem('token', newToken);
      setToken(newToken);
    } else {
      localStorage.removeItem('token');
      setToken(newToken);
    }
  };

  return (
    <AuthContext.Provider value={{ user, token, setToken: updateToken }}>
      {children}
    </AuthContext.Provider>
  );
}
