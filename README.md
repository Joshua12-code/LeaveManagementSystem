# 🧾 Employee Leave Management System

A full-stack web application built with **Spring Boot**, **MySQL/PostgreSQL**, and **HTML/CSS/JavaScript** that allows employees to apply for leave and managers to review and approve requests.  
This project helps organizations manage employee leave requests efficiently.

🔗 **Live Demo:** [https://leavemanagementsystem-kcww.onrender.com/](https://leavemanagementsystem-kcww.onrender.com/)

---

## 🚀 Features

- 👨‍💼 **Employee Login:** Employees can log in and submit leave applications.  
- ✅ **Manager Approval:** Managers can review and approve/reject leave requests.  
- 📅 **Leave Tracking:** Tracks total, used, and remaining leaves per employee.  
- 🧮 **Automatic Status Update:** Leave status (Approved / Rejected / Pending).  
- 🧑‍💻 **Admin Control:** Admins can manage users and departments.  
- 💾 **Database Integration:** Uses PostgreSQL (or MySQL) for persistent storage.  
- 🌐 **Deployed on Render (Free Tier)** for public access.

---

## 🛠️ Tech Stack

**Backend:**  
- Java 17  
- Spring Boot 3  
- Spring Data JPA  
- Hibernate  
- PostgreSQL / MySQL  

**Frontend:**  
- HTML5  
- CSS3  
- JavaScript  

**Dev Tools:**  
- Maven  
- IntelliJ IDEA / VS Code  
- Render (for deployment)  
- Git & GitHub  

---

## ⚙️ Project Setup (Local)

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/yourusername/employee-leave-management.git
cd employee-leave-management
```

### 2️⃣ Configure the Database
Update the file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://<your-host>:5432/<your-database>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

*(You can also use MySQL if preferred.)*

### 3️⃣ Build and Run
```bash
./mvnw clean package
java -jar target/employee-leave-management-0.0.1-SNAPSHOT.jar
```

The app will run at 👉 **http://localhost:8080**

---

## 🐳 Docker Support

You can also run this project using Docker:

```dockerfile
# Use official OpenJDK image
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/employee-leave-management-0.0.1-SNAPSHOT.jar"]
```

Then build & run:

```bash
docker build -t leave-management .
docker run -p 8080:8080 leave-management
```

---

## 🗄️ Database Schema

### Tables:
- `employees` – stores employee info  
- `leave_requests` – stores leave applications  

### Example Entity:
```sql
CREATE TABLE employees (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255),
  department VARCHAR(100),
  role VARCHAR(50)
);
```

---

## 🧠 Future Enhancements
- Email notifications on leave approval/rejection  
- Role-based access control  
- Leave summary dashboard  
- Attendance integration  

---

## 📸 Screenshots

| Login Page | Manager Dashboard |
|-------------|-------------------|
| ![Login](screenshots/login.png) | ![Dashboard](screenshots/dashboard.png) |

> Add your screenshots inside a folder named `screenshots/` in the project root.

---

## 🧑‍💻 Author
**Joshua**  
📧 Contact: (your email or LinkedIn)  
🌐 GitHub: [https://github.com/yourusername](https://github.com/yourusername)

---

## 🪪 License
This project is licensed under the [MIT License](LICENSE).
