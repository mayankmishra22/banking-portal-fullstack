import React from "react";
import { motion } from "framer-motion";
import { useAuth } from "../hooks/useAuth";
import { Link } from "react-router-dom";
import {
  FaWallet,
  FaExchangeAlt,
  FaPiggyBank,
  FaHistory,
} from "react-icons/fa";

const features = [
  {
    title: "Manage Accounts",
    desc: "View and manage all your bank accounts in one place.",
    icon: <FaWallet size={28} />,
    color: "bg-primary",
    route: "/fetch-accounts",
  },
  {
    title: "Transfer Funds",
    desc: "Send money securely and instantly between accounts.",
    icon: <FaExchangeAlt size={28} />,
    color: "bg-success",
    route: "/transfer-funds",
  },
  {
    title: "Savings & Investments",
    desc: "Grow your wealth with smart saving options.",
    icon: <FaPiggyBank size={28} />,
    color: "bg-warning",
    route: "/create-account",
  },
  {
    title: "Transaction History",
    desc: "Track all your financial activities with ease.",
    icon: <FaHistory size={28} />,
    color: "bg-info",
    route: "/transactions",
  },
];

const DashboardHome: React.FC = () => {
  const { user } = useAuth();

  return (
    <div className="dashboard-home container py-5">
      {/* Animated welcome text */}
      <motion.div
        className="text-center mb-5"
        initial={{ opacity: 0, y: -30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <h2 className="fw-bold mb-3 gradient-text">
          {user}
        </h2>
        <p className="lead text-muted">
          all your needs compiled at one place for convenience.
        </p>
      </motion.div>

      {/* Feature cards */}
      <div className="row g-4">
        {features.map((f, idx) => (
          <motion.div
            key={idx}
            className="col-12 col-md-6 col-lg-3"
            initial={{ opacity: 0, y: 40 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: idx * 0.2 }}
          >
            <Link to={"/dashboard"+f.route} className="text-decoration-none text-dark">
              <div className="card shadow-sm h-100 border-0 rounded-4 hover-card">
                <div className="card-body text-center p-4">
                  <div
                    className={`icon-wrapper d-inline-flex align-items-center justify-content-center mb-3 text-white rounded-circle ${f.color}`}
                    style={{ width: "60px", height: "60px" }}
                  >
                    {f.icon}
                  </div>
                  <h5 className="fw-bold">{f.title}</h5>
                  <p className="text-muted small">{f.desc}</p>
                </div>
              </div>
            </Link>
          </motion.div>
        ))}
      </div>

      <style>{`
        .gradient-text {
          background: linear-gradient(90deg, #007bff, #00c6ff);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
        .hover-card {
          transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .hover-card:hover {
          transform: translateY(-8px);
          box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
      `}</style>
    </div>
  );
};

export default DashboardHome;
