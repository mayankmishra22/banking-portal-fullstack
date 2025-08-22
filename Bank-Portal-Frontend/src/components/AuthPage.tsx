import React, { useState } from "react";
import LoginForm from "./Login";
import RegisterForm from "./Register";
import "../styles/AuthPage.css";

const AuthPage: React.FC = () => {
  const [mode, setMode] = useState<"login" | "register">("login");

  return (
    <div className="auth-container">
      <div className="auth-card">
        {/* Left Panel */}
        <div className="auth-left">
          <h2 className="bank-title">BankingPortal</h2>
          <p></p>
        </div>

        {/* Right Panel */}
        <div className="auth-right">
          {mode === "login" ? (
            <LoginForm switchToRegister={() => setMode("register")} />
          ) : (
            <RegisterForm switchToLogin={() => setMode("login")} />
          )}
        </div>
      </div>
    </div>
  );
};

export default AuthPage;
