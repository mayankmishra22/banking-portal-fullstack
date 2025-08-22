import React, { useMemo, useState } from "react";
import { toast } from "react-toastify";
import api from "../api/axios";
import { useAuth } from "../hooks/useAuth";

interface RegisterFormProps {
  switchToLogin: () => void;
}

const emailOk = (v: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v.trim());
const mobileOk = (v: string) => /^[0-9]{10,15}$/.test(v.replace(/\D/g, ""));

const passwordScore = (v: string) => {
  let score = 0;
  if (v.length >= 8) score++;
  if (/[A-Z]/.test(v)) score++;
  if (/[a-z]/.test(v)) score++;
  if (/[0-9]/.test(v)) score++;
  if (/[^A-Za-z0-9]/.test(v)) score++;
  return Math.min(score, 4);
};

const scoreMeta = (score: number) => {
  switch (score) {
    case 0:
    case 1:
      return { label: "Weak", className: "bg-danger", width: "25%" };
    case 2:
      return { label: "Fair", className: "bg-warning", width: "50%" };
    case 3:
      return { label: "Good", className: "bg-info", width: "75%" };
    case 4:
      return { label: "Strong", className: "bg-success", width: "100%" };
    default:
      return { label: "Weak", className: "bg-danger", width: "25%" };
  }
};

const RegisterForm: React.FC<RegisterFormProps> = ({ switchToLogin }) => {
  const { login } = useAuth();
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [mobile, setMobile] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [acceptTerms, setAcceptTerms] = useState(false);

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const pwdScore = useMemo(
    () => scoreMeta(passwordScore(password)),
    [password]
  );

  const validate = () => {
    if (!fullName.trim()) {
      toast.error("Full name is required!");
      return false;
    }
    if (!emailOk(email)) {
      toast.error("Enter a valid email address!");
      return false;
    }
    if (!mobileOk(mobile)) {
      toast.error("Enter a valid mobile number (10–15 digits)!");
      return false;
    }
    if (passwordScore(password) < 2) {
      toast.error(
        "Password too weak! Use 8+ chars incl. upper, lower, number & symbol."
      );
      return false;
    }
    if (confirmPassword !== password) {
      toast.error("Passwords do not match!");
      return false;
    }
    if (!acceptTerms) {
      toast.error("You must accept Terms & Conditions!");
      return false;
    }
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const response = await api.post("/auth/register", {
        username : fullName,
        email,
        password
      }); 
      login(email, response.data.accessToken);
      toast.success("Registered successfully! Redirecting to login...");
      setTimeout(() => {
        switchToLogin();
      }, 2000);
    } catch (err) {
      toast.error(
        "Something went wrong. Please try again." +
          (err instanceof Error ? `: ${err.message}` : "")
      );
      setPassword("");
      setConfirmPassword("");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <h3 className="mb-4 fw-bold text-center">Create Your Account</h3>
      <form onSubmit={handleSubmit} noValidate>
        {/* Full Name */}
        <div className="form-floating-custom">
          <input
            type="text"
            id="registerName"
            placeholder=" "
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            className="form-control"
          />
          <label htmlFor="registerName">Full Name</label>
        </div>

        {/* Email */}
        <div className="form-floating-custom">
          <input
            type="email"
            id="registerEmail"
            placeholder=" "
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="form-control"
          />
          <label htmlFor="registerEmail">Email Address</label>
        </div>

        {/* Mobile */}
        <div className="form-floating-custom">
          <input
            type="tel"
            id="registerMobile"
            placeholder=" "
            value={mobile}
            onChange={(e) => setMobile(e.target.value)}
            className="form-control"
          />
          <label htmlFor="registerMobile">Mobile Number</label>
        </div>

        {/* Password */}
        <div className="form-floating-custom position-relative">
          <input
            type={showPassword ? "text" : "password"}
            id="registerPassword"
            placeholder=" "
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="form-control pe-5"
          />
          <label htmlFor="registerPassword">Password</label>

          {/* Eye icon */}
          <span
            className="password-toggle"
            onClick={() => setShowPassword((s) => !s)}
          >
            <i className={`bi ${showPassword ? "bi-eye-slash-fill" : "bi-eye-fill"}`} />
          </span>

          {/* Strength meter */}
          {password.length > 0 && (
            <div className="mt-2">
              <div className="d-flex justify-content-between align-items-center small mb-1">
                <span>Password strength</span>
                <span className="fw-semibold">{pwdScore.label}</span>
              </div>
              <div className="progress" style={{ height: 6 }}>
                <div
                  className={`progress-bar ${pwdScore.className}`}
                  style={{
                    width: pwdScore.width,
                    transition: "width 0.3s ease",
                  }}
                />
              </div>
            </div>
          )}
        </div>

        {/* Confirm Password */}
        <div className="form-floating-custom position-relative">
          <input
            type={showConfirm ? "text" : "password"}
            id="registerConfirmPassword"
            placeholder=" "
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="form-control pe-5"
          />
          <label htmlFor="registerConfirmPassword">Confirm Password</label>

          {/* Eye icon */}
          <span
            className="password-toggle"
            onClick={() => setShowConfirm((s) => !s)}
          >
            <i className={`bi ${showConfirm ? "bi-eye-slash-fill" : "bi-eye-fill"}`} />
          </span>
        </div>

        {/* Terms */}
        <div className="form-check mb-3">
          <input
            className="form-check-input"
            type="checkbox"
            id="registerTerms"
            checked={acceptTerms}
            onChange={(e) => setAcceptTerms(e.target.checked)}
          />
          <label className="form-check-label" htmlFor="registerTerms">
            I agree to the <a href="#">Terms & Conditions</a>
          </label>
        </div>

        <button
          type="submit"
          className="btn btn-success w-100 py-2 rounded-3 mt-1"
          disabled={submitting}
        >
          {submitting ? (
            <>
              <span
                className="spinner-border spinner-border-sm me-2"
                role="status"
              />
              Registering...
            </>
          ) : (
            "Register"
          )}
        </button>
      </form>

      <div className="text-center mt-3">
        <p className="mb-0">
          Already have an account?{" "}
          <span
            className="switch-link text-primary fw-bold"
            onClick={switchToLogin}
            role="button"
          >
            Login here
          </span>
        </p>
      </div>
    </>
  );
};

export default RegisterForm;
