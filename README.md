# Cheffin Backend

This is the backend component of the Cheffin application - a platform to connect users with professional chefs.

## Overview

The Cheffin backend is built with Spring Boot and provides a robust API for:
- User authentication and authorization
- User and chef profile management
- Real-time messaging
- Database persistence

## Technology Stack

- Java 17
- Spring Boot 3.2.2
- Spring Security with JWT Authentication
- Spring Data JPA
- PostgreSQL Database
- WebSocket for real-time communication
- Lombok for reduced boilerplate code
- Docker for containerization

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8+
- PostgreSQL database (local or remote)
- Git

### Installation

```bash
# Navigate to the backend directory
cd Spring_BackEnd/Spring_BackEnd

# Build the application using the provided script (Windows)
.\build.bat

# Or manually set JAVA_HOME and build
set JAVA_HOME=C:\Path\To\JDK17
mvn clean package -DskipTests
```

### Configuration

The application is configured through `src/main/resources/application.properties`. Key configurations include:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://your-database-host:5432/your-db-name
spring.datasource.username=your-username
spring.datasource.password=your-password

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000
```

For development, you can use the provided configuration or set up your own local PostgreSQL database.

### Running the Application

```bash
# Using the provided script
.\build.bat

# Or manually
java -jar target/cheffin-0.0.1-SNAPSHOT.jar
```

The backend will be available at [http://localhost:8080](http://localhost:8080).

## Project Structure

```
Spring_BackEnd/
├── src/
│   └── main/
│       ├── java/com/example/cheffin/
│       │   ├── config/        # Application configuration
│       │   ├── controller/    # REST controllers
│       │   ├── dto/           # Data Transfer Objects
│       │   ├── model/         # Entity models
│       │   ├── repository/    # Data access layer
│       │   ├── security/      # Security configuration
│       │   ├── service/       # Business logic
│       │   └── websocket/     # WebSocket configuration
│       └── resources/
│           └── application.properties  # Application configuration
├── Dockerfile                # Docker configuration
├── pom.xml                   # Maven dependencies and build
├── build.bat                 # Build script for Windows
└── render-deploy.sh          # Deployment script for Render.com
```

## API Endpoints

### Authentication

#### `POST /users/login`
Authenticates a user and returns a JWT token.

**Headers:**
- `Content-Type: application/json`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "userPassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "user123",
    "email": "user@example.com",
    "profilePicture": "profile.jpg",
    "role": "USER",
    "specialty": null,
    "bio": null,
    "experience": null,
    "featured": false
  }
}
```

#### `POST /users/register`
Registers a new regular user.

**Headers:**
- `Content-Type: application/json`

**Request Body:**
```json
{
  "username": "user123",
  "email": "user@example.com",
  "password": "userPassword123",
  "profilePicture": "profile.jpg"
}
```

**Response:** Same as login response.

#### `POST /users/register/chef`
Registers a new chef user.

**Headers:**
- `Content-Type: application/json`

**Request Body:**
```json
{
  "username": "chef123",
  "email": "chef@example.com",
  "password": "chefPassword123",
  "profilePicture": "profile.jpg",
  "specialty": "Italian Cuisine",
  "bio": "Professional chef with 10 years of experience",
  "experience": 10
}
```

**Response:** Same as login response.

### User Management

#### `GET /users/profile`
Gets the profile of the currently authenticated user.

**Headers:**
- `Authorization: Bearer {jwt-token}`

**Response:**
```json
{
  "id": 1,
  "username": "user123",
  "email": "user@example.com",
  "profilePicture": "profile.jpg",
  "role": "USER",
  "specialty": null,
  "bio": null,
  "experience": null,
  "featured": false
}
```

#### `GET /users/profile/{username}`
Gets the profile of a specific user.

**Headers:**
- None required (public endpoint)

**Response:** Same format as above.

#### `PUT /users/profile`
Updates the profile of the currently authenticated user.

**Headers:**
- `Authorization: Bearer {jwt-token}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "username": "user123",
  "profilePicture": "new-profile.jpg",
  "specialty": "Desserts",
  "bio": "Food enthusiast",
  "experience": 2
}
```

**Response:** Updated user profile in the same format as GET responses.

#### `GET /users/chefs/featured`
Gets a list of featured chefs.

**Headers:**
- None required (public endpoint)

**Response:**
```json
[
  {
    "id": 2,
    "username": "chef123",
    "email": "chef@example.com",
    "profilePicture": "profile.jpg",
    "role": "CHEF",
    "specialty": "Italian Cuisine",
    "bio": "Professional chef with 10 years of experience",
    "experience": 10,
    "featured": true
  }
]
```

### Posts

#### `GET /post`
Gets all posts.

**Headers:**
- None required (public endpoint)

**Response:**
```json
[
  {
    "id": 1,
    "username": "chef123",
    "title": "Italian Pasta Masterclass",
    "description": "Learn how to make authentic Italian pasta from scratch",
    "contentImages": ["pasta1.jpg", "pasta2.jpg"],
    "createdAt": "2025-05-12T10:30:00"
  }
]
```

