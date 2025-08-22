#  Banking Portal – Fullstack Application

This is a **full-stack banking application** built to simulate a modern online banking experience. It supports **account creation, transactions, authentication, and user management** with a secure backend and a responsive frontend.

---

## Tech Stack

### 🔹 Frontend

* **React (TypeScript)** – for a modular, component-based UI.
* **CSS (custom themes)** – black, grey, white, pink, and yellow modern theme.
* **Axios** – for making API calls to the backend.
* **Toastify** – for notifications.

### 🔹 Backend

* **Spring Boot (Java 17+)** – handles business logic and REST APIs.
* **Spring Security + JWT** – for authentication & authorization.
* **Hibernate / JPA** – ORM for database interaction.
* **Maven** – build and dependency management.

### 🔹 Database

* **MySQL** – relational database to persist users, accounts, and transactions.
* Connection pooling via **HikariCP**.

---

##  Features

*  **JWT authentication** (login & register).
*  User and account management.
*  Transfer funds between accounts.
*  Transaction history tracking.
*  Dark theme with black/grey/pink/yellow color palette.

---

##  Running Locally

### 1️ Clone the repository

```bash
git clone https://github.com/mayankmishra22/banking-portal-fullstack.git
cd banking-portal-fullstack
```

### 2️ Backend (Spring Boot)

```bash
cd Bank-Portal-Backend
mvn spring-boot:run
```

* Runs on: `http://localhost:8080`
* Make sure MySQL is running and update `application.properties` with your DB credentials.

### 3️ Frontend (React)

```bash
cd Bank-Portal-Frontend
npm install
npm run dev
```

* Runs on: `http://localhost:5173` (default Vite dev server).

---

## Deployment Plan

* **Backend** → [Render](https://render.com) (Spring Boot JAR deployed as web service).
* **Database** → [Railway](https://railway.app) (free MySQL instance).
* **Frontend** → [Netlify](https://netlify.com) (React build hosted as static site).

---

## 📄 License

MIT – free to use and modify.

