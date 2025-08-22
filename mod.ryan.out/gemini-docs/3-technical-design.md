# Technical Design for Spring Boot Migration

This document outlines the technical design for migrating the legacy `shopping-cart` J2EE application to a modern Spring Boot application.

## 1. Project Structure

The new application will be a multi-module Maven project to promote modularity and separation of concerns. The proposed modules are:

-   `parent-pom`: The parent POM for the entire project, defining common dependencies, plugin management, and build configurations.
-   `web-app`: Contains the Spring Boot web application, including controllers, views (Thymeleaf templates), and static resources. This will be the main executable JAR.
-   `data-access`: Contains JPA entities, repositories (Spring Data JPA), and database migration scripts. This module will encapsulate all data persistence logic.
-   `service`: Contains the business logic and service layer interfaces and implementations. This layer will orchestrate interactions between the web layer and data access layer.
-   `integration-tests`: A separate module for end-to-end integration tests using Testcontainers and Selenium.

```
mod.ryan.out/
├── pom.xml (parent-pom)
├── web-app/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/shashi/webapp/
│       │   ├── resources/
│       │   │   ├── application.properties
│       │   │   └── templates/
│       │   └── static/
│       └── test/
│           └── java/com/shashi/webapp/
├── data-access/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/shashi/dataaccess/
│       │   └── resources/db/migration/
│       └── test/
│           └── java/com/shashi/dataaccess/
├── service/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   └── java/com/shashi/service/
│       └── test/
│           └── java/com/shashi/service/
└── integration-tests/
    ├── pom.xml
    └── src/
        └── test/
            └── java/com/shashi/integrationtests/
```

## 2. Key Libraries and Technologies

-   **Spring Boot**: For rapid application development, auto-configuration, and embedded server.
-   **Spring Web (Spring MVC)**: For building RESTful APIs and handling web requests.
-   **Spring Data JPA**: For simplified data access and persistence using Hibernate as the JPA provider.
-   **Thymeleaf**: A modern server-side template engine for rendering dynamic HTML views.
-   **Spring Security**: For robust authentication and authorization mechanisms.
-   **PostgreSQL**: The target relational database for the modernized application.
-   **Flyway/Liquibase**: For managing database schema migrations (to be decided, but Flyway is generally simpler for initial migrations).
-   **JUnit 5**: The testing framework for unit tests.
-   **Mockito**: For mocking dependencies in unit tests.
-   **Testcontainers**: For providing lightweight, disposable containers for integration tests (e.g., PostgreSQL database, Selenium browser).
-   **Selenium**: For automating browser interactions in end-to-end integration tests.

## 3. Architectural Patterns

The application will follow a layered architecture:

-   **Controller Layer (`web-app` module)**:
    -   Handles incoming HTTP requests.
    -   Uses Spring MVC annotations (`@Controller`, `@RequestMapping`, etc.).
    -   Performs input validation.
    -   Delegates business logic to the Service Layer.
    -   Prepares model data for Thymeleaf views or returns JSON responses for APIs.

-   **Service Layer (`service` module)**:
    -   Encapsulates the core business logic.
    -   Annotated with `@Service`.
    -   Orchestrates operations across multiple repositories if needed.
    -   Applies transaction management.
    -   Contains the main application logic and rules.

-   **Data Access Layer (`data-access` module)**:
    -   Manages persistence operations.
    -   Uses Spring Data JPA repositories (`@Repository`).
    -   Interacts with the database (PostgreSQL).
    -   Maps Java objects (Entities) to database tables.

## 4. Database Migration Strategy

The existing MySQL database will be migrated to PostgreSQL. The strategy involves:

1.  **Schema Conversion**: Convert the existing MySQL schema (from `mysql_query.sql`) to PostgreSQL compatible DDL statements. This will involve adjusting data types and syntax where necessary.
2.  **Data Migration**: Develop scripts or use tools to migrate existing data from the MySQL database to the new PostgreSQL database. For the initial phase, manual data insertion via SQL scripts will be sufficient if the dataset is small.
3.  **Flyway/Liquibase Integration**: Integrate a database migration tool (e.g., Flyway) into the Spring Boot application. Initial schema creation and subsequent schema changes will be managed through versioned migration scripts.

## 5. Security Considerations

-   **Spring Security**: Will be used for authentication and authorization.
    -   User authentication will be handled via a custom `UserDetailsService` interacting with the `user` table.
    -   Role-based authorization will be implemented to differentiate between regular users and administrators.
-   **Password Hashing**: User passwords will be securely stored using strong hashing algorithms (e.g., BCrypt) via Spring Security's `PasswordEncoder`.
-   **Input Validation**: All user inputs will be validated at the Controller and Service layers to prevent common vulnerabilities like SQL injection and XSS.
-   **Secure HTTP Headers**: Implement appropriate HTTP security headers (e.g., HSTS, X-Content-Type-Options, X-Frame-Options) to enhance application security.

## 6. Testing Strategy

A comprehensive testing strategy will be employed:

-   **Unit Tests**: For individual components (services, controllers, utility classes) using JUnit 5 and Mockito. These tests will run in isolation.
-   **Integration Tests**: For verifying the interaction between different layers (e.g., Controller-Service-Repository) and external systems (database). Testcontainers will be used to spin up a real PostgreSQL instance for these tests.
-   **End-to-End Tests**: Using Selenium and Testcontainers to simulate full user journeys through the web UI, ensuring critical functionalities work as expected from a user's perspective.

## 7. Deployment Strategy (Cloud Run Ready)

The application will be designed for cloud-native deployment, specifically targeting Google Cloud Run.

-   **Containerization**: The Spring Boot application will be containerized using Docker. A `Dockerfile` will be created for the `web-app` module.
-   **Stateless Design**: The application will be designed to be stateless, allowing for easy scaling and deployment on platforms like Cloud Run.
-   **Externalized Configuration**: Configuration properties (e.g., database connection details) will be externalized using Spring Boot's configuration mechanisms, allowing them to be injected via environment variables or Kubernetes secrets in a Cloud Run environment.
-   **Health Checks**: Spring Boot Actuator will be used to expose health endpoints for Cloud Run to monitor application health.