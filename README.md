# 🏦 Bank Management System

A Java-based web application that simulates core banking operations such as user account management, deposits, withdrawals, balance checking, fund transfers, and transaction tracking.

The project demonstrates practical software development concepts including Java, JSP, HTML/CSS, database connectivity, authentication, and transaction-oriented application design.

## 🚀 Features

### 👤 User Management

* User account creation
* User login and authentication
* Account details management
* User information viewing

### 💰 Banking Operations

* Check account balance
* Deposit funds
* Withdraw funds
* Transfer funds
* Handle insufficient funds
* View transaction history

### 👨‍💼 Employee / Staff Operations

* Employee login
* Employee registration
* Customer/account enquiries
* View user details
* Staff enquiry portal

### 🔐 Application Handling

* Login validation
* Invalid credential handling
* Account existence validation
* Error and failure pages
* Input-based application workflows

## 🛠️ Tech Stack

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| **Java**          | Application and business logic |
| **JSP**           | Dynamic web pages              |
| **HTML**          | Web page structure             |
| **CSS**           | User interface styling         |
| **MySQL**         | Database                       |
| **JDBC**          | Database connectivity          |
| **Apache Tomcat** | Java web application server    |

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
│           ├── *.html
│           ├── *.jsp
│           ├── *.css
│           ├── META-INF/
│           └── WEB-INF/
│
├── .gitignore
└── README.md
```

## 🔑 Core Modules

### Authentication

The application provides separate login workflows for users and employees with validation for incorrect credentials.

### Account Management

Users can create accounts and access account-related information through the web interface.

### Transactions

The system supports common banking operations including:

* Deposits
* Withdrawals
* Fund transfers
* Balance checking
* Transaction history

### Employee Portal

Employees have dedicated functionality for managing and enquiring about customer account information.

## 🗄️ Database

The application uses **MySQL** for storing banking and user-related information.

Database connectivity is handled through **JDBC** using MySQL Connector/J.

> Database configuration should be updated according to your local MySQL environment before running the application.

## ⚙️ Getting Started

### Prerequisites

Make sure the following are installed:

* Java JDK
* MySQL Server
* Apache Tomcat
* IDE such as IntelliJ IDEA, Eclipse, or VS Code
* MySQL Connector/J

### 1. Clone the Repository

```bash
git clone https://github.com/tharunkolipaka/BankManagementSystem.git
cd BankManagementSystem
```

### 2. Configure MySQL

Create the required database and tables based on the application's database configuration.

Update the database connection settings in:

```text
src/main/java/p2/DbUtil.java
```

with your MySQL username, password, database name, and connection URL.

### 3. Configure the Application Server

Deploy the project using Apache Tomcat.

Make sure the required MySQL JDBC driver is available to the application.

### 4. Run the Application

Start the Tomcat server and open the application through the deployed URL.

## 🧪 Functional Areas

The application currently contains pages and workflows for:

* User login
* User registration
* Employee login
* Employee registration
* Account existence validation
* Balance checking
* Deposits
* Withdrawals
* Fund transfers
* Transaction history
* User enquiries
* Staff enquiries
* Error handling

## 📸 Screenshots

Screenshots can be added here to demonstrate:

* Login page
* User dashboard
* Account creation
* Balance page
* Deposit/withdrawal workflow
* Fund transfer
* Transaction history
* Employee portal

Example:

```text
screenshots/
├── login.png
├── dashboard.png
├── account-creation.png
├── transactions.png
└── employee-portal.png
```

## 🎯 Learning Outcomes

This project helped strengthen practical knowledge of:

* Java programming
* Object-oriented programming
* Java web application development
* JSP
* HTML/CSS
* JDBC
* MySQL database integration
* Authentication workflows
* CRUD-style application logic
* Exception and error handling

## 🔮 Future Enhancements

Planned improvements include:

* Password hashing and stronger authentication
* Improved UI/UX
* Transaction audit logging
* REST API integration
* Automated unit and integration testing
* Improved database security
* Input validation and sanitization
* Responsive frontend design
* Deployment to a cloud platform

## 👨‍💻 Author

**Tharun Kolipaka**

GitHub: [@tharunkolipaka](https://github.com/tharunkolipaka)

## 📄 License

This project is intended for educational and portfolio purposes.
