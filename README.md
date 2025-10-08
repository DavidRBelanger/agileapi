# AgileAPI 🚀

A production-ready REST API for agile project management, built with modern Spring Boot and enterprise-grade security practices.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

##  Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Testing](#testing)
- [Configuration](#configuration)
- [Deployment](#deployment)
- [Project Structure](#project-structure)
- [Key Learnings](#key-learnings)

---

##  Overview

AgileAPI is a secure, multi-tenant REST API designed for managing agile software development workflows. The application demonstrates modern backend development practices including JWT authentication, layered architecture, comprehensive testing, and production-ready configuration management.

---

##  Features

### Core Functionality
-  **Multi-tenant Organization Management** - Complete data isolation between organizations
-  **User Management** - Secure registration, authentication, and profile management
-  **Project Management** - Create and manage multiple projects per organization
-  **Sprint Planning** - Time-boxed iterations with date validation
-  **Task Tracking** - Granular task management with priorities, statuses, and assignments
-  **Advanced Filtering** - Query tasks by status, priority, and assignee

### Security Features
-  **JWT Authentication** - Stateless, token-based authentication with configurable expiration
-  **BCrypt Password Hashing** - Industry-standard password security
-  **Organization-based Access Control** - Multi-tenant isolation at the database level
-  **Input Validation** - Comprehensive request validation with clear error messages
-  **Authorization Checks** - Every endpoint validates user permissions

### Developer Experience
-  **Comprehensive Unit Tests** - High test coverage with JUnit 5 and Mockito
-  **Input Validation** - Clear, actionable error messages for API consumers
-  **Persistent Storage** - File-based H2 database (easily swappable for PostgreSQL)
-  **API Documentation** - Detailed endpoint documentation with examples
-  **Environment Profiles** - Separate configurations for dev, test, and production

---

##  Tech Stack

### Backend Framework
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.3.4** - Industry-standard framework for enterprise Java
- **Spring Security** - Comprehensive security framework
- **Spring Data JPA** - Object-relational mapping and database abstraction
- **Hibernate** - JPA implementation with lazy loading and caching

### Security & Authentication
- **JJWT 0.11.5** - JSON Web Token creation and validation
- **BCrypt** - Adaptive password hashing algorithm

### Database
- **H2 Database** - Embedded database (development/demo)
- **PostgreSQL-ready** - Production configuration template included

### Testing
- **JUnit 5** - Modern testing framework
- **Mockito** - Mocking framework for unit tests
- **MockMvc** - Spring MVC testing support
- **AssertJ** - Fluent assertion library

### Build & Dependency Management
- **Maven 3.9+** - Project management and build automation

---

##  Architecture

### Layered Architecture
```
┌─────────────────────────────────────────┐
│           Controller Layer              │  ← REST endpoints, request/response handling
├─────────────────────────────────────────┤
│            Service Layer                │  ← Business logic, orchestration
├─────────────────────────────────────────┤
│          Repository Layer               │  ← Data access, JPA repositories
├─────────────────────────────────────────┤
│            Database Layer               │  ← H2/PostgreSQL
└─────────────────────────────────────────┘
```

### Key Design Patterns
- **Repository Pattern** - Abstraction over data access
- **DTO Pattern** - Separation of API contracts from domain models
- **Service Layer Pattern** - Centralized business logic
- **Dependency Injection** - Loose coupling via Spring IoC
- **Builder Pattern** - JWT token construction
- **Strategy Pattern** - Pluggable authentication mechanisms

### Data Model
```
Organization (1) ──── (*) Users
     │
     │ (1)
     │
     ▼ (*)
  Projects (1) ──── (*) Sprints (1) ──── (*) Tasks
                                              │
                                              ▼
                                         Assigned to User
```

### Security Flow
```
1. User Registration → BCrypt hash password → Create organization → Generate JWT
2. User Login → Validate credentials → Generate JWT with claims (userId, orgId)
3. API Request → Extract JWT → Validate signature → Load user → Check org access
4. Data Access → Filter by organization ID → Return only authorized data
```

---

##  Getting Started

### Prerequisites
- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.9+** (or use included wrapper `./mvnw`)
- **Git** (for cloning the repository)

### Quick Start (5 minutes)

1. **Clone the repository**
   ```bash
   git clone https://github.com/DavidRBelanger/agileapi.git
   cd agileapi
   ```

2. **Run the application**
   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw spring-boot:run
   
   # Or if you have Maven installed
   mvn spring-boot:run
   ```

3. **Verify it's running**
   ```bash
   curl http://localhost:8080/api/v1/health
   ```

4. **Test the API** (see [Testing the API](#testing-the-api) section)

The application will start on `http://localhost:8080`

### Building from Source

```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Package as JAR
./mvnw clean package

# Run the packaged JAR
java -jar target/agileapi-0.0.1-SNAPSHOT.jar
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication Flow

#### 1. Register a New User
Creates a new user and their organization.

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securePassword123",
    "organizationName": "Acme Corporation",
    "organizationSlug": "acme-corp"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "organizationId": 1
}
```

#### 2. Login
Authenticate and receive a JWT token.

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

#### 3. Use the Token
Include the token in the `Authorization` header for all subsequent requests:

```bash
TOKEN="your-jwt-token-here"

curl -X GET http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN"
```

### Core Endpoints

#### Projects
```bash
# List all projects in your organization
GET /api/v1/projects

# Create a new project
POST /api/v1/projects
{
  "name": "Website Redesign",
  "description": "Complete overhaul of company website"
}

# Get project by ID
GET /api/v1/projects/{id}

# Update project
PATCH /api/v1/projects/{id}

# Delete project
DELETE /api/v1/projects/{id}
```

#### Sprints
```bash
# List sprints for a project
GET /api/v1/projects/{projectId}/sprints

# Create a sprint
POST /api/v1/projects/{projectId}/sprints
{
  "name": "Sprint 1",
  "startDate": "2025-10-01",
  "endDate": "2025-10-14"
}

# Get sprint by ID
GET /api/v1/sprints/{sprintId}

# Update sprint
PATCH /api/v1/sprints/{sprintId}

# Delete sprint
DELETE /api/v1/sprints/{sprintId}
```

#### Tasks
```bash
# List tasks in a sprint (with optional filters)
GET /api/v1/sprints/{sprintId}/tasks?status=TO_DO&priority=5&assigneeId=1

# Create a task
POST /api/v1/sprints/{sprintId}/tasks
{
  "title": "Design homepage mockup",
  "description": "Create initial design concepts",
  "priority": 5,
  "status": "TO_DO",
  "assigneeId": 1
}

# Get task by ID
GET /api/v1/tasks/{taskId}

# Update task
PATCH /api/v1/tasks/{taskId}

# Delete task
DELETE /api/v1/tasks/{taskId}
```

### Status Codes
| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 204 | No Content (successful deletion) |
| 400 | Bad Request (validation error) |
| 401 | Unauthorized (missing/invalid token) |
| 403 | Forbidden (insufficient permissions) |
| 404 | Not Found |
| 500 | Internal Server Error |

### Complete API Reference
For detailed documentation with all endpoints, request/response examples, and error codes, see:
- [endpoints.md](docs/endpoints.md) - Complete API reference
- [test-endpoints.sh](docs/test-endpoints.sh) - Automated test script

---

##  Security

### Authentication & Authorization

**JWT (JSON Web Tokens)**
- Tokens are signed with HS256 algorithm
- Include user ID and organization ID claims
- Default expiration: 24 hours (configurable)
- Stateless authentication (no server-side sessions)

**Password Security**
- BCrypt hashing with salt (adaptive cost factor)
- Minimum password length: 6 characters
- Never stored in plain text

**Multi-tenant Isolation**
- Every resource is scoped to an organization
- Users can only access data within their organization
- Authorization checks on every endpoint
- Database-level filtering prevents data leakage

### Input Validation

All inputs are validated before processing:

```java
// Example: Sprint validation
{
  "name": "Sprint 1",           // Required, 3-100 characters
  "startDate": "2025-10-01",    // Required, valid date
  "endDate": "2025-10-14"       // Required, must be after start date
}
```

**Validation Rules:**
- Email format validation
- Password strength requirements
- String length constraints
- Date range validation
- Priority bounds (1-5)
- Organization slug format (lowercase, numbers, hyphens only)

### Security Best Practices Implemented
 Password hashing with BCrypt  
 JWT tokens with expiration  
 Input validation on all endpoints  
 SQL injection prevention via JPA/Hibernate  
 CORS configuration  
 Stateless session management  
 Organization-based access control  
 Secure configuration management (secrets in environment variables)

---

##  Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProjectServiceTest

# Run with coverage report
./mvnw test jacoco:report
# Report: target/site/jacoco/index.html
```

### Test Coverage

- **Unit Tests**: Service layer business logic
- **Integration Tests**: Controller endpoints with MockMvc
- **Security Tests**: Authentication and authorization flows

**Test Statistics:**
- 7 test classes
- ~30 test cases
- Coverage of all major workflows

### Test Configuration

Tests use a separate in-memory H2 database:
- **Profile**: `test`
- **Database**: `jdbc:h2:mem:testdb`
- **Auto-cleanup**: Fresh database for each test run

### Example Test Structure

```java
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProjectServiceTest {
    
    @Mock
    private ProjectRepository projectRepository;
    
    @InjectMocks
    private ProjectService projectService;
    
    @Test
    void createProject_ShouldSaveAndReturn_WhenValid() {
        // Arrange
        Project project = new Project();
        project.setName("Test Project");
        
        // Act
        Project result = projectService.createNewProject(project, user);
        
        // Assert
        assertThat(result.getName()).isEqualTo("Test Project");
    }
}
```

For detailed test documentation, see [TEST_COVERAGE.md](docs/TEST_COVERAGE.md).

---

##  Configuration

### Application Profiles

The application supports multiple configuration profiles:

| Profile | Purpose | Database |
|---------|---------|----------|
| **default** | Development | H2 file-based (`./data/agiledb`) |
| **test** | Unit testing | H2 in-memory |
| **prod** | Production | PostgreSQL (configurable) |

### Environment Variables

**Development** (optional):
```bash
# All defaults are provided in application.properties
./mvnw spring-boot:run
```

**Production** (required):
```bash
# Set JWT secret via environment variable
export JWT_SECRET="your-256-bit-secret-key-here"
export JWT_EXPIRATION=86400000

# Optional: Database configuration
export DB_URL="jdbc:postgresql://localhost:5432/agileapi"
export DB_USERNAME="agileapi_user"
export DB_PASSWORD="secure_password"

# Run with production profile
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

### Configuration Files

```
src/main/resources/
├── application.properties              # Default (development) config
├── application-test.properties         # Test configuration
└── application-prod.properties.template # Production template (not committed)
```

### JWT Configuration

**Generate a secure JWT secret:**
```bash
openssl rand -base64 64
```

**Configuration:**
```properties
# application.properties
jwt.secret=your-secret-key-here
jwt.expiration=86400000  # 24 hours in milliseconds
```

### Database Configuration

**Development (H2 File-based):**
```properties
spring.datasource.url=jdbc:h2:file:./data/agiledb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

**Production (PostgreSQL):**
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.h2.console.enabled=false
```

**Access H2 Console** (development only):
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/agiledb
Username: sa
Password: (leave empty)
```

---

##  Deployment

### Local Deployment

1. **Build the JAR**
   ```bash
   ./mvnw clean package
   ```

2. **Run the JAR**
   ```bash
   java -jar target/agileapi-0.0.1-SNAPSHOT.jar
   ```

### Production Deployment Checklist

- [ ] Set `JWT_SECRET` environment variable with strong random key
- [ ] Configure production database (PostgreSQL recommended)
- [ ] Disable H2 console (`spring.h2.console.enabled=false`)
- [ ] Set `spring.profiles.active=prod`
- [ ] Review and set `jwt.expiration` appropriately
- [ ] Configure CORS for your frontend domain
- [ ] Set up SSL/TLS (HTTPS)
- [ ] Configure logging levels
- [ ] Set up monitoring and health checks

### Docker Deployment (Optional)

Create a `Dockerfile`:
```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/agileapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t agileapi .
docker run -p 8080:8080 \
  -e JWT_SECRET="your-secret" \
  -e SPRING_PROFILES_ACTIVE=prod \
  agileapi
```

### Cloud Deployment

Compatible with:
- **AWS Elastic Beanstalk** - Simple Java application deployment
- **Heroku** - Quick deployment with PostgreSQL add-on
- **Google Cloud Run** - Containerized deployment
- **Azure App Service** - Spring Boot native support
- **DigitalOcean App Platform** - Managed application hosting

---

##  Project Structure

```
agileapi/
├── src/
│   ├── main/
│   │   ├── java/com/dbelanger/spring/agileapi/
│   │   │   ├── auth/                    # Authentication controllers & services
│   │   │   │   ├── AuthController.java
│   │   │   │   └── AuthService.java
│   │   │   ├── controller/              # REST controllers
│   │   │   │   ├── ProjectController.java
│   │   │   │   ├── SprintController.java
│   │   │   │   ├── TaskController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/                     # Data Transfer Objects
│   │   │   │   ├── AuthRegisterRequest.java
│   │   │   │   ├── ProjectRequest.java
│   │   │   │   ├── SprintRequest.java
│   │   │   │   └── TaskRequest.java
│   │   │   ├── model/                   # Domain entities (JPA)
│   │   │   │   ├── Organization.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Project.java
│   │   │   │   ├── Sprint.java
│   │   │   │   └── Task.java
│   │   │   ├── repository/              # Data access layer (JPA)
│   │   │   │   ├── OrganizationRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProjectRepository.java
│   │   │   │   ├── SprintRepository.java
│   │   │   │   └── TaskRepository.java
│   │   │   ├── security/                # Security configuration
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── service/                 # Business logic layer
│   │   │   │   ├── OrganizationService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ProjectService.java
│   │   │   │   ├── SprintService.java
│   │   │   │   └── TaskService.java
│   │   │   ├── exception/               # Global exception handling
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── AgileapiApplication.java # Main application class
│   │   └── resources/
│   │       ├── application.properties              # Main config
│   │       ├── application-test.properties         # Test config
│   │       ├── application-prod.properties.template # Prod template
│   │       └── data.sql                            # Initial data (optional)
│   └── test/
│       └── java/com/dbelanger/spring/agileapi/
│           ├── auth/                    # Auth tests
│           │   ├── AuthServiceTest.java
│           │   └── AuthControllerTest.java
│           ├── controller/              # Controller tests
│           │   ├── ProjectControllerTest.java
│           │   ├── SprintControllerTest.java
│           │   └── TaskControllerTest.java
│           └── service/                 # Service tests
│               ├── ProjectServiceTest.java
│               ├── SprintServiceTest.java
│               ├── TaskServiceTest.java
│               └── UserServiceTest.java
├── docs/                               # Documentation
│   ├── endpoints.md                    # API reference
│   ├── models.md                       # Data models
│   ├── layerstack.md                   # Architecture
│   ├── TEST_COVERAGE.md                # Test documentation
│   ├── PAIN_POINTS_ANALYSIS.md         # Code review
│   ├── VALIDATION_AND_JWT_CHANGES.md   # Recent changes
│   └── test-endpoints.sh               # Automated test script
├── pom.xml                             # Maven configuration
└── README.md                           # This file
```

---

##  Key Learnings & Highlights

### Technical Challenges Solved

1. **Multi-tenant Data Isolation**
   - Implemented organization-based filtering at the repository level
   - Ensured no cross-tenant data leakage through authorization checks
   - Designed flexible service layer that enforces tenant boundaries

2. **Stateless Authentication**
   - Implemented JWT-based authentication without server-side sessions
   - Handled token generation, validation, and expiration
   - Included custom claims (userId, organizationId) for efficient authorization

3. **Input Validation**
   - Applied Jakarta Bean Validation for automatic request validation
   - Implemented custom cross-field validators (e.g., date ranges)
   - Provided clear, actionable error messages to API consumers

4. **Testing Strategy**
   - Separated test configuration with in-memory database
   - Used mocking for unit tests (Mockito) and integration tests (MockMvc)
   - Achieved comprehensive coverage without slow end-to-end tests

5. **Configuration Management**
   - Externalized secrets to environment variables
   - Created profile-specific configurations for different environments
   - Provided production-ready templates without committing secrets

### Design Decisions

**Why H2 Database?**
- Fast setup for demos and local development
- Easy to switch to PostgreSQL for production
- File-based mode provides persistence without external dependencies

**Why JWT over Sessions?**
- Stateless architecture enables horizontal scaling
- No server-side session storage required
- Mobile-friendly authentication approach

**Why Layered Architecture?**
- Clear separation of concerns
- Easy to test each layer independently
- Maintainable and extensible codebase

**Why DTOs?**
- Decouples API contracts from domain models
- Prevents over-fetching and under-fetching
- Enables input validation without polluting domain models

---

##  Contributing

This is a portfolio project, but suggestions and feedback are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

##  License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

##  Author

**David Belanger**

- GitHub: [@DavidRBelanger](https://github.com/DavidRBelanger)
- LinkedIn: [linkedin.com/in/davidrbelanger23](https://linkedin.com/in/davidrbelanger23)
- Email: davidrbelanger23@gmail.com

---

##  Acknowledgments

- Built with [Spring Boot](https://spring.io/projects/spring-boot)
- Secured with [JJWT](https://github.com/jwtk/jjwt)
- Tested with [JUnit 5](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/)

---

##  Contact & Questions

If you're a recruiter or hiring manager reviewing this project:

**Want to see it in action?** I can provide:
- Live demo deployment
- Video walkthrough
- Detailed architecture discussion
- Pair programming session

**Have questions about:**
- Design decisions?
- Technical implementation details?
- How I would scale this for production?
- Additional features I'd add with more time?

Feel free to reach out - I'd love to discuss this project!

---

<div align="center">

** If you find this project interesting, please give it a star! **

Made with ☕ and Spring Boot

</div>
