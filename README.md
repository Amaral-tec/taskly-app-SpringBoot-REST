# 📌 Taskly

**Taskly** is a modular productivity and personal organization app with features such as **Agenda**, **To-Do List**, **Shopping List**, and **Financial Control**.  
The backend is built with **Java 17 + Spring Boot + PostgreSQL**, following a **RESTful architecture**.

---

## ✨ Features (Initial Roadmap)

- **Agenda** → create and manage events with reminders.  
- **To-Do** → task lists with priorities and completion tracking.  
- **Shopping List** → categorized shopping items.  
- **Finance** → recurring bills, income and expense tracking.  

---

## 🛠️ Tech Stack

- **Java 17+**  
- **Spring Boot 3.x**  
  - Spring Web (REST API)  
  - Spring Data JPA (persistence)  
  - Spring Security + JWT (authentication & authorization)  
  - Validation API  
- **PostgreSQL 15+** (AWS RDS compatible)  
- **Flyway** (database migration)  
- **Maven** (build & dependency management)  

---

## 📂 Project Structure
```
taskly/
├── src/
│ ├── main/
│ │ ├── java/com/taskly/ # Java source code
│ │ │ ├── config/ # security & configuration classes
│ │ │ ├── controller/ # REST controllers
│ │ │ ├── model/ # JPA entities
│ │ │ ├── repository/ # JPA repositories
│ │ │ ├── service/ # business logic
│ │ └── resources/
│ │ ├── application.yml # environment configs
│ │ └── db/migration/ # Flyway scripts
│ └── test/ # unit and integration tests
├── pom.xml
└── README.md
```

---

## ⚙️ Setup

### 1. Database
Create a PostgreSQL database locally or on AWS RDS:

```sql
CREATE DATABASE taskly;
CREATE USER taskly_user WITH ENCRYPTED PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE taskly TO taskly_user;
```
2. Environment Variables

Set database credentials using environment variables:

Linux / MacOS
```
export DB_USER=taskly_user
export DB_PASSWORD=your_password
```
Windows (PowerShell)
```
setx DB_USER "taskly_user"
setx DB_PASSWORD "your_password"
```

application.yml
```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskly
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```
▶️ Running the Application
1. Using Maven
```
./mvnw spring-boot:run
```
2. Using JAR
```
mvn clean package
java -jar target/taskly-0.0.1-SNAPSHOT.jar
```

The API will be available at:
http://localhost:8088/taskly

🔑 Authentication

JWT (JSON Web Token) based authentication.

Users can register and login via /auth/register and /auth/login.

Include token in the HTTP header for protected endpoints:

Authorization: Bearer <your_token>

🧪 REST API Examples
To-Do (Tasks)

GET all tasks

curl -X GET http://localhost:8088/taskly/api/todos \
  -H "Authorization: Bearer <token>"


POST create a task
```
curl -X POST http://localhost:8088/taskly/api/todos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"title": "Buy groceries", "description": "Milk, eggs, bread", "priority": "HIGH"}'
```

PUT update a task
```
curl -X PUT http://localhost:8088/taskly/api/todos/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"title": "Buy groceries and fruits", "completed": true}'
```

DELETE a task
```
curl -X DELETE http://localhost:8088/taskly/api/todos/1 \
  -H "Authorization: Bearer <token>"
```
Agenda (Events)

GET all events
```
curl -X GET http://localhost:8088/taskly/api/agenda/events \
  -H "Authorization: Bearer <token>"
```

POST create an event
```
curl -X POST http://localhost:8088/taskly/api/agenda/events \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"title": "Team Meeting", "description": "Project sync-up", "dateTime": "2025-09-12T10:00:00"}'
```
📌 Next Steps

 Implement CRUD for To-Do module.

 Implement Agenda endpoints with reminders.

 Add Finance module with recurring bills.

 Configure CI/CD (GitHub Actions).

 Deploy to AWS (ECS / Elastic Beanstalk).

📜 License

This project is licensed under the MIT License.
Feel free to use, modify, and contribute.
