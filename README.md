# Hotel Management System 🏨

## Overview
The **Hotel Management System** is a Spring Boot-based web application designed to streamline hotel operations such as room reservations, user management, role-based acces,etc. It integrates **MySQL** for data persistence and uses **Thymeleaf** for the frontend. The system is designed to optimize booking and customer management, increasing efficiency by 60%.

## Features ✨
- ✅ **Admin Dashboard** for managing hotel operations
- ✅ **User Registration & Authentication** (Spring Security)
- ✅ **Room Reservation System** 🏠
- ✅ **Customer & Booking Management** 📅
- ✅ **MySQL Database Integration** 🗄️
- ✅ **Spring Boot & Thymeleaf-based UI** 🎨
- ✅ **Role-based Access Control (Admin & User)** 🔑

## Technologies Used 🛠️
- **Backend:** Java, Spring Boot, Spring Data JPA
- **Frontend:** Thymeleaf, HTML5, CSS, Bootstrap
- **Database:** MySQL
- **Security:** Spring Security (JWT-based authentication)
- **Development Tools:** VS Code, IntelliJ IDEA

## Database Schema 📊
The system consists of the following key tables:
1. **accounts** - Stores account details of users.
2. **reservation** - Stores room reservation details.
3. **user** - Stores user information.

## Setup Instructions 🚀
### 1️⃣ Clone the Repository
```bash
git clone https://github.com/Anugupta5102/HostMate.git
cd HostMate
```

### 2️⃣ Configure Database
- Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_management
spring.datasource.username=root
spring.datasource.password=yourpassword
```
- Run the following command to create the database:
```sql
CREATE DATABASE hotel_management;
```

### 3️⃣ Build and Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

### 4️⃣ Access the Application
- **Frontend:** `http://localhost:8080`
- **Admin Panel:** `http://localhost:8080/admin`

## API Endpoints 🔗
| Method | Endpoint           | Description              |
|--------|-------------------|--------------------------|
| GET    | /users            | Get all users            |
| POST   | /register         | Register new user        |
| POST   | /login            | User login               |
| GET    | /rooms            | Get available rooms      |
| POST   | /reserve          | Reserve a room           |
| GET    | /admin/bookings   | Admin: View reservations |

## Contribution Guidelines 🤝
1. Fork the repository 🍴
2. Create a feature branch (`git checkout -b feature-name`)
3. Commit changes (`git commit -m 'Added new feature'`)
4. Push to GitHub (`git push origin feature-name`)
5. Submit a pull request 📩

## License 📜
This project is licensed under the **MIT License**.

## Contact 📧
- **Developer:** Anushree Gupta
- **GitHub:** [Anugupta5102](https://github.com/Anugupta5102)
- **Email:** anugupta5102@gmail.com

---

**Star ⭐ this repository if you find it useful!** 🚀
