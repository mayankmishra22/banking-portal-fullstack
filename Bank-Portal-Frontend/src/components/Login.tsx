import React, { useState } from "react";
import api from "../api/axios";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

interface LoginFormProps {
  switchToRegister: () => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ switchToRegister }) => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await api.post("/auth/login", { username, password });
      console.log(response.data);
      const { accessToken } = response.data;
      login(username, accessToken);
      toast.success("Login successfully! Redirecting to Dashboard...");
      navigate("/dashboard/home");
    } catch (err) {
      console.error(err);
      const status = err.status, message = err.message;
      if (status && message) {
        switch (status) {
          case 401:
            toast.error("Unauthorized: Invalid credentials");
            break;
          case 404:
            toast.error(`Not Found: ${message}`);
            break;
          case 400:
            toast.error(`Bad Request: ${message}`);
            break;
          default:
            toast.error(`Error: ${message || "Something went wrong"}`);
        }
      } else {
        toast.error("Login failed: Unable to reach server");
      }
    }
  };

  return (
    <>
      <h3 className="mb-4 fw-bold text-center">lesssgo again {" "}</h3>
      <form onSubmit={handleSubmit}>
        {/* Email */}
        <div className="form-floating-custom">
          <input
            type="text"
            id="loginEmail"
            placeholder=" "
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="form-control"
          />
          <label htmlFor="loginEmail">Email Address / Username</label>
        </div>

        {/* Password */}
        <div className="form-floating-custom position-relative">
          <input
            type={showPassword ? "text" : "password"}
            id="loginPassword"
            placeholder=" "
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="form-control pe-5"
          />
          <label htmlFor="loginPassword">Password</label>

          <button
            type="button"
            className="btn btn-sm position-absolute end-0 top-50 translate-middle-y me-2"
            onClick={() => setShowPassword(!showPassword)}
            style={{ border: "none", background: "transparent" }}
          >
            <i className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`}></i>
          </button>
        </div>

        <button
          type="submit"
          className="btn btn-primary w-100 py-2 rounded-3 mt-3"
        >
          Login
        </button>
      </form>

      <div className="text-center mt-3">
        <p>
          Don’t have an account?{" "}
          <span
            className="switch-link text-primary fw-bold"
            onClick={switchToRegister}
          >
            Register here
          </span>
        </p>
      </div>
    </>
  );
};

export default LoginForm;
