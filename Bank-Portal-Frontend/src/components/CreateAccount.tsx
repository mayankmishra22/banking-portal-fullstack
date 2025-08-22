// src/components/dashboard/CreateAccount.tsx
import React, { useState } from "react";
import { toast } from "react-toastify";
import { motion } from "framer-motion";
import api from "../api/axios";
import "../styles/CreateAccount.css"; 

const CreateAccount: React.FC = () => {
  const [accountNumber, setAccountNumber] = useState("");
  const [accountType, setAccountType] = useState("SAVINGS");
  const [balance, setBalance] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await api.post("/accounts/create", {
        accountNumber,
        balance,
        type: accountType,
      });
      toast.success(`Account created! ID: ${response.data.id}`);
      setAccountNumber("");
      setBalance("");
      setAccountType("SAVINGS");
    } catch (err: any) {
      toast.error("Failed to create account - " + err.message);
    }
  };

  return (
    <motion.div
      className="create-account-card"
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
    >
      <h2 className="title">Create New Account</h2>
      <form onSubmit={handleSubmit} className="form">

        {/* Floating Label Input */}
        <div className={`form-group floating ${accountNumber ? "has-value" : ""}`}>
          <input
            type="number"
            value={accountNumber}
            onChange={(e) => setAccountNumber(e.target.value)}
            required
          />
          <label>Account Number</label>
        </div>

        <div className={`form-group floating ${accountType ? "has-value" : ""}`}>
          <select
            value={accountType}
            onChange={(e) => setAccountType(e.target.value)}
            required
          >
            <option value="SAVINGS">Savings</option>
            <option value="CURRENT">Current</option>
            <option value="FIXED_DEPOSIT">Fixed Deposit</option>
          </select>
          <label>Account Type</label>
        </div>

        <div className={`form-group floating ${balance ? "has-value" : ""}`}>
          <input
            type="number"
            value={balance}
            onChange={(e) => setBalance(e.target.value)}
            required
          />
          <label>Initial Deposit</label>
        </div>

        <motion.button
          type="submit"
          className="submit-btn"
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
        >
          Create Account
        </motion.button>
      </form>
    </motion.div>
  );
};

export default CreateAccount;
