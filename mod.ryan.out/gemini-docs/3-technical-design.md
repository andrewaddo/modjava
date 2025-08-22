## Technical Design for Modernization: Spring Boot Application

## 1. Introduction
This document outlines the technical design for migrating the legacy J2EE `shopping-cart` application to a modern Spring Boot application. The goal is to achieve full feature parity, enhance performance and security, and establish a production-grade application with a robust automated testing suite.

## 2. Project Structure
The new application will be a multi-module Maven project to promote modularity and separation of concerns.

```
./mod.ryan.out/
├── pom.xml (Parent POM)
├── web-app/
│   ├── pom.xml (Web module)
│   └── src/main/java/com/shashi/webapp/ (Spring Boot application, Controllers, Services, etc.)
│   └── src/main/resources/templates/ (Thymeleaf templates)
│   └── src/main/resources/static/ (Static assets like CSS, JS, images)
│   └── src/test/java/com/shashi/webapp/ (Unit tests for web layer)
├── data-access/
│   ├── pom.xml (Data access module)
│   └── src/main/java/com/shashi/data/ (JPA Entities, Repositories)
│   └── src/test/java/com/shashi/data/ (Unit tests for data access layer)
├── integration-tests/
│   ├── pom.xml (Integration tests module)
│   └── src/test/java/com/shashi/integration/ (Selenium and Testcontainers tests)
└── gemini-docs/
    ├── 1-database-schema.md
    ├── 2-features/
    └── 3-technical-design.md
```

## 3. Key Libraries and Technologies

*   **Spring Boot**: Core framework for building stand-alone, production-grade Spring applications.
*   **Spring Web (Spring MVC)**: For building RESTful APIs and handling web requests.
*   **Thymeleaf**: Server-side template engine for rendering dynamic HTML content. Chosen for its natural templating and strong integration with Spring.
*   **Spring Data JPA**: Simplifies data access using JPA (Java Persistence API) and Hibernate as the ORM (Object-Relational Mapping) provider.
*   **PostgreSQL**: Relational database management system. The existing MySQL schema will be migrated to PostgreSQL.
*   **Spring Security**: For robust authentication and authorization mechanisms.
*   **Lombok**: Reduces boilerplate code (getters, setters, constructors, etc.).
*   **JUnit 5**: Testing framework for writing unit tests.
*   **Mockito**: Mocking framework for isolating units under test in unit tests.
*   **Testcontainers**: Provides lightweight, disposable instances of databases, web browsers, or anything else that can run in a Docker container. Essential for integration tests with a real PostgreSQL database.
*   **Selenium**: Browser automation framework for end-to-end UI testing.

## 4. Architectural Patterns

The application will follow a layered architecture:

*   **Presentation Layer (Controllers)**: Handles incoming HTTP requests, delegates business logic to the Service Layer, and prepares data for the View Layer (Thymeleaf).
*   **Service Layer (Services)**: Contains the core business logic. It orchestrates operations, interacts with the Data Access Layer, and applies business rules.
*   **Data Access Layer (Repositories/Entities)**: Responsible for interacting with the database. Spring Data JPA repositories will abstract away much of the boilerplate data access code. JPA Entities will represent the database tables.

## 5. Database Migration Strategy

The existing MySQL database schema will be migrated to PostgreSQL.

1.  **Schema Conversion**: The `mysql_query.sql` script will be analyzed, and equivalent DDL (Data Definition Language) statements for PostgreSQL will be created. Data types will be mapped appropriately (e.g., `LONGBLOB` in MySQL might map to `BYTEA` in PostgreSQL).
2.  **Data Migration**: If existing data needs to be preserved, a data migration strategy will be employed (e.g., using a tool like Flyway or Liquibase, or custom scripts). For this migration, we will focus on schema conversion and initial data population as provided in the `mysql_query.sql`.
3.  **JPA Entities**: JPA entities will be created based on the new PostgreSQL schema.

## 6. Testing Strategy

A comprehensive testing strategy will be implemented:

*   **Unit Tests**:
    *   Written using JUnit 5 and Mockito.
    *   Focus on testing individual components (e.g., Service methods, Controller methods) in isolation.
    *   Mock dependencies to ensure fast and reliable tests.
    *   Target >90% code coverage for business logic.
*   **Integration Tests**:
    *   Written using JUnit 5, Testcontainers, and Selenium.
    *   **Testcontainers**: Used to spin up a real PostgreSQL database instance for each test run, ensuring tests run against a clean, consistent database environment.
    *   **Selenium**: Used for end-to-end UI testing, simulating user interactions with the web application.
    *   Cover critical user journeys to ensure end-to-end functionality.

## 7. Security Considerations

*   **Spring Security**: Will be integrated for authentication and authorization.
    *   User authentication will be handled via Spring Security's form login.
    *   Role-based authorization will be implemented to restrict access to admin functionalities.
*   **Data Validation**: Input validation will be performed at the Controller and Service layers to prevent common vulnerabilities like SQL injection and cross-site scripting (XSS).
*   **Password Hashing**: User passwords will be stored securely using strong hashing algorithms (e.g., BCrypt) provided by Spring Security.
*   **Secure HTTP Headers**: Appropriate HTTP security headers will be configured to enhance application security.
