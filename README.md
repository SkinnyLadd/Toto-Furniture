# Furniture Shop Management System

A comprehensive inventory and workforce management system for a local furniture shop. This application is built with Spring Boot for the backend and JavaFX for the frontend.

## Features

- **Inventory Management**: Track furniture items, types, conditions, and refurbishment history
- **Sales & Customer Management**: Process orders, manage customers, and track payments
- **Procurement & Supplier Management**: Manage suppliers and purchase orders
- **Workforce Management**: Schedule staff shifts, track assignments, and calculate payroll
- **Reporting & Analytics**: Dashboard with key metrics and exportable reports
- **Security**: Role-based access control with JWT authentication

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Spring Data JPA, Spring Security
- **Frontend**: JavaFX 21
- **Database**: PostgreSQL
- **Build Tool**: Maven

## Project Structure

The project follows a standard Maven structure with Spring Boot and JavaFX integration (WIP):

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── furnitureshop/
│   │           ├── config/           # Spring configuration classes
│   │           ├── model/            # Entity classes
│   │           │   └── entity/       # JPA entities
│   │           ├── repository/       # Spring Data JPA repositories
│   │           ├── security/         # Security configuration and JWT handling
│   │           ├── service/          # Business logic services
│   │           └── ui/               # JavaFX UI components
│   │               ├── controller/   # JavaFX controllers
│   │               └── view/         # FXML view definitions
│   └── resources/
│       ├── fxml/                     # FXML layout files
│       ├── images/                   # Image resources
│       ├── styles/                   # CSS stylesheets
│       ├── application.properties    # Application configuration
│       └── schema.sql                # Database schema
└── test/                             # Test classes
```

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher


### Building the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/SkinnyLadd/Toto-Furniture.git
   cd totoApp
   ```

2. Build the application with Maven:
   ```bash
   mvn clean package
   ```

### Running the Application

1. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file directly:
   ```bash
   java -jar target/management-system-0.0.1-SNAPSHOT.jar
   ```

2. The application will start and display the login screen.

