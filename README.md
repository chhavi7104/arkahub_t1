# Finance dashboard

# 💳 FinWallet – Digital Wallet Application

A full-stack fintech-inspired Digital Wallet application built with **Angular** and **Spring Boot**. This project demonstrates how a modern wallet system works by allowing users to manage their balance, transfer money, and view transaction history through a clean web interface.

> **Note:** This project is being developed as a learning project to understand Angular, Spring Boot, REST APIs, database design, and full-stack application development.

---

## ✨ Features

- User Registration & Login *(Future Enhancement)*
- Wallet Dashboard
- View Current Balance
- Add Money to Wallet
- Transfer Money to Another User
- Transaction History
- Input Validation
- Exception Handling
- Responsive User Interface

---

## 🛠️ Tech Stack

### Frontend
- Angular
- TypeScript
- HTML5
- CSS3
- Bootstrap / Angular Material *(Optional)*

### Backend
- Spring Boot
- Spring MVC
- Spring Data JPA
- Maven

### Database
- MySQL *(or MongoDB if following the DatMT series)*

---

## 📂 Project Structure

```text
FinWallet/
│
├── backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   ├── exception/
│   └── config/
│
├── frontend/
│   ├── components/
│   ├── services/
│   ├── models/
│   ├── pages/
│   └── shared/
│
└── README.md
```


---

## 📌 Functionalities

### 🏠 Dashboard
- Display wallet balance
- Quick access to wallet features
- Show recent transactions

### 💰 Add Money
- Add funds to wallet
- Validate entered amount
- Update wallet balance

### 💸 Transfer Money
- Send money to another user
- Check available balance
- Update sender and receiver balances
- Record transaction history

### 📜 Transaction History
- View all wallet transactions
- Display transaction type
- Show amount, status, and date

---

## 🌐 REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/wallet/{userId}` | Get wallet details |
| POST | `/api/wallet/add` | Add money |
| POST | `/api/wallet/transfer` | Transfer money |
| GET | `/api/transactions/{userId}` | Get transaction history |

---

## 🗄️ Database Design

### User

| Field | Type |
|-------|------|
| id | Long |
| name | String |
| email | String |
| balance | Decimal |

### Transaction

| Field | Type |
|-------|------|
| id | Long |
| senderId | Long |
| receiverId | Long |
| amount | Decimal |
| transactionType | String |
| status | String |
| createdAt | Timestamp |

---

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/your-username/finwallet.git
```

### Backend Setup

1. Open the backend project.
2. Configure the database connection.
3. Run the Spring Boot application.

### Frontend Setup

Install dependencies:

```bash
npm install
```

Run the Angular application:

```bash
ng serve
```

Open:

```text
http://localhost:4200
```

---

## 🎯 Learning Objectives

This project is built to understand:

- Spring Boot Architecture
- Dependency Injection
- REST API Development
- Layered Architecture
- Spring Data JPA
- Angular Components
- Angular Routing
- Angular Services
- HTTP Client
- API Integration
- Form Validation
- Exception Handling

---

## 📈 Future Enhancements

- JWT Authentication
- Role-Based Access Control
- QR Code Payments
- UPI Payment Simulation
- Email Notifications
- Search & Filter Transactions
- Pagination
- Dashboard Analytics
- Docker Support
- Cloud Deployment

---

## 📚 Learning Journey

This project is being developed step by step while learning Angular and Spring Boot.

Instead of simply completing the project, the focus is on understanding:

- Why each technology is used
- How requests flow from the frontend to the backend
- How Spring Boot and Angular communicate
- Database design and business logic
- Industry-standard project architecture

---

## 🤝 Acknowledgements

Inspired by real-world digital wallet applications such as Paytm, PhonePe, Google Pay, and Amazon Pay. This project is developed purely for educational purposes.

---

## 👩‍💻 Author

**Chhavi Sharma**

- 💼 Java Full Stack Learner
- 🌱 Learning Angular & Spring Boot
- 💻 Computer Science Engineer

---
## 📅 Learning Progress

- [x] Episode 1 - Project Setup
- [ ] Episode 2 - Backend Configuration
- [ ] Episode 3 - User Management
- [ ] Episode 4 - Wallet API
- [ ] Episode 5 - Angular Integration
- [ ] Episode 6 - Transaction History
- [ ] Episode 7 - Deployment

---

⭐ If you like this project, consider giving it a star!



