# Spring Boot CRUD Application

## Description
This is a basic CRUD (Create, Read, Update, Delete) application built with Spring Boot. It manages two entities: **Users** and **Tasks**, which have a many-to-many relationship. The application is secured using OAuth2 and JWT. Users must authenticate via the `/token` endpoint to obtain a Bearer token, which is required for accessing the API.

## Features
- **User Management**: Create, read, update, and delete users.
- **Task Management**: Create, read, update, and delete tasks.
- **Many-to-Many Relationship**: Users can have multiple tasks and tasks can belong to multiple users.
- **Security**: OAuth2 with JWT for authentication and authorization.

## Prerequisites
- **Java Development Kit (JDK) 11** or higher
- **Maven** (for dependency management)
- **An IDE** like IntelliJ IDEA, Eclipse, or Spring Tool Suite

## Setup Instructions
1. **Clone the repository**:
2. **Import the project into your preferred IDE as a Maven project**
3. **Run the file**

## Authentication & Authorization
**Obtain a Bearer Token:**
1. Send a POST request with your credentials (username and password) to the /token endpoint.
2. The response will include a JWT token.
3. Include the token in the Authorization header of your API requests:

```
Authorization: Bearer <your-token>
```

## Endpoints Overview
/users: CRUD operations for users \n
/tasks: CRUD operations for tasks \n
/token: Authentication endpoint to obtain the JWT \n

