# MySQL Setup Guide

## Step 1 – Install MySQL 8.0+
Download from https://dev.mysql.com/downloads/ or use your package manager.

## Step 2 – Create Database and User
```sql
CREATE DATABASE student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'smsuser'@'localhost' IDENTIFIED BY 'smspassword';
GRANT ALL PRIVILEGES ON student_management.* TO 'smsuser'@'localhost';
FLUSH PRIVILEGES;
```

## Step 3 – Configure Environment Variables
Copy `.env.example` to `.env` and fill in your values:
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=student_management
DB_USERNAME=smsuser
DB_PASSWORD=smspassword
JWT_SECRET=your_64_char_hex_secret_here
```

## Step 4 – Run with MySQL (prod profile)
```bash
# Option A: Maven
mvn spring-boot:run -Dspring-boot.run.profiles=prod \
  -DDB_USERNAME=smsuser \
  -DDB_PASSWORD=smspassword \
  -DJWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Option B: Set environment variables then run JAR
export SPRING_PROFILES_ACTIVE=prod
export DB_USERNAME=smsuser
export DB_PASSWORD=smspassword
export JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
java -jar target/student-management-1.0.0.jar
```

Flyway will automatically create all tables and seed data on first run.

## Step 5 – Verify
- App: http://localhost:8080/actuator/health
- Swagger: http://localhost:8080/swagger-ui.html
- Login: POST /api/auth/login  { "username": "admin", "password": "password" }

## Default Credentials (auto-seeded by Flyway)
| Username | Password | Role  |
|----------|----------|-------|
| admin    | password | ADMIN |
| teacher1 | password | TEACHER |
| teacher2 | password | TEACHER |

## Running with H2 (local dev – no MySQL needed)
```bash
mvn spring-boot:run
# or
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Open http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:smsdb`  Username: `sa`  Password: *(blank)*
