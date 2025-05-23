# Furniture Shop Management System

A comprehensive inventory and workforce management system for a local furniture shop in Pakistan. This application is built with Spring Boot for the backend and JavaFX for the frontend.

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

The project follows a standard Maven structure with Spring Boot and JavaFX integration:

\`\`\`
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
\`\`\`

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Database Setup

1. Create a PostgreSQL database named `furniture_shop`:
   \`\`\`sql
   CREATE DATABASE furniture_shop;
   \`\`\`

2. Configure the database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/furniture_shop
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   \`\`\`

3. The application will automatically create the schema on first run using JPA, or you can manually run the `schema.sql` script.

### Building the Application

1. Clone the repository:
   \`\`\`bash
   git clone https://github.com/yourusername/furniture-shop-management.git
   cd furniture-shop-management
   \`\`\`

2. Build the application with Maven:
   \`\`\`bash
   mvn clean package
   \`\`\`

### Running the Application

1. Run the application using Maven:
   \`\`\`bash
   mvn spring-boot:run
   \`\`\`

   Or run the JAR file directly:
   \`\`\`bash
   java -jar target/management-system-0.0.1-SNAPSHOT.jar
   \`\`\`

2. The application will start and display the login screen.

## Default Credentials

The application comes with a default admin user:

- Username: `admin`
- Password: `admin123`

You should change this password after the first login.

## Development

### Adding Sample Data

You can add sample data by running the `data.sql` script or by using the application's UI.

### Running Tests

Run the tests using Maven:
\`\`\`bash
mvn test
\`\`\`

## License

This project is licensed under the MIT License - see the LICENSE file for details.
