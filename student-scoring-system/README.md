# Student Scoring System

A Spring Boot web application for managing student scores in 5 subjects with comprehensive reporting capabilities including statistical analysis (mean, median, mode).

## Quick Start

### 1. **Run the Application**

```bash
# Build the JAR file first
mvn clean package -DskipTests

# Start all services with Docker Compose
docker-compose up --build
```

### 2. **Access Points**
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **pgAdmin**: http://localhost:8081

## Database Connection (pgAdmin)

### **Connect to PostgreSQL via pgAdmin**

1. **Access pgAdmin**: http://localhost:8081
2. **Login**:
    - Email: `admin@studentscoringapp.com`
    - Password: `admin`

3. **Add Server**:
    - Right-click "Servers" → "Register" → "Server"
    - **General Tab**: Name = `StudentScoring DB`
    - **Connection Tab**:
        - Host: `postgres`
        - Port: `5433`
        - Database: `studentscoring`
        - Username: `postgres`
        - Password: `password`

4. **View Tables**: Navigate to StudentScoring DB → Databases → studentscoring → Schemas → public → Tables

## API Endpoints Documentation

### Student Management

#### 1. **POST /api/v1/students** - Create Student
**Purpose**: Creates a new student with scores in 5 subjects  
**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "studentId": "ST001",
  "subjects": {
    "Mathematics": 85,
    "English": 90,
    "Science": 78,
    "History": 92,
    "Geography": 88
  }
}
```
**Response (201 Created)**:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "studentId": "ST001",
  "subjects": {
    "Mathematics": 85,
    "English": 90,
    "Science": 78,
    "History": 92,
    "Geography": 88
  },
  "createdAt": "2025-01-15T10:30:00",
  "updatedAt": "2025-01-15T10:30:00"
}
```
**Validation Rules**:
- First name and last name: 2-50 characters, required
- Email: Valid email format, unique
- Student ID: Unique (optional)
- Subject scores: 0-100 range

---

#### 2. **GET /api/v1/students/{id}** - Get Student by ID
**Purpose**: Retrieves a specific student's information and scores  
**Path Parameter**: `id` - Student's database ID  
**Response (200 OK)**:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "studentId": "ST001",
  "subjects": {
    "Mathematics": 85,
    "English": 90,
    "Science": 78,
    "History": 92,
    "Geography": 88
  },
  "createdAt": "2025-01-15T10:30:00",
  "updatedAt": "2025-01-15T10:30:00"
}
```
**Error Response (404 Not Found)**:
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Student not found with ID: 999"
}
```

---

#### 3. **PUT /api/v1/students/{id}** - Update Student
**Purpose**: Updates an existing student's information and scores  
**Path Parameter**: `id` - Student's database ID  
**Request Body**: Same format as POST request  
**Response (200 OK)**: Updated student object (same format as GET response)  
**Features**:
- Updates all student information
- Replaces all subject scores
- Validates email/studentId uniqueness (excluding current student)
- Returns updated timestamps

---

#### 4. **DELETE /api/v1/students/{id}** - Delete Student
**Purpose**: Permanently deletes a student and all their scores  
**Path Parameter**: `id` - Student's database ID  
**Response (204 No Content)**: Empty response body  
**Note**: This action is irreversible and cascades to delete all associated scores

---

#### 5. **GET /api/v1/students** - Get All Students (Paginated & Filtered)
**Purpose**: Retrieves all students with pagination, sorting, and filtering capabilities

**Query Parameters**:
- `page` (default: 0) - Page number (0-based)
- `size` (default: 10) - Number of items per page
- `sortBy` (default: "id") - Field to sort by
- `sortDir` (default: "asc") - Sort direction (asc/desc)
- `firstName` (optional) - Filter by first name (partial match)
- `lastName` (optional) - Filter by last name (partial match)
- `email` (optional) - Filter by email (partial match)

