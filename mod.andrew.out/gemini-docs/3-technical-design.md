# Technical Design: Shopping Cart Modernization

This document outlines the technical design for migrating the legacy J2EE `shopping-cart` application to a modern Spring Boot application.

## 1. Project Structure

The new application will be a multi-module Maven project with the following structure:

```
/mod.andrew.out
├── pom.xml
├── data-access
│   ├── pom.xml
│   └── src
├── service
│   ├── pom.xml
│   └── src
├── web-app
│   ├── pom.xml
│   └── src
└── integration-tests
    ├── pom.xml
    └── src
```

*   **`data-access`**: This module will contain all the components related to data persistence, including JPA entities and repositories.
*   **`service`**: This module will contain the core business logic of the application, including service classes and any business-specific exceptions.
*   **`web-app`**: This module will be the main entry point of the application. It will contain the Spring Boot application class, controllers for handling web requests, and Thymeleaf templates for the UI.
*   **`integration-tests`**: This module will contain end-to-end integration tests using Selenium and Testcontainers.

## 2. Key Libraries and Frameworks

*   **Spring Boot**: The core framework for building the application.
*   **Spring Web**: For building the web layer, including RESTful controllers.
*   **Spring Data JPA**: For simplifying data access with JPA and Hibernate.
*   **Thymeleaf**: For server-side rendering of the HTML UI.
*   **Spring Security**: For handling authentication and authorization.
*   **PostgreSQL Driver**: For connecting to the PostgreSQL database.
*   **JUnit 5**: For writing unit tests.
*   **Mockito**: For creating mock objects in unit tests.
*   **Testcontainers**: For running integration tests with a real PostgreSQL database in a Docker container.
*   **Selenium**: For browser automation in integration tests.
*   **Maven**: For dependency management and building the project.

## 3. Architecture

The application will follow a classic layered architecture:

*   **Controller Layer (`web-app`)**: This layer will be responsible for handling incoming HTTP requests, delegating to the service layer, and returning responses (either RESTful JSON or rendered HTML pages).
*   **Service Layer (`service`)**: This layer will contain the core business logic of the application. It will orchestrate calls to the repository layer and handle transactions.
*   **Repository Layer (`data-access`)**: This layer will be responsible for all communication with the database. It will use Spring Data JPA repositories to perform CRUD operations on the entities.

## 4. Database Migration

The existing MySQL database schema will be migrated to PostgreSQL. The `1-database-schema.md` document will be used as a reference for creating the new schema. The JPA entities in the `data-access` module will be annotated to match the PostgreSQL schema.

## 5. Testing Strategy

*   **Unit Tests**: Each service and controller will have corresponding unit tests using JUnit 5 and Mockito. These tests will run in isolation and will not require a running database or application server.
*   **Integration Tests**: The `integration-tests` module will contain end-to-end tests that simulate real user scenarios. These tests will use Selenium to interact with the application's UI and Testcontainers to spin up a PostgreSQL database for each test run.