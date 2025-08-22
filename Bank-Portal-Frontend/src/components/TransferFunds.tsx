// src/components/dashboard/TransferFunds.tsx
import React, { useState } from "react";
import { toast } from "react-toastify";
import api from "../api/axios";
import "../styles/TransferFunds.css";

const TransferFunds: React.FC = () => {
  const [fromAccount, setFromAccount] = useState("");
  const [toAccount, setToAccount] = useState("");
  const [amount, setAmount] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post("/transactions/transfer", {
        fromAccountId : Number(fromAccount),
        toAccountId : Number(toAccount),
        amount: Number(amount),
      });
      toast.success("Funds transferred successfully!");
      setFromAccount("");
      setToAccount("");
      setAmount("");
    } catch (err: any) {
      console.log(err)
      toast.error("Transfer failed : " + err.message);
    }
  };

  return (
    <div className="card shadow-sm transfer-card p-4">
      <h3 className="mb-4 transfer-title text-success">Transfer Funds</h3>
      <form onSubmit={handleSubmit} className="transfer-form">
        <div className="form-group floating mb-3">
          <input
            type="text"
            value={fromAccount}
            onChange={(e) => setFromAccount(e.target.value)}
            required
            placeholder=" "
          />
          <label>From Account ID</label>
        </div>

        <div className="form-group floating mb-3">
          <input
            type="text"
            value={toAccount}
            onChange={(e) => setToAccount(e.target.value)}
            required
            placeholder=" "
          />
          <label>To Account ID</label>
        </div>

        <div className="form-group floating mb-3">
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            placeholder=" "
          />
          <label>Amount</label>
        </div>

        <button type="submit" className="btn btn-success transfer-btn">
          Transfer
        </button>
      </form>
    </div>
  );
};

export default TransferFunds;
