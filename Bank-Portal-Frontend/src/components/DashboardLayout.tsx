import { useState } from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { FaBars, FaTimes, FaHome, FaWallet, FaPlusCircle, FaExchangeAlt, FaListAlt } from "react-icons/fa";
import "../styles/DashboardLayout.css";

const navItems = [
  { name: "Home", path: "/dashboard/home", icon: <FaHome /> },
  { name: "Accounts", path: "/dashboard/fetch-accounts", icon: <FaWallet /> },
  { name: "Create Account", path: "/dashboard/create-account", icon: <FaPlusCircle /> },
  { name: "Transfer Funds", path: "/dashboard/transfer-funds", icon: <FaExchangeAlt /> },
  { name: "Transactions", path: "/dashboard/transactions", icon: <FaListAlt /> },
];

const DashboardLayout = () => {
  const { logout } = useAuth();
  const [open, setOpen] = useState(false);

  return (
    <div className="d-flex vh-100">
      {/* Sidebar */}
      <aside
        className={`sidebar glassy-sidebar p-3 flex-column position-fixed h-100 ${
          open ? "open" : ""
        }`}
        style={{ width: "250px", zIndex: 1050 }}
      >
        {/* Close button (mobile only) */}
        <div className="d-md-none text-end mb-3">
          <button
            className="btn btn-outline-light btn-sm"
            onClick={() => setOpen(false)}
          >
            <FaTimes />
          </button>
        </div>

        <h3 className="text-center fw-bold brand mb-4">🏦 BankingPortal</h3>

        <nav className="flex-grow-1">
          {navItems.map((item) => (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) =>
                `d-flex align-items-center gap-2 py-2 px-3 rounded mb-2 text-decoration-none nav-item-link ${
                  isActive ? "active-link" : ""
                }`
              }
              onClick={() => setOpen(false)}
            >
              <span className="nav-icon">{item.icon}</span>
              <span>{item.name}</span>
            </NavLink>
          ))}
        </nav>

        <button className="logout-btn mt-auto w-100" onClick={logout}>
          Logout
        </button>
      </aside>

      {/* Backdrop (mobile only) */}
      {open && (
        <div
          className="sidebar-backdrop d-md-none"
          onClick={() => setOpen(false)}
        ></div>
      )}

      {/* Main */}
      <main
        className="flex-grow-1 bg-light overflow-auto"
        style={{ marginLeft: "250px" }}
      >
        {/* Topbar (mobile only) */}
        <div className="d-flex align-items-center justify-content-between shadow-sm px-3 py-2 bg-white d-md-none">
          <button
            className="btn btn-outline-secondary"
            onClick={() => setOpen(true)}
          >
            <FaBars />
          </button>
          <span className="fw-bold text-primary">Dashboard</span>
        </div>

        <div className="p-4">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default DashboardLayout;
