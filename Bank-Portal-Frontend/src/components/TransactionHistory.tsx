import React, { useState } from "react";
import { toast } from "react-toastify";
import api from "../api/axios";
import "../styles/TransactionHistory.css";

interface TransactionResponse {
  transactionId: number;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  timestamp: string;
  status: string;
  fromBalanceAfter: number;
  toBalanceAfter: number;
}

const TransactionHistory: React.FC = () => {
  const [accountId, setAccountId] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");
  const [sortBy, setSortBy] = useState<"amount" | "timestamp">("timestamp");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("desc");
  const [transactions, setTransactions] = useState<TransactionResponse[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchHistory = async () => {
    setLoading(true);
    try {
      // Only include params that have values
      const params: any = {};
      if (accountId.trim() !== "") params.accountId = Number(accountId);
      if (fromDate) params.from = fromDate;
      if (toDate) params.to = toDate;

      console.log("Request params:", params);

      const response = await api.get("/transactions/history", { params });

      // Sort the results
      const sorted = [...response.data].sort((a, b) => {
        const valA = sortBy === "amount" ? a.amount : new Date(a.timestamp).getTime();
        const valB = sortBy === "amount" ? b.amount : new Date(b.timestamp).getTime();
        return sortOrder === "asc" ? valA - valB : valB - valA;
      });

      setTransactions(sorted);
    } catch (err: any) {
      console.error(err);
      toast.error(
        err.response?.data?.message || "Failed to fetch transaction history"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card shadow-sm p-4 rounded-4">
        <h4 className="mb-4 fw-bold text-primary">🔍 Transaction History</h4>

        <div className="d-flex flex-wrap gap-3 align-items-end mb-4">
          <div className="flex-grow-1">
            <label className="form-label">Account ID</label>
            <input
              type="text"
              className="form-control rounded-pill"
              value={accountId}
              onChange={(e) => setAccountId(e.target.value)}
              placeholder="e.g. 101"
            />
          </div>
          <div>
            <label className="form-label">From</label>
            <input
              type="date"
              className="form-control rounded-pill"
              value={fromDate}
              onChange={(e) => setFromDate(e.target.value)}
            />
          </div>
          <div>
            <label className="form-label">To</label>
            <input
              type="date"
              className="form-control rounded-pill"
              value={toDate}
              onChange={(e) => setToDate(e.target.value)}
            />
          </div>
          <div>
            <label className="form-label">Sort By</label>
            <select
              className="form-select rounded-pill"
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value as "amount" | "timestamp")}
            >
              <option value="timestamp">Timestamp</option>
              <option value="amount">Amount</option>
            </select>
          </div>
          <div>
            <label className="form-label">Order</label>
            <select
              className="form-select rounded-pill"
              value={sortOrder}
              onChange={(e) => setSortOrder(e.target.value as "asc" | "desc")}
            >
              <option value="desc">Descending</option>
              <option value="asc">Ascending</option>
            </select>
          </div>
          <div>
            <button
              className="btn btn-primary rounded-pill px-4"
              onClick={fetchHistory}
              disabled={loading}
            >
              {loading ? "Fetching..." : "Fetch"}
            </button>
          </div>
        </div>

        {loading ? (
          <p>Loading transactions...</p>
        ) : transactions.length > 0 ? (
          <div className="table-responsive">
            <table className="table table-hover table-striped align-middle">
              <thead className="table-light">
                <tr>
                  <th>ID</th>
                  <th>From</th>
                  <th>To</th>
                  <th>Amount</th>
                  <th>Timestamp</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((tx) => (
                  <tr key={tx.transactionId}>
                    <td>{tx.transactionId}</td>
                    <td>{tx.fromAccountId}</td>
                    <td>{tx.toAccountId}</td>
                    <td>₹{tx.amount.toFixed(2)}</td>
                    <td>{new Date(tx.timestamp).toLocaleString()}</td>
                    <td>
                      <span
                        className={`badge bg-${
                          tx.status === "SUCCESS" ? "success" : "danger"
                        }`}
                      >
                        {tx.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-muted">
            No transactions found for the selected filters.
          </p>
        )}
      </div>
    </div>
  );
};

export default TransactionHistory;
