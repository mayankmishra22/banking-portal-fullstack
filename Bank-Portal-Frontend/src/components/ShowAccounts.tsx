// src/components/dashboard/ShowAccounts.tsx
import React, { useState } from "react";
import { toast } from "react-toastify";
import api from "../api/axios";
import "../styles/ShowAccounts.css";

interface Account {
  accountHolderName: string;
  accountId: number;
  accountNumber: string;
  accountType: string;
  balance: number;
  userId: number;
}

const ShowAccounts: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [accountId, setAccountId] = useState<string>("");

  const handleFetch = async () => {
    const trimmedId = accountId.trim();
    const endpoint = trimmedId ? `/accounts/${trimmedId}` : "/accounts";

    try {
      const response = await api.get(endpoint);

      const accountsData = Array.isArray(response.data)
        ? response.data
        : [response.data];

      setAccounts(accountsData);
      toast.success("Accounts fetched successfully!");
    } catch (err: any) {
      const errorMessage = trimmedId
        ? `Account not found: ${trimmedId}`
        : "No accounts registered with this user";

      toast.error(errorMessage);
      setAccounts([]);
    }
  };

  return (
    <div className="card shadow-sm show-accounts-card p-4">
      <h3 className="mb-4 fw-bold show-accounts-title">Accounts</h3>

      <div className="row g-2 align-items-end">
        <div className="form-group floating flex-grow-1">
          <input
            type="text"
            value={accountId}
            onChange={(e) => setAccountId(e.target.value)}
          />
          <label className={accountId ? "floated" : ""}>
            Account ID (optional)
          </label>
        </div>
        <div className="col-md-4 text-md-start">
          <button className="btn btn-primary w-50" onClick={handleFetch}>
            Fetch
          </button>
        </div>
      </div>

      {accounts.length > 0 ? (
        <table className="table table-striped table-hover mt-4 align-middle">
          <thead className="table-light">
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>A/C Number</th>
              <th>Type</th>
              <th>Balance</th>
              <th>User ID</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((acc, index) => (
              <tr key={index}>
                <td>{acc.accountId}</td>
                <td>{acc.accountHolderName}</td>
                <td>{acc.accountNumber}</td>
                <td>{acc.accountType}</td>
                <td className="fw-semibold text-success">
                  ₹{acc.balance.toLocaleString()}
                </td>
                <td>{acc.userId}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p className="mt-4 text-muted fst-italic text-center">
          No accounts to display.
        </p>
      )}
    </div>
  );
};

export default ShowAccounts;
