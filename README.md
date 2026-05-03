# Dormitory Management System

JavaFX desktop application scaffold focused on the technical foundation and basic authentication.

## Stack
- Java 21
- JavaFX (FXML)
- PostgreSQL
- JDBC
- Maven
- JPMS (`module-info.java`)

## Project Structure
- `model`
- `dao`
- `service`
- `controller`
- `util`

## Run (CLI)
```bash
mvn clean javafx:run
```

## Run Database With Docker
Start PostgreSQL and pgAdmin:
```bash
docker compose up -d
```

Stop containers:
```bash
docker compose down
```

Reset database volume (will rerun init.sql on next startup):
```bash
docker compose down -v
```

Services:
- PostgreSQL: localhost:5432
- pgAdmin: http://localhost:5050
	- Email: admin@dormitory.local
	- Password: admin

## Run (IntelliJ)
1. Open as Maven project.
2. Let IntelliJ import dependencies.
3. Run Maven goal `javafx:run` or run `com.dormitory.management.DormitoryApp`.

## Database Configuration
Default config file:
- `src/main/resources/config/database.properties`

Environment variables override file values:
- `DORMITORY_DB_URL`
- `DORMITORY_DB_USER`
- `DORMITORY_DB_PASSWORD`

At startup, the application verifies database connectivity and initializes the core authentication schema (`users` table + default admin seed) if missing.

## Scope
Current implementation covers only Activity 1:
- project structure and layered architecture
- database connectivity and configuration
- Docker/Maven setup
- base login/register flow with roles

Business features for applications, room allocation, complaints, tickets, and notifications are intentionally not implemented yet.
