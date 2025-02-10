import { createContext, useState, useEffect, ReactNode } from "react";
import { user } from "../types";

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
    localStorage.getItem("token")
  );
  const [user, setUser] = useState<user | null>(null);

  async function getAuthUser() {
    if (!token) {
      return;
    }
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        credentials: "include",
      });
      const data = await response.json();
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
    const storedToken = localStorage.getItem("token");
    setToken(storedToken);
  }, []);

  const updateToken = (newToken: string | null) => {
    if (newToken) {
      localStorage.setItem("token", newToken);
      setToken(newToken);
    } else {
      localStorage.removeItem("token");
      setToken(newToken);
    }
  };

  return (
    <AuthContext.Provider value={{ user, token, setToken: updateToken }}>
      {children}
    </AuthContext.Provider>
  );
}
