# 🍎 Fruit & Provider API (MySQL Version)

## 📋 Description

This project is the **Level 2** evolution of the Fruit Stock Management API. It expands the original functionality by introducing **MySQL** persistence (migrating from H2) and adding a new domain entity: **Providers**.

The system now manages the relationship between Fruits and Providers using JPA (`@ManyToOne`), ensuring that every fruit is associated with a supplier. It follows a **TDD (Test-Driven Development)** approach and includes **Docker** configuration for production-ready deployment.

## 🚀 Features

* **Fruit Management:** CRUD operations for fruits (Name, Weight).
* **Provider Management:** CRUD operations for providers (Name, Country).
* **Relationship Mapping:** Fruits are linked to specific Providers.
* **Advanced Filtering:** List fruits filtered by their Provider ID.
* **Data Validation:** Input validation using DTOs and Bean Validation (`@NotNull`, `@Positive`, etc.).
* **Error Handling:** Global Exception Handler for consistent HTTP responses (404, 400, 500).
* **Containerization:** Multi-stage Docker build and Docker Compose for infrastructure.

## 🛠️ Tech Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.x
* **Build Tool:** Maven
* **Database:** MySQL 8.0
* **Persistence:** Spring Data JPA (Hibernate)
* **Testing:** JUnit 5, Mockito, MockMvc
* **Tools:** Lombok, Docker, Docker Compose

## 🗄️ Database Model

The application uses a relational database with two main tables.

* **Provider:** One provider can supply multiple fruits.
* **Fruit:** Each fruit belongs to one provider (**Many-To-One** relationship).

## ⚙️ Configuration & Environment Variables

The application is configured to run with environment variables for security and flexibility in Docker environments.

| Variable | Description | Default (Local) |
| :--- | :--- | :--- |
| `DB_HOST` | Database host address | `localhost` |
| `DB_PORT` | Database port | `3306` |
| `DB_NAME` | Database schema name | `fruit_db` |
| `DB_USER` | Database username | `root` |
| `DB_PASSWORD` | Database password | `root` |

## 🔌 API Endpoints

### 🚛 Providers

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/providers` | Register a new provider | `201 Created` |
| `GET` | `/providers` | List all providers | `200 OK` |
| `PUT` | `/providers/{id}` | Update provider details | `200 OK` |
| `DELETE` | `/providers/{id}` | Delete a provider* | `204 No Content` |

*\*Note: A provider cannot be deleted if they have associated fruits (Returns 400 Bad Request).*

### 🍎 Fruits

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/fruits` | Add a new fruit (requires `providerId`) | `201 Created` |
| `GET` | `/fruits` | List all fruits | `200 OK` |
| `GET` | `/fruits/{id}` | Get specific fruit detail | `200 OK` |
| `GET` | `/fruits?providerId={id}` | **Filter** fruits by provider | `200 OK` |
| `PUT` | `/fruits/{id}` | Update fruit details | `200 OK` |
| `DELETE` | `/fruits/{id}` | Delete a fruit | `204 No Content` |

---
## 🐳 Running with Docker

This project includes a **Docker Compose** file to set up the infrastructure (MySQL) and the Application.

### Option 1: Full Deployment (App + DB)
To build the application image and start everything:

```bash
docker-compose up --build
```
The API will be available at: http://localhost:8080

### Option 2: Development Mode (Hybrid)

To run only the database in Docker while you debug the Java app in your IDE:
```bash
docker-compose up -d mysql-db
```
Then run the `FruitApiMySqlApplication.java` class in IntelliJ/Eclipse.

## 🧪 Testing

The application has been developed using TDD. To run the unit tests (Controllers & Services) select
test option in project's LifeCycle directory in maven window.
You can also execute this command in a terminal in the root of the project:
``` bash
./mvnw test
```
---
## 📦 Project Structure
```Plaintext
src/main/java/cat/itacademy/s04/t02/n02
├── controllers    # REST Controllers (Web Layer)
├── services       # Business Logic
├── repository     # JPA Interfaces (Data Layer)
├── model          # Entities and DTOs
└── exception      # Global Exception Handling
```
![fruteria_img](https://images.unsplash.com/photo-1550989460-0adf9ea622e2?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D)