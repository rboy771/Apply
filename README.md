# Apply
Intern Job search made easy

A Spring Boot web application for tracking job applications, built with:
- **Java 17 + Spring Boot 3.2**
- **Spring Data JPA + MariaDB** (H2 for tests)
- **Thymeleaf** for server-side HTML templates
- **Bootstrap 5 + Bootstrap Icons** for styling

## Features
- **List** all job applications, sorted by most recent
- **Search** by company name or filter by status
- **Add / Edit / Delete** applications
- **View** full detail of any application
- Track company, position, location, date applied, status, job URL, and notes
- Status workflow: Applied → Phone Screen → Interview → Technical → Offer → Accepted / Rejected / Withdrawn

## Prerequisites
- Java 17+
- MariaDB (or MySQL) running locally

> Note: the Gradle wrapper is committed, so you do not need a separate Gradle installation.

## Setup

### 1. Create the database and user
```sql
CREATE DATABASE apply_db;
CREATE USER 'apply_user'@'localhost' IDENTIFIED BY 'apply_pass';
GRANT ALL PRIVILEGES ON apply_db.* TO 'apply_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configure credentials (optional)
Edit `src/main/resources/application.properties` to match your MariaDB host/user/password.

### 3. Run the application
```bash
./gradlew bootRun
```

On Windows PowerShell:
```powershell
.\gradlew.bat bootRun
```

Navigate to <http://localhost:8080>.

## Running Tests
```bash
./gradlew test
```

On Windows PowerShell:
```powershell
.\gradlew.bat test
```

Tests use an H2 in-memory database — no external database required.

## Build
```bash
./gradlew build
java -jar build/libs/apply-0.0.1-SNAPSHOT.jar
```

On Windows PowerShell:
```powershell
.\gradlew.bat build
```