**Example Request**:
```
GET /api/v1/students?page=0&size=5&sortBy=firstName&sortDir=asc&firstName=John
```

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "studentId": "ST001",
      "subjects": {
        "Mathematics": 85,
        "English": 90,
        "Science": 78,
        "History": 92,
        "Geography": 88
      },
      "createdAt": "2025-01-15T10:30:00",
      "updatedAt": "2025-01-15T10:30:00"
    }
  ],
  "page": 0,
  "size": 5,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

---

### Report Generation

#### 6. **GET /api/v1/reports/student/{studentId}** - Generate Student Report
**Purpose**: Generates a comprehensive statistical report for a specific student  
**Path Parameter**: `studentId` - Student's database ID

**Response (200 OK)**:
```json
{
  "studentId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "subjectScores": {
    "Mathematics": 85,
    "English": 90,
    "Science": 78,
    "History": 92,
    "Geography": 88
  },
  "meanScore": 86.6,
  "medianScore": 88.0,
  "modeScore": 85,
  "totalScore": 433,
  "highestScore": 92,
  "lowestScore": 78
}
```

**Report Includes**:
- **Individual Subject Scores**: Score for each subject
- **Mean Score**: Average of all scores
- **Median Score**: Middle value when scores are sorted
- **Mode Score**: Most frequently occurring score
- **Total Score**: Sum of all scores
- **Highest Score**: Maximum score achieved
- **Lowest Score**: Minimum score achieved

---

#### 7. **GET /api/v1/reports/all** - Generate All Students Report (Paginated & Filtered)
**Purpose**: Generates reports for all students with pagination and filtering options

**Query Parameters**:
- `page` (default: 0) - Page number (0-based)
- `size` (default: 10) - Number of reports per page
- `sortBy` (default: "studentId") - Field to sort by
- `sortDir` (default: "asc") - Sort direction (asc/desc)
- `firstName` (optional) - Filter by first name
- `lastName` (optional) - Filter by last name
- `email` (optional) - Filter by email

**Example Request**:
```
GET /api/v1/reports/all?page=0&size=10&sortBy=meanScore&sortDir=desc&firstName=John
```

**Response (200 OK)**:
```json
{
  "content": [
    {
      "studentId": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "subjectScores": {
        "Mathematics": 85,
        "English": 90,
        "Science": 78,
        "History": 92,
        "Geography": 88
      },
      "meanScore": 86.6,
      "medianScore": 88.0,
      "modeScore": 85,
      "totalScore": 433,
      "highestScore": 92,
      "lowestScore": 78
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

**Use Cases**:
- View class performance overview
- Compare students' statistical performance
- Export filtered reports for specific groups
- Sort students by performance metrics

---


### **Using Swagger UI**
Visit http://localhost:8080/swagger-ui.html for interactive API testing with a user-friendly interface.

## Common HTTP Status Codes

- **200 OK**: Successful GET/PUT requests
- **201 Created**: Successful POST requests
- **204 No Content**: Successful DELETE requests
- **400 Bad Request**: Invalid request data or validation errors
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side errors

## Features

- ✅ Complete CRUD operations for students
- ✅ Score validation (0-100 range)
- ✅ Statistical calculations (mean, median, mode)
- ✅ Pagination and filtering
- ✅ Swagger/OpenAPI documentation
- ✅ Comprehensive error handling
- ✅ Unit tests with high coverage
- ✅ Docker containerization
- ✅ Database migrations with Hibernate

## Project Requirements Met

- **5 Subjects**: Mathematics, English, Science, History, Geography
- **PostgreSQL Database**: Complete with Docker setup
- **Reports**: Mean, median, mode calculations
- **Pagination**: All list endpoints support pagination
- **Data Validation**: Scores between 0-100, email validation
- **API Documentation**: Swagger/OpenAPI integration
- **Unit Tests**: Comprehensive test coverage
- **Docker Compose**: Complete deployment setup
- **pgAdmin**: Database management interface


### **Port Info**
- Application: 8080
- PostgreSQL: 5433
- pgAdmin: 8081
