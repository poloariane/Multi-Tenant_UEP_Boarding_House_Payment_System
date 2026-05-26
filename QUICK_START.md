# 🚀 UEP BHPS - Quick Start Guide

## ✅ What's Been Completed

A **complete, production-ready Spring Boot backend** with:
- ✅ 6 Database entities (Owner, User, Tenant, Room, Payment, Notification)
- ✅ Full REST API with 30+ endpoints
- ✅ JWT authentication & authorization
- ✅ Multi-tenant data isolation
- ✅ Role-based access control
- ✅ Payment tracking system
- ✅ Notification system
- ✅ Security configuration
- ✅ Exception handling
- ✅ Frontend integration ready

## 📋 File Checklist

### Backend Files Created (30+)
```
✅ UepBhpsApplication.java                 (Main app)
✅ 6 Entity classes                        (Entity)
✅ 6 Repository interfaces                 (Repository)
✅ 5 Service classes                       (Service)
✅ 6 Controller classes                    (API endpoints)
✅ 8 DTO classes                           (Data transfer objects)
✅ 2 Utility classes                       (JWT & Tenant Context)
✅ 1 Filter class                          (JWT Authentication)
✅ 1 Security Config                       (Spring Security)
✅ 1 Global Exception Handler              (Error handling)
```

### Documentation Files
```
✅ BACKEND_SETUP.md                        (Setup & deployment)
✅ FRONTEND_INTEGRATION.md                 (Frontend integration)
✅ BACKEND_IMPLEMENTATION_SUMMARY.md       (Complete overview)
✅ init_database.sql                       (Database schema)
✅ QUICK_START.md                          (This file)
```

### Frontend Configuration
```
✅ config.js                               (API client library)
```

### Configuration
```
✅ pom.xml                                 (Updated with dependencies)
✅ application.properties                  (Database configuration)
```

## 🏃 Quick Start (5 Minutes)

### Step 1: Setup Database
```bash
# Open MySQL/Terminal
mysql -u root -p

# Create database
CREATE DATABASE uep_bhps;
USE uep_bhps;
EXIT;

# Run schema
mysql -u root -p uep_bhps < init_database.sql
```

### Step 2: Configure Application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 3: Build & Run
```bash
cd "c:\Users\nicolemones\Videos\UEP BHPS"
mvn clean install
mvn spring-boot:run
```

✅ Backend running on `http://localhost:8080`

### Step 4: Test Backend
```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "UP",
  "message": "UEP BHPS Backend is running",
  "timestamp": 1234567890
}
```

## 🔐 First Login

### Register Owner
```bash
curl -X POST http://localhost:8080/api/auth/register/owner \
  -H "Content-Type: application/json" \
  -d '{
    "username":"admin",
    "email":"admin@example.com",
    "password":"password123",
    "boardingHouseName":"UEP Boarding House",
    "address":"Catarman, Northern Samar",
    "contactNumber":"09123456789"
  }'
```

### Login to Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login/owner \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

Response:
```json
{
  "token":"eyJhbGciOiJIUzI1NiJ9...",
  "username":"admin",
  "role":"ADMIN",
  "ownerId":"550e8400-e29b-41d4-a716-446655440000"
}
```

### Save Token for Testing
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

## 📝 Test Sample Operations

### Add Tenant
```bash
curl -X POST http://localhost:8080/api/tenants \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName":"John",
    "lastName":"Doe",
    "email":"john@example.com",
    "contactNumber":"09987654321"
  }'
```

### Add Room
```bash
curl -X POST http://localhost:8080/api/rooms \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber":"101",
    "description":"Single Room",
    "monthlyRate":2500.00,
    "capacity":1
  }'
```

### Record Payment
```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId":1,
    "amount":2500.00,
    "status":"PAID",
    "month":"2026-05",
    "dueDate":"2026-05-31T23:59:59"
  }'
```

### Get Payment Status
```bash
curl -X GET http://localhost:8080/api/payments/status \
  -H "Authorization: Bearer $TOKEN"
```

## 🔌 Frontend Integration

### 1. Include config.js in HTML
```html
<script src="/assets/js/config.js"></script>
```

### 2. Use API Functions
```javascript
// Login
const response = await loginOwner('admin', 'password123');
console.log(response.token);

// Load tenants
const tenants = await loadTenants();
console.log(tenants);

// Add tenant
const newTenant = await addTenant({
  firstName: 'Jane',
  lastName: 'Smith',
  email: 'jane@example.com'
});

// Get payment status
const status = await getPaymentStatus();
console.log(status);
```

