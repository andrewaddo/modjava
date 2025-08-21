# Technical Design for Spring Boot Migration

## 1. Introduction

This document outlines the technical design for migrating the legacy J2EE `shopping-cart` application to a modern Spring Boot application. The goal is to achieve full feature parity, enhance performance and security, and establish a production-grade application with a robust automated testing suite.

## 2. Project Structure

The new Spring Boot application will adopt a multi-module Maven project structure to promote modularity, separation of concerns, and easier management of dependencies. The proposed modules are:

*   **`web-app`**: This module will contain the Spring Boot application entry point, REST controllers, service layer, and Thymeleaf templates for server-side rendering. It will handle HTTP requests and responses.
*   **`data-access`**: This module will encapsulate all data persistence logic, including JPA entities, Spring Data JPA repositories, and database migration scripts. It will interact directly with the PostgreSQL database.
*   **`integration-tests`**: This module will house end-to-end integration tests using Selenium for UI interaction and Testcontainers for managing a real PostgreSQL database instance during testing. This module will be separate to ensure that integration tests can be run independently and do not interfere with unit tests or the main application build.

```
./mod3.out/
├── pom.xml (Parent POM)
├── web-app/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/shashi/webapp/
│       │   │   ├── ShoppingCartApplication.java
│       │   │   ├── controller/
│       │   │   ├── service/
│       │   │   └── model/ (DTOs, View Models)
│       │   └── resources/
│       │       ├── application.properties
│       │       └── templates/ (Thymeleaf HTMLs)
│       └── test/
│           └── java/com/shashi/webapp/
│               └── unit/ (Unit tests for controllers and services)
├── data-access/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/shashi/dataaccess/
│       │   │   ├── entity/
│       │   │   └── repository/
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── db/migration/ (Flyway/Liquibase scripts)
│       │       └── data.sql (Initial data)
│       └── test/
│           └── java/com/shashi/dataaccess/
│               └── unit/ (Unit tests for repositories)
└── integration-tests/
    ├── pom.xml
    └── src/
        └── test/
            ├── java/com/shashi/integrationtests/
            │   └── ShoppingCartIntegrationTest.java
            └── resources/
                └── application.properties
```

## 3. Key Libraries and Technologies

*   **Spring Boot**: The foundation for the new application, providing auto-configuration and a powerful ecosystem.
*   **Spring Web**: For building RESTful APIs and handling web requests (Controllers).
*   **Spring Data JPA**: For simplified data access and persistence using Java Persistence API (JPA) with Hibernate as the default implementation.
*   **Thymeleaf**: A modern server-side template engine for rendering HTML views, replacing JSP.
*   **Spring Security**: For robust authentication and authorization, replacing custom J2EE security mechanisms.
*   **PostgreSQL**: The target relational database, replacing MySQL. This will require a schema migration.
*   **JUnit 5**: The testing framework for writing unit tests.
*   **Mockito**: For mocking dependencies in unit tests.
*   **Testcontainers**: For providing lightweight, disposable instances of databases (PostgreSQL) and web browsers (Selenium) for integration tests.
*   **Selenium**: For automating browser interactions in end-to-end integration tests.
*   **Maven**: The build automation tool.
*   **Flyway/Liquibase (TBD)**: For managing database schema migrations. (Decision to be made during implementation, but Flyway is generally preferred for its simplicity).

## 4. Architectural Patterns

The application will follow a layered architecture:

*   **Controller Layer**: Handles incoming HTTP requests, delegates to the service layer, and returns appropriate HTTP responses. Will use Spring MVC annotations (`@RestController`, `@Controller`).
*   **Service Layer**: Contains the core business logic. Services will orchestrate operations, interact with repositories, and apply business rules. Annotated with `@Service`.
*   **Repository Layer**: Provides an abstraction over the data persistence mechanism. Uses Spring Data JPA interfaces (`JpaRepository`) for CRUD operations and custom queries. Annotated with `@Repository`.
*   **Entity Layer**: Plain Old Java Objects (POJOs) representing the database tables, annotated with JPA (`@Entity`, `@Table`, `@Id`, `@Column`).
*   **DTOs/View Models**: Data Transfer Objects or View Models will be used to transfer data between layers, especially between controllers and services, and for presenting data to the UI, ensuring separation of concerns and preventing direct exposure of entities.

## 5. Database Migration Strategy

The existing MySQL database will be migrated to PostgreSQL. The migration will involve:

1.  **Schema Conversion**: Converting MySQL-specific data types and syntax to PostgreSQL equivalents. This will be primarily handled by defining JPA entities and allowing Hibernate to generate the schema, or by writing explicit Flyway/Liquibase migration scripts.
2.  **Data Migration**: Transferring existing data from the MySQL database to the new PostgreSQL database. For the initial prototype, this might involve simple `INSERT` statements derived from the existing `mysql_query.sql` or manual data entry. For a production migration, a dedicated data migration tool or script would be used.
3.  **Connection Configuration**: Updating `application.properties` in the `data-access` module to connect to the PostgreSQL database.

## 6. Security Considerations

Spring Security will be integrated to handle authentication and authorization. Key aspects include:

*   **User Authentication**: Implementing user login and registration with secure password hashing (e.g., BCrypt).
*   **Authorization**: Defining roles (e.g., `USER`, `ADMIN`) and securing endpoints based on these roles.
*   **Session Management**: Spring Security will manage user sessions.
*   **CSRF Protection**: Built-in CSRF protection will be enabled.
*   **Input Validation**: All user inputs will be validated to prevent common vulnerabilities like SQL injection and XSS.

## 7. Testing Strategy

A multi-layered testing strategy will be employed:

*   **Unit Tests**: Written using JUnit 5 and Mockito for isolated testing of individual components (services, controllers, repositories). These tests will run fast and provide immediate feedback.
*   **Integration Tests**: Located in the `integration-tests` module, these will verify the interaction between different layers and external systems (database, browser). Testcontainers will be used to spin up real PostgreSQL instances for database interaction and Selenium for simulating user interactions in a browser.
*   **Test Data**: Test-specific data will be managed to ensure repeatable and reliable test execution.

## 8. Deployment Strategy (Cloud Run)

The application will be designed for deployment on Google Cloud Run. This involves:

*   **Containerization**: Packaging the Spring Boot application as a Docker image.
*   **Stateless Design**: Ensuring the application is stateless to leverage Cloud Run's scaling capabilities.
*   **Externalized Configuration**: Using Spring Boot's externalized configuration features to manage environment-specific properties (e.g., database connection strings, API keys) outside the Docker image.
*   **Logging and Monitoring**: Integrating with Google Cloud Logging and Monitoring for application observability.

## 9. Future Considerations

*   **Asynchronous Operations**: For email notifications or other long-running tasks, consider using Spring's `@Async` or message queues (e.g., Google Cloud Pub/Sub).
*   **Caching**: Implement caching (e.g., Spring Cache with Redis) for frequently accessed data to improve performance.
*   **API Documentation**: Use OpenAPI (Swagger) for documenting REST APIs.
*   **Error Handling**: Implement global exception handling for consistent error responses.
