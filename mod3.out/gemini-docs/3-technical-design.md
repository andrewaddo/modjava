# Technical Design for Spring Boot Modernization

This document outlines the technical design for migrating the legacy J2EE `shopping-cart` application to a modern Spring Boot application. The design emphasizes a layered architecture, robust testing, and cloud-native readiness.

## 1. Project Structure

The new application will be a multi-module Maven project, organized as follows:

```
./mod3.out/
├── pom.xml (Parent POM)
├── web-app/ (Spring Boot application for web layer, controllers, services)
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/shashi/webapp/
│       │   ├── controller/
│       │   ├── service/
│       │   └── config/ (Spring Security, WebConfig etc.)
│       └── main/resources/
│           ├── templates/ (Thymeleaf templates)
│           ├── static/ (CSS, JS, images)
│           └── application.properties
│       └── test/java/com/shashi/webapp/
│           ├── controller/
│           └── service/
├── data-access/ (Module for JPA entities and repositories)
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/shashi/dataaccess/
│       │   ├── entity/
│       │   └── repository/
│       └── test/java/com/shashi/dataaccess/
├── integration-tests/ (Module for end-to-end and integration tests)
│   ├── pom.xml
│   └── src/
│       └── test/java/com/shashi/integration/
│           ├── config/
│           └── tests/
└── gemini-docs/ (Documentation generated during migration)
    ├── 1-database-schema.md
    ├── 2-features/
    └── 3-technical-design.md
```

## 2. Key Libraries and Technologies

*   **Spring Boot**: Core framework for building standalone, production-grade Spring applications.
*   **Spring Web (Spring MVC)**: For building RESTful APIs and handling web requests. Will be used for controllers.
*   **Spring Data JPA**: For simplified data access layer using Java Persistence API (JPA) and Hibernate as the ORM.
*   **Thymeleaf**: Server-side template engine for rendering dynamic HTML content. Chosen for its natural templating and strong Spring integration.
*   **Spring Security**: For robust authentication and authorization mechanisms.
*   **JUnit 5**: Primary testing framework for unit tests.
*   **Mockito**: For mocking dependencies in unit tests.
*   **Testcontainers**: For providing lightweight, disposable instances of databases (PostgreSQL) and other services for integration tests.
*   **Selenium**: For automating browser interactions in end-to-end UI tests.
*   **PostgreSQL**: The target relational database for the modernized application.
*   **Maven**: Build automation tool.

## 3. Architectural Patterns

The application will follow a layered architecture:

*   **Presentation Layer (Controllers)**: Handles incoming HTTP requests, delegates business logic to services, and prepares responses (either JSON for APIs or rendered HTML via Thymeleaf).
*   **Service Layer (Services)**: Contains the core business logic. Services orchestrate operations, interact with the data access layer, and apply business rules. They are typically annotated with `@Service`.
*   **Data Access Layer (Repositories)**: Provides an abstraction over the persistence mechanism. Spring Data JPA repositories will handle CRUD operations and custom queries. Entities will represent the database tables.

## 4. Database Migration Strategy

The existing MySQL database will be migrated to PostgreSQL. The strategy involves:

1.  **Schema Conversion**: Manually converting the MySQL schema (from `mysql_query.sql`) to PostgreSQL-compatible DDL. This includes adjusting data types, auto-increment syntax, and foreign key constraints if necessary.
2.  **Data Migration**: For initial data, `INSERT` statements from `mysql_query.sql` will be adapted for PostgreSQL. For production data, a one-time migration script or tool will be used.
3.  **JPA Entity Mapping**: Creating JPA entities in the `data-access` module that accurately map to the new PostgreSQL table structure.
4.  **Flyway/Liquibase (Future Consideration)**: For managing database schema evolution in a production environment, a tool like Flyway or Liquibase will be integrated in a later phase to handle version-controlled migrations.

## 5. Testing Strategy

A comprehensive testing strategy will be employed:

*   **Unit Tests**: Written using JUnit 5 and Mockito for the service and controller layers, ensuring individual components function correctly in isolation. These tests will reside in the `web-app` and `data-access` modules.
*   **Integration Tests**: Written using JUnit 5, Testcontainers (for a real PostgreSQL instance), and Selenium (for UI interaction). These tests will cover end-to-end user journeys and interactions between different layers of the application. These tests will reside in the `integration-tests` module.

## 6. Cloud Run Deployment Considerations

The application will be designed with Cloud Run in mind:

*   **Statelessness**: Services will be designed to be stateless to allow for easy scaling and load balancing.
*   **Containerization**: A `Dockerfile` will be created for the `web-app` module to package the application into a Docker image.
*   **Configuration**: Externalized configuration using Spring Boot's `application.properties` (or `application.yml`) and environment variables for sensitive information.
*   **Logging**: Standardized logging to `stdout`/`stderr` for easy integration with Cloud Run's logging mechanisms.
*   **Health Checks**: Spring Boot Actuator will be used to expose health endpoints for Cloud Run's readiness and liveness probes.

This technical design provides a roadmap for the modernization effort, ensuring a robust, maintainable, and cloud-ready Spring Boot application.