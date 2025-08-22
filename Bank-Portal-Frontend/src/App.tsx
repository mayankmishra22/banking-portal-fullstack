import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "react-toastify/dist/ReactToastify.css";
import "./App.css"; // <-- keep your custom dark theme styles last

import { ToastContainer } from "react-toastify";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import { AuthProvider } from "./context/AuthProvider";
import AuthPage from "./components/AuthPage"; // Login/Register
import DashboardLayout from "./components/DashboardLayout";
import DashboardHome from "./components/DashboardHome";
import CreateAccount from "./components/CreateAccount";
import TransferFunds from "./components/TransferFunds";
import TransactionHistory from "./components/TransactionHistory";
import ProtectedRoute from "./components/ProtectedRoute";
import ShowAccounts from "./components/ShowAccounts";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Auth page handles both login and register */}
          <Route path="/auth" element={<AuthPage />} />

          {/* Protected dashboard with nested routes */}
          <Route
            path="/dashboard/*"
            element={
              <ProtectedRoute>
                <DashboardLayout />
              </ProtectedRoute>
            }
          >
            <Route path="home" element={<DashboardHome />} />
            <Route path="create-account" element={<CreateAccount />} />
            <Route path="fetch-accounts" element={<ShowAccounts />} />
            <Route path="transfer-funds" element={<TransferFunds />} />
            <Route path="transactions" element={<TransactionHistory />} />
          </Route>

          {/* Redirect all unknown routes to AuthPage */}
          <Route path="*" element={<AuthPage />} />
        </Routes>
      </BrowserRouter>

      {/* Toast notifications */}
      <ToastContainer position="top-right" autoClose={2000} />
    </AuthProvider>
  );
}

export default App;