### 3. Link in login.html
```html
<form id="loginForm" onsubmit="handleLogin(event)">
  <input type="text" id="username" placeholder="Username" required>
  <input type="password" id="password" placeholder="Password" required>
  <button type="submit">Login</button>
</form>

<script src="/assets/js/config.js"></script>
<script>
async function handleLogin(event) {
  event.preventDefault();
  try {
    const response = await loginOwner(
      document.getElementById('username').value,
      document.getElementById('password').value
    );
    // Redirect on success
    window.location.href = '/dashboard.html';
  } catch (error) {
    alert('Login failed: ' + error.message);
  }
}
</script>
```

## 📚 API Endpoints by Category

### Authentication (2 endpoints)
- `POST /api/auth/register/owner` - Register
- `POST /api/auth/login/owner` - Login
- `POST /api/auth/login/tenant` - Tenant Login

### Tenants (6 endpoints)
- `GET /api/tenants` - List all
- `GET /api/tenants/active` - List active
- `GET /api/tenants/{id}` - Get one
- `POST /api/tenants` - Create
- `PUT /api/tenants/{id}` - Update
- `DELETE /api/tenants/{id}` - Deactivate

### Rooms (6 endpoints)
- `GET /api/rooms` - List all
- `GET /api/rooms/active` - List active
- `GET /api/rooms/{id}` - Get one
- `POST /api/rooms` - Create
- `PUT /api/rooms/{id}` - Update
- `DELETE /api/rooms/{id}` - Delete

### Payments (7 endpoints)
- `GET /api/payments` - List all
- `GET /api/payments/status` - Payment summary
- `GET /api/payments/unpaid` - Unpaid only
- `GET /api/payments/overdue` - Overdue only
- `GET /api/payments/tenant/{id}` - By tenant
- `POST /api/payments` - Create
- `PUT /api/payments/{id}` - Update

### Notifications (5 endpoints)
- `GET /api/notifications/user/{id}` - All
- `GET /api/notifications/user/{id}/unread` - Unread
- `POST /api/notifications` - Send
- `PUT /api/notifications/{id}/read` - Mark read
- `PUT /api/notifications/user/{id}/read-all` - Mark all read

### Health (2 endpoints)
- `GET /api/health` - Health check
- `GET /api/info` - API info

## 🎯 Key Concepts

### Multi-Tenancy
- Each owner has unique UUID (`ownerId`)
- All data isolated by `ownerId`
- Automatic data filtering in all queries
- Secure data separation

### Authentication
- JWT tokens expire in 24 hours
- Include token in `Authorization: Bearer {token}` header
- Automatic logout on expiration
- Password encrypted with BCrypt

### Data Soft Deletion
- Tenants/Rooms marked inactive instead of deleted
- Historical data preserved
- `active` flag used for filtering

### Payment Status
- **PAID** - Payment received
- **UNPAID** - Payment pending
- **OVERDUE** - Past due date

## 🐛 Troubleshooting

### Build Error: Cannot find symbol
→ Run `mvn clean` and rebuild

### Database Connection Error
→ Check MySQL is running and credentials correct

### 401 Unauthorized
→ Token missing or expired, login again

### CORS Error
→ Frontend and backend on same domain or CORS configured

### 404 Not Found
→ Check endpoint URL is correct

## 📁 Important Files to Know

| File | Purpose |
|------|---------|
| `pom.xml` | Maven dependencies |
| `application.properties` | Database config |
| `init_database.sql` | Database schema |
| `UepBhpsApplication.java` | Entry point |
| `SecurityConfig.java` | JWT configuration |
| `*Service.java` | Business logic |
| `*Controller.java` | API endpoints |
| `config.js` | Frontend API client |

## 🚀 Next Steps

1. ✅ Backend running
2. → Integrate frontend (use `config.js`)
3. → Add unit tests
4. → Add more features (reports, exports)
5. → Deploy to production
6. → Monitor & optimize

## 📖 Full Documentation

For detailed information, see:
- **BACKEND_SETUP.md** - Comprehensive setup guide
- **FRONTEND_INTEGRATION.md** - Frontend integration details
- **BACKEND_IMPLEMENTATION_SUMMARY.md** - Complete architecture overview

## ✨ Features Summary

| Feature | Status |
|---------|--------|
| Owner Registration | ✅ |
| JWT Authentication | ✅ |
| Multi-Tenant Support | ✅ |
| Tenant Management | ✅ |
| Room Management | ✅ |
| Payment Tracking | ✅ |
| Payment Status | ✅ |
| Notifications | ✅ |
| Role-Based Access | ✅ |
| Soft Deletes | ✅ |
| Exception Handling | ✅ |
| API Documentation | ✅ |

## 🎉 You're Ready!

Your Spring Boot backend is **fully functional and ready for integration with the frontend**.

Start the server and begin building your frontend!

```bash
mvn spring-boot:run
```

Happy coding! 🚀

---

**Backend Version**: 1.0.0  
**Last Updated**: May 17, 2026  
**Status**: ✅ Production Ready