#### `POST /post`
Creates a new post.

**Headers:**
- `Authorization: Bearer {jwt-token}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "title": "Italian Pasta Masterclass",
  "description": "Learn how to make authentic Italian pasta from scratch",
  "contentImages": ["pasta1.jpg", "pasta2.jpg"]
}
```

**Response:** Created post in the same format as GET responses.

#### `GET /post/user/{username}`
Gets all posts by a specific user.

**Headers:**
- None required (public endpoint)

**Response:** Array of posts in the same format as the GET /post endpoint.

#### `PUT /post/{postId}`
Updates an existing post.

**Headers:**
- `Authorization: Bearer {jwt-token}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "title": "Updated Italian Pasta Masterclass",
  "description": "Updated description with more details",
  "contentImages": ["pasta1.jpg", "pasta2.jpg", "pasta3.jpg"]
}
```

**Response:** Updated post in the same format as GET responses.

#### `DELETE /post/{postId}`
Deletes a post.

**Headers:**
- `Authorization: Bearer {jwt-token}`

**Response:** HTTP 204 No Content

#### `GET /post/search?query={searchTerm}`
Search for posts by title or description.

**Headers:**
- None required (public endpoint)

**Response:** Array of posts in the same format as the GET /post endpoint.

### Chat

#### `GET /chat/conversations`
Gets all conversations for the current user.

**Headers:**
- `Authorization: Bearer {jwt-token}`

**Response:**
```json
[
  {
    "participantId": 2,
    "username": "chef123",
    "lastMessage": "When are you available for the cooking session?",
    "timestamp": "2025-05-13T14:25:00"
  }
]
```

#### `GET /chat/messages/{participantId}`
Gets all messages between the current user and another user.

**Headers:**
- `Authorization: Bearer {jwt-token}`

**Response:**
```json
[
  {
    "id": 1,
    "senderId": 1,
    "senderUsername": "user123",
    "recipientId": 2,
    "recipientUsername": "chef123",
    "content": "I'm interested in your cooking session",
    "timestamp": "2025-05-13T14:20:00",
    "read": true
  },
  {
    "id": 2,
    "senderId": 2,
    "senderUsername": "chef123",
    "recipientId": 1,
    "recipientUsername": "user123",
    "content": "When are you available for the cooking session?",
    "timestamp": "2025-05-13T14:25:00",
    "read": false
  }
]
```

#### `GET /chat/unread`
Gets all unread messages for the current user.

**Headers:**
- `Authorization: Bearer {jwt-token}`

**Response:** Array of chat messages in the same format as the GET /chat/messages/{participantId} endpoint.

### Chat Communication

The application supports two methods for chat communication:

#### HTTP Polling (Recommended)

A reliable polling-based approach for chat functionality:

**Send Message Endpoint:** `POST /chat/send`

**Headers:**
- `Authorization: Bearer {jwt-token}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "recipientUsername": "chef123",
  "content": "Hello, I'd like to book a cooking session"
}
```

**Response:**
```json
{
  "id": 3,
  "senderId": 1,
  "senderUsername": "user123",
  "recipientId": 2,
  "recipientUsername": "chef123",
  "content": "Hello, I'd like to book a cooking session",
  "timestamp": "2025-05-13T15:00:00",
  "read": false
}
```

**Recommended Polling Interval:** 10-15 seconds

The client should periodically poll these endpoints to get updated data:
- `GET /chat/conversations` - For the list of conversations
- `GET /chat/messages/{participantId}` - For messages in a specific conversation
- `GET /chat/unread` - For unread messages

#### WebSocket (Legacy Support)

The application also maintains WebSocket support for backward compatibility:

**Connection URL:** `/ws`

**STOMP Endpoints:**
- **Connect:** `/ws`
- **Subscribe to receive messages:** `/user/queue/messages`
- **Send message endpoint:** `/app/chat`

**Authentication:**
- Include the JWT token as a request parameter when connecting:
  ```
  /ws?token=eyJhbGciOiJIUzI1NiJ9...
  ```

**Message Format for Sending:**
```json
{
  "recipientUsername": "chef123",
  "content": "Hello, I'd like to book a cooking session"
}
```

## Deployment

### Docker Deployment

The application includes a Dockerfile for containerization:

```bash
# Build the Docker image
docker build -t cheffin-backend .

# Run the container
docker run -p 8080:8080 cheffin-backend
```

### Render.com Deployment

1. Push your code to a Git repository
2. Create a new Web Service on Render.com
3. Connect your Git repository
4. Set the build command: `./render-deploy.sh`
5. Set the start command: `java -jar target/cheffin-0.0.1-SNAPSHOT.jar`
6. Add required environment variables

## Learn More

For more detailed information about the entire Cheffin project, including the frontend implementation and deployment instructions, refer to the [main project README](../../README.md).