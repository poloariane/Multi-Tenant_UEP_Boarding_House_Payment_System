# UEP Multi-Tenant Boarding House Payment System - Backend Setup

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 5.7 or higher
- Git

## Database Setup

### 1. Create Database
```sql
CREATE DATABASE uep_bhps;
USE uep_bhps;
```

### 2. Tables will be auto-created by Hibernate
The application uses `spring.jpa.hibernate.ddl-auto=update` to automatically create and update tables based on entity definitions.

## Installation & Configuration

### 1. Clone the Project
```bash
cd c:\Users\nicolemones\Videos\UEP BHPS
```

### 2. Update Database Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/uep_bhps?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## API Endpoints

### Authentication
- **POST** `/api/auth/register/owner` - Register boarding house owner
- **POST** `/api/auth/login/owner` - Login as owner
- **POST** `/api/auth/login/tenant` - Login as tenant

### Tenants
- **POST** `/api/tenants` - Add new tenant
- **GET** `/api/tenants` - Get all tenants
- **GET** `/api/tenants/active` - Get active tenants only
- **GET** `/api/tenants/{id}` - Get tenant details
- **PUT** `/api/tenants/{id}` - Update tenant
- **DELETE** `/api/tenants/{id}` - Deactivate tenant

### Rooms
- **POST** `/api/rooms` - Add new room
- **GET** `/api/rooms` - Get all rooms
- **GET** `/api/rooms/active` - Get active rooms
- **GET** `/api/rooms/{id}` - Get room details
- **PUT** `/api/rooms/{id}` - Update room
- **DELETE** `/api/rooms/{id}` - Delete room

### Payments
- **POST** `/api/payments` - Record payment
- **GET** `/api/payments` - Get all payments
- **GET** `/api/payments/status` - Get payment status for all tenants
- **GET** `/api/payments/tenant/{tenantId}` - Get tenant payments
- **GET** `/api/payments/unpaid` - Get unpaid payments
- **GET** `/api/payments/overdue` - Get overdue payments
- **PUT** `/api/payments/{id}` - Update payment

### Notifications
- **POST** `/api/notifications` - Send notification
- **GET** `/api/notifications/user/{userId}` - Get user notifications
- **GET** `/api/notifications/user/{userId}/unread` - Get unread notifications
- **PUT** `/api/notifications/{id}/read` - Mark notification as read
- **PUT** `/api/notifications/user/{userId}/read-all` - Mark all as read

### Health
- **GET** `/api/health` - Check API health
- **GET** `/api/info` - Get API info

## Authentication

### Login Example
```bash
curl -X POST http://localhost:8080/api/auth/login/owner \
  -H "Content-Type: application/json" \
  -d '{
    "username": "owner1",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "owner1",
  "role": "ADMIN",
  "ownerId": "uuid-here"
}
```

### Using the Token
Add the token to the Authorization header for subsequent requests:
```bash
curl -X GET http://localhost:8080/api/tenants \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Project Structure

```
src/main/java/com/uepbh/
├── UepBhpsApplication.java       # Main application class
├── config/                        # Configuration classes
│   └── SecurityConfig.java       # Spring Security configuration
├── controller/                    # REST API controllers
│   ├── AuthController.java       # Authentication endpoints
│   ├── TenantController.java     # Tenant management
│   ├── RoomController.java       # Room management
│   ├── PaymentController.java    # Payment management
│   ├── NotificationController.java # Notifications
│   └── HealthController.java     # Health check
├── service/                       # Business logic
│   ├── AuthService.java          # Authentication logic
│   ├── TenantService.java        # Tenant operations
│   ├── RoomService.java          # Room operations
│   ├── PaymentService.java       # Payment operations
│   └── NotificationService.java  # Notification logic
├── repository/                    # Database access (JPA)
│   ├── OwnerRepository.java
│   ├── UserRepository.java
│   ├── TenantRepository.java
│   ├── RoomRepository.java
│   ├── PaymentRepository.java
│   └── NotificationRepository.java
├── entity/                        # Database entities
│   ├── Owner.java
│   ├── User.java
│   ├── Tenant.java
│   ├── Room.java
│   ├── Payment.java
│   └── Notification.java
├── dto/                           # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── TenantDTO.java
│   ├── RoomDTO.java
│   ├── PaymentDTO.java
│   ├── PaymentStatusDTO.java
│   └── NotificationDTO.java
├── util/                          # Utility classes
│   ├── JwtUtil.java              # JWT token generation
│   └── TenantContext.java        # Multi-tenant context
├── filter/                        # HTTP filters
│   └── JwtAuthenticationFilter.java
└── exception/                     # Exception handling
    ├── GlobalExceptionHandler.java
    └── ErrorResponse.java
```

## Key Features

### Multi-Tenancy
- Each boarding house owner gets a unique `Owner ID`
- Data isolation ensures owners only access their data
- Thread-safe context management via `TenantContext`

### Security
- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control (ADMIN, TENANT)
- CORS enabled for frontend communication

### Data Management
- Soft delete for tenants and rooms (status flags)
- Payment status tracking (PAID, UNPAID, OVERDUE)
- Comprehensive notification system
- Automated timestamps for all records

## Testing

### Register a New Owner
```bash
curl -X POST http://localhost:8080/api/auth/register/owner \
  -H "Content-Type: application/json" \
  -d '{
    "username": "owner1",
    "email": "owner@example.com",
    "password": "password123",
    "boardingHouseName": "UEP Boarding House",
    "address": "Catarman, Northern Samar",
    "contactNumber": "09123456789",
    "description": "Premium boarding house"
  }'
```

### Add a Tenant
```bash
curl -X POST http://localhost:8080/api/tenants \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "contactNumber": "09987654321",
    "address": "Room 101"
  }'
```

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials in `application.properties`
- Ensure database `uep_bhps` exists

### Build Failures
- Clear Maven cache: `mvn clean`
- Update dependencies: `mvn dependency:resolve`
- Check Java version: `java -version` (should be 17+)

### Runtime Issues
- Check logs in console for detailed error messages
- Verify JWT token is included in Authorization header
- Ensure token hasn't expired (24-hour validity)

## Support
For issues or questions, please contact the development team.
