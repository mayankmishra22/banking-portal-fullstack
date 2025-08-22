// src/context/AuthProvider.tsx
import React, { useState } from "react";
import { AuthContext } from "./AuthContext";

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<string | null>(null);
  const [token, setToken] = useState<string | null>(null);

  const login = (username: string, token: string) => {
    console.log("Login called with:", { username, token });
    setUser(username);
    setToken(token);
    localStorage.setItem("accessToken", token);
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem("accessToken");
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
