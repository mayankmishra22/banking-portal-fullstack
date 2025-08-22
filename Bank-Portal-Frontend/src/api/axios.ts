import axios, { AxiosError } from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");

    config.headers = config.headers || {};
    config.headers["Content-Type"] = "application/json";

    // Skip Authorization for login/register
    if (
      !config.url?.includes("/auth/login") &&
      !config.url?.includes("/auth/register") &&
      token
    ) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to get backend error data
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response && error.response.data) {
      // Pass backend response to frontend
      return Promise.reject(error.response.data);
    }
    return Promise.reject(error);
  }
);

export default api;
