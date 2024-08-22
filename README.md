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

`POST /token` - Generates and returns a JWT token for an authenticated user.

`GET /users` - Retrieves all users.

`GET /users/{id}` - Retrieves a specific user by their ID.

`GET /users/{userId}/tasks` - Retrieves all tasks associated with a specific user.

`POST /users` - Adds a new user.

`PUT /users/{id}` - Updates an existing user by their ID.

`DELETE /users/{id}` - Deletes a user by their ID.

`POST /users/{userId}/tasks/{taskId}` - Assigns a task to a user.

`DELETE /users/{userId}/tasks/{taskId}` - Unassigns a task from a user.

`GET /tasks` - Retrieves all tasks.

`GET /tasks/{id}` - Retrieves a specific task by its ID.

`GET /tasks/{taskId}/users` - Retrieves all users associated with a specific task.

`POST /tasks` - Adds a new task.

`PUT /tasks/{id}` - Updates an existing task by its ID.

`PATCH /tasks/{id}/complete` - Marks a task as complete.

`PATCH /tasks/{id}/uncomplete` - Marks a task as incomplete.

`DELETE /tasks/{id}` - Deletes a task by its ID.


