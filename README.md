# 🏦 Bank Management System

A Java-based web application for managing common banking operations through a browser-based interface.

The project demonstrates Java web development using Servlets, JSP, HTML/CSS, JDBC, and MySQL.

## 🚀 Features

### User Operations

* User account creation
* User login
* Account information
* Balance checking
* Deposits
* Withdrawals
* Fund transfers
* Transaction history

### Employee Operations

* Employee login
* Employee registration
* Customer/account enquiries
* User detail viewing
* Staff enquiry portal

### Application Handling

* Account validation
* Login validation
* Insufficient-funds handling
* Invalid-credential handling
* Dedicated error and status pages

## 🛠️ Tech Stack

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java              | Application and business logic |
| Java Servlets     | Request handling               |
| JSP               | Dynamic web pages              |
| HTML              | Page structure                 |
| CSS               | Styling                        |
| JDBC              | Database connectivity          |
| MySQL             | Data persistence               |
| MySQL Connector/J | JDBC driver                    |
| Apache Tomcat     | Web application server         |

## 📂 Project Structure

```text
BankManagementSystem/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── p1/
│       │   │   ├── Employee.java
│       │   │   ├── Login.java
│       │   │   ├── UserAccountLogin.java
│       │   │   ├── UserCreateAccount.java
│       │   │   ├── UserFromEmployeeDetails.java
│       │   │   └── showAllTransactions.java
│       │   │
│       │   └── p2/
│       │       └── DbUtil.java
│       │
│       └── webapp/
│           ├── HTML pages
│           ├── JSP pages
│           ├── CSS files
│           └── WEB-INF/
│
├── .gitignore
└── README.md
```

## 🔄 Application Workflow

```text
User / Employee
       ↓
     Login
       ↓
 Authentication
       ↓
 ┌─────┴─────┐
 ↓           ↓
User       Employee
Portal      Portal
 ↓           ↓
Banking     Customer
Operations  Enquiries
 ↓
MySQL Database
```

## 🗄️ Database

The application uses MySQL for persistent storage and JDBC for database connectivity.

Database configuration should be supplied through local environment variables rather than committed credentials.

Example:

```text
BANK_DB_URL
BANK_DB_USER
BANK_DB_PASSWORD
```

## ⚙️ Getting Started

### Prerequisites

Install:

* Java JDK
* MySQL Server
* Apache Tomcat
* An IDE such as Eclipse, IntelliJ IDEA, or VS Code

### Clone the Repository

```bash
git clone https://github.com/tharunkolipaka/BankManagementSystem.git
cd BankManagementSystem
```

### Configure the Database

Create the `BankManagementSystem` database and configure the required tables.

Set the database connection variables in your local environment.

### Run the Application

Deploy the web application to Apache Tomcat and start the server.

Open the deployed application in your browser.

## 🔐 Security Improvements

The project is being improved to follow better security practices, including:

* Removing database credentials from source code
* Using `PreparedStatement` for database queries
* Improving authentication handling
* Avoiding plaintext password storage
* Improving input validation

## 🎯 Learning Outcomes

This project demonstrates practical experience with:

* Core Java
* Java Servlets
* JSP
* JDBC
* MySQL
* HTML/CSS
* CRUD-style application development
* Authentication workflows
* Database integration
* Exception handling
* Web application structure

## 🔮 Future Enhancements

* Password hashing
* Role-based authorization
* Transaction audit logging
* Improved responsive UI
* Automated testing
* REST API integration
* Better validation and error handling
* Cloud deployment

## 📸 Screenshots

Add screenshots of:

```text
Login
Dashboard
Account Creation
Balance
Deposit
Withdrawal
Fund Transfer
Transaction History
Employee Portal
```

## 👨‍💻 Author

**Tharun Kolipaka**

GitHub: https://github.com/tharunkolipaka

## 📄 License

This project is intended for educational and portfolio purposes.
