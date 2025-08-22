// src/context/AuthContext.ts
import { createContext } from "react";

export interface AuthContextType {
  user: string | null;
  token: string | null;
  login: (username: string, token: string) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
