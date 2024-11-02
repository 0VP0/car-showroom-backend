# Car Showrooms Server

A demo project for managing car showrooms using Spring Boot. This application utilizes Spring Data JPA, Spring Security,
PostgreSQL, and Flyway for database migrations.

## Project Structure

- **Spring Boot**: For building RESTful APIs.
- **Spring Security**: For securing API endpoints.
- **Spring Data JPA**: For data access and object-relational mapping.
- **PostgreSQL**: Database for storing car and showroom data.
- **Flyway**: For managing database migrations.
- **Lombok**: To reduce boilerplate code (make sure to enable annotation processing in your IDE).

## Prerequisites

- **Java 21** (set in the POM)
- **Maven** (for dependency management and build)
- **PostgreSQL** (configured for runtime)

## Features

### 1. Authentication (Bonus)

- **User Signup**: New users can create an account by providing necessary details.
- **User Login**: Users can log in with their credentials to receive a JWT for secure access to other endpoints.
- **JWT Token Generation**: JWT tokens are generated for authenticated sessions, which are required for accessing
  protected resources.

### 2. Car Management

- **Create Car**: Add a new car with details such as maker, model, VIN, and associated showroom.
- **List Cars**: Retrieve a list of cars with optional filters for attributes such as maker, showroom name, VIN, and
  model year. Results are paginated.
- **Delete Car (Bonus)**: Delete a specific car from the system based on its ID.

### 3. Car Showroom Management

- **Create Car Showroom**: Add a new car showroom with details.
- **Update Car Showroom**: Modify the details of an existing car showroom.
- **List Car Showrooms**: Retrieve a paginated list of all car showrooms.
- **Dropdown List for Car Showrooms**: Retrieve a simple list of all car showrooms, useful for dropdown selections in a
  frontend application.
- **Get Car Showroom by ID**: Retrieve details of a specific showroom.
- **Delete Car Showroom**: Remove a car showroom from the system by ID.

### 4. Admin Panel (Bonus)

- **List All Users**: Retrieve a paginated list of all users, sorted by ID in descending order.
- **Retrieve System Statistics**: Get system-wide stats for administrative purposes.
- **Update User Status**: Update the status (enabled/disabled) of a user by their ID.

## Technologies Used

- **Spring Boot**: Main application framework.
- **Spring Security**: Used for authentication and authorization.
- **JWT**: JSON Web Token for secure user sessions.
- **Lombok**: Reduces boilerplate code with annotations.
- **Spring Data JPA**: Data persistence and repository support.
- **PostgreSQL**: Database used to store application data.

## Getting Started

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/0VP0/car-showroom-backend.git
    cd car-showroom-backend
    ```

2. **Configure Database**:
Ensure PostgreSQL is running.

   - Make sure Docker is installed on your machine.
   - Navigate to the project directory.
   - Run the following commands:
   
   This builds the database container.
   ```bash
     docker compose build
     ```
   
   This starts the database in a container. To stop it, run:
   ```bash
     docker compose up
     ```
   
   This stop the database container, the `-v` option removes the associated volumes, ensuring a clean stop.
   ```bash
    docker compose down -v
    ``` 

3. **Build and Run**:
   Build the application using Maven and run it:
   ```bash
   mvn clean install
   mvn spring-boot:run

4. **Database Migration**:

- Flyway will automatically handle migrations on application startup based on the scripts in
  src/main/resources/db/migration.

5. **Admin Account**:
   We are automatically creating admin account:
   ```
   email: admin@test.com
   password: admin
   ```
6. **Manage Cache**:
   Actuator to manage cache:
   ```
   http://localhost:8080/actuator/caches
   ```
6. **Swagger**:
   Access api docomention on this url:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```