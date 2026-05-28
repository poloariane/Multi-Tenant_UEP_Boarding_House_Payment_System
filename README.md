# 🏠 UEP Multi-Tenant Boarding House Payment System

## 📋 Overview

A complete full-stack solution for managing boarding house payments digitally. This project includes:

- ✅ **Spring Boot REST API Backend** (Complete)
- 📱 **Frontend UI** (HTML/CSS/JavaScript - Ready for integration)
- 🗄️ **MySQL Database** (Schema provided)
- 🔐 **JWT Authentication** (Secure)
- 👥 **Multi-Tenant Support** (Data isolation)
- 💳 **Payment Management** (Tracking & reporting)
- 🔔 **Notification System** (Real-time alerts)

## 🎯 Project Status

| Component | Status | Notes |
|-----------|--------|-------|
| Backend API | ✅ Complete | 30+ endpoints ready |
| Database Schema | ✅ Complete | MySQL with auto-schema |
| Authentication | ✅ Complete | JWT with 24h validity |
| Multi-Tenancy | ✅ Complete | Owner ID isolation |
| Frontend Integration | ✅ Ready | config.js provided |
| Documentation | ✅ Complete | 5 comprehensive guides |

## 📁 Quick Navigation

### Getting Started
- **5 Minutes**: [QUICK_START.md](QUICK_START.md)
- **Setup & Deployment**: [BACKEND_SETUP.md](BACKEND_SETUP.md)
- **Frontend Integration**: [FRONTEND_INTEGRATION.md](FRONTEND_INTEGRATION.md)

### Documentation
- **Architecture**: [BACKEND_IMPLEMENTATION_SUMMARY.md](BACKEND_IMPLEMENTATION_SUMMARY.md)
- **Database Schema**: [init_database.sql](init_database.sql)
- **API Config**: [config.js](src/main/resources/static/assets/js/config.js)

## 🚀 Quick Start (5 Minutes)

### 1. Setup Database
```bash
mysql -u root -p
CREATE DATABASE uep_bhps;
mysql -u root -p uep_bhps < init_database.sql
```

### 2. Configure Backend
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_DB_PASSWORD
```

### 3. Run Backend
```bash
.\mvnw.cmd spring-boot:run
```

✅ Backend running on `http://localhost:8080`

### 4. Test API
```bash
curl http://localhost:8080/api/health
```

## 🔐 Authentication Flow

```
1. Register Owner → Get UUID (Owner ID)
        ↓
2. Login with credentials → Get JWT Token
        ↓
3. Include token in requests → Automatic data isolation
        ↓
4. Token expires in 24 hours → Re-login required
```

## 📊 API Endpoints Overview

```
Authentication (Public)
├── POST /api/auth/register/owner
├── POST /api/auth/login/owner
└── POST /api/auth/login/tenant

Tenants (Protected)
├── GET    /api/tenants
├── POST   /api/tenants
├── PUT    /api/tenants/{id}
└── DELETE /api/tenants/{id}

Rooms (Protected)
├── GET    /api/rooms
├── POST   /api/rooms
├── PUT    /api/rooms/{id}
└── DELETE /api/rooms/{id}

Payments (Protected)
├── GET    /api/payments
├── GET    /api/payments/status
├── GET    /api/payments/unpaid
├── GET    /api/payments/overdue
├── POST   /api/payments
└── PUT    /api/payments/{id}

Notifications (Protected)
├── GET    /api/notifications/user/{id}
├── POST   /api/notifications
└── PUT    /api/notifications/{id}/read

Health (Public)
├── GET /api/health
└── GET /api/info
```

## 💻 Frontend Integration

### 1. Include API Configuration
```html
<script src="/assets/js/config.js"></script>
```

### 2. Use API Functions
```javascript
// Login
const response = await loginOwner(username, password);
const token = response.token;

// Load Data
const tenants = await loadTenants();
const payments = await getPaymentStatus();

// Create Data
const newTenant = await addTenant(tenantData);

// Update Data
await updateTenant(id, updatedData);

// Send Notification
await sendNotification(userId, title, message, type);
```

### 3. Example Form Integration
```html
<form onsubmit="handleAddTenant(event)">
  <input type="text" id="firstName" placeholder="First Name" required>
  <input type="text" id="lastName" placeholder="Last Name" required>
  <input type="email" id="email" placeholder="Email" required>
  <input type="tel" id="contactNumber" placeholder="Phone">
  <button type="submit">Add Tenant</button>
</form>

<script>
async function handleAddTenant(event) {
  event.preventDefault();
  const tenant = await addTenant({
    firstName: document.getElementById('firstName').value,
    lastName: document.getElementById('lastName').value,
    email: document.getElementById('email').value,
    contactNumber: document.getElementById('contactNumber').value
  });
  if (tenant) {
    alert('Tenant added successfully!');
    loadTenants(); // Refresh list
  }
}
</script>
```

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────┐
│               Frontend (HTML/CSS/JS)            │
│         ├─ login.html                          │
│         ├─ dashboard.html                      │
│         ├─ add-tenant.html                     │
│         ├─ pay-rent.html                       │
│         ├─ config.js (API Client)              │
│         └─ assets/js/* (Utilities)             │
└──────────────────────┬──────────────────────────┘
                       │ HTTP/REST
┌──────────────────────▼──────────────────────────┐
│          Spring Boot REST API (8080)            │
│  ├─ Controllers (6)                            │
│  ├─ Services (5)                               │
│  ├─ Repositories (6)                           │
│  ├─ Security (JWT + CORS)                      │
│  └─ Exception Handling                         │
└──────────────────────┬──────────────────────────┘
                       │ JDBC/JPA
┌──────────────────────▼──────────────────────────┐
│       MySQL Database (uep_bhps)                 │
│  ├─ owners                                     │
│  ├─ users                                      │
│  ├─ tenants                                    │
│  ├─ rooms                                      │
│  ├─ payments                                   │
│  └─ notifications                              │
└─────────────────────────────────────────────────┘
```

## 🔑 Key Features

### 1. Multi-Tenancy
- Each boarding house owner gets unique `Owner ID` (UUID)
- Complete data isolation by Owner ID
- ThreadLocal-based context management
- Automatic filtering in all queries

### 2. Security
- JWT token-based authentication
- BCrypt password encryption
- Role-based access control (ADMIN/TENANT)
- CORS configuration for frontend communication
- Stateless architecture

### 3. Data Management
- Soft deletes (status flags) for audit trail
- Automatic timestamps (createdAt, updatedAt)
- Foreign key relationships
- Database indices for performance

### 4. Payment System
- Record rent payments
- Track payment status (PAID/UNPAID/OVERDUE)
- Generate payment reports
- Monthly payment tracking

### 5. Notifications
- Send payment reminders
- Confirm payment receipts
- General announcements
- Mark as read functionality

## 📊 Database Schema

### Owners Table
```sql
- id (Primary Key)
- ownerId (Unique UUID) ← Data Discriminator
- username, email, password
- boardingHouseName, address, contactNumber
- active, createdAt, updatedAt
```

### Users Table
```sql
- id (Primary Key)
- ownerId (Foreign Key) → Owner
- username, email, password
- role (ADMIN or TENANT)
- active, createdAt, updatedAt
```

### Tenants Table
```sql
- id (Primary Key)
- ownerId (Foreign Key) → Owner
- firstName, lastName, email
- contactNumber, address
- roomId (Foreign Key) → Room
- active, inactiveDate ← Soft Delete
```

### Rooms Table
```sql
- id (Primary Key)
- ownerId (Foreign Key) → Owner
- roomNumber, description
- monthlyRate, capacity
- active, createdAt, updatedAt
```

### Payments Table
```sql
- id (Primary Key)
- ownerId (Foreign Key) → Owner
- tenantId (Foreign Key) → Tenant
- amount, status (PAID/UNPAID/OVERDUE)
- paymentDate, dueDate, month
- notes, createdAt, updatedAt
```

### Notifications Table
```sql
- id (Primary Key)
- ownerId (Foreign Key) → Owner
- userId, title, message
- type (PAYMENT_REMINDER/CONFIRMATION/ANNOUNCEMENT)
- read (Flag), createdAt
```

## 🧪 Testing the API

### Example 1: Register & Login
```bash
# Register Owner
curl -X POST http://localhost:8080/api/auth/register/owner \
  -H "Content-Type: application/json" \
  -d '{
    "username":"owner1",
    "email":"owner@example.com",
    "password":"pass123",
    "boardingHouseName":"UEP BH"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login/owner \
  -H "Content-Type: application/json" \
  -d '{"username":"owner1","password":"pass123"}'

# Response includes: token, username, role, ownerId
```

### Example 2: Add Tenant
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

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

### Example 3: Record Payment
```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId":1,
    "amount":2500.00,
    "status":"PAID",
    "dueDate":"2026-05-31T23:59:59"
  }'
```

## 📝 Configuration

### application.properties
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/uep_bhps
spring.datasource.username=root
spring.datasource.password=PASSWORD

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=MySecretKeyForJWT...
jwt.expiration=86400000  (24 hours)
```

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| Frontend | HTML5, CSS3, JavaScript, Fetch API |
| Backend | Java 17, Spring Boot 4.0.6 |
| Web Framework | Spring Web |
| Security | Spring Security, JWT (jjwt) |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL 5.7+ |
| Build Tool | Maven |
| Code Generation | Lombok |

## 📦 Project Structure

```
uep-bhps/
├── src/
│   ├── main/
│   │   ├── java/com/uepbh/
│   │   │   ├── UepBhpsApplication.java
│   │   │   ├── controller/       (6 controllers)
│   │   │   ├── service/          (5 services)
│   │   │   ├── repository/       (6 repositories)
│   │   │   ├── entity/           (6 entities)
│   │   │   ├── dto/              (8 DTOs)
│   │   │   ├── config/           (Security config)
│   │   │   ├── filter/           (JWT filter)
│   │   │   ├── util/             (JWT, Context utilities)
│   │   │   └── exception/        (Error handling)
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── assets/js/config.js
│   └── test/
├── pom.xml
├── init_database.sql
├── QUICK_START.md
├── BACKEND_SETUP.md
├── FRONTEND_INTEGRATION.md
├── BACKEND_IMPLEMENTATION_SUMMARY.md
└── README.md (this file)
```

## ✨ What You Get

✅ **Production-Ready Backend**
- Fully functional REST API
- Security best practices
- Error handling
- Database migration ready

✅ **Complete Documentation**
- Setup guides
- API documentation
- Integration examples
- Troubleshooting tips

✅ **Frontend Integration Ready**
- JavaScript API client (`config.js`)
- Example HTML integration
- Form handling examples
- Token management

✅ **Database Schema**
- Multi-tenant design
- Optimized indexes
- Proper relationships
- Soft delete support

## 🚀 Deployment Checklist

- [ ] Database created and initialized
- [ ] `application.properties` configured
- [ ] Backend built and running
- [ ] API endpoints tested
- [ ] `config.js` included in frontend
- [ ] Frontend pages integrated
- [ ] Authentication working
- [ ] Forms submitting correctly
- [ ] Data displaying properly

## 📞 Support & Troubleshooting

### Backend Won't Start
1. Check Java version: `java -version` (should be 17+)
2. Check Maven installed: `mvn --version`
3. Check database connection in `application.properties`
4. Check MySQL is running

### Database Connection Error
1. Verify MySQL is running
2. Check credentials in `application.properties`
3. Verify database `uep_bhps` exists
4. Run `init_database.sql` if tables missing

### CORS Error from Frontend
1. Ensure backend running on port 8080
2. Verify `config.js` is included
3. Check token included in requests
4. See `FRONTEND_INTEGRATION.md` for details

### 401 Unauthorized Error
1. Token may have expired (24 hours)
2. User needs to login again
3. Check Authorization header format: `Bearer TOKEN`
4. Verify token not corrupted

## 📚 Documentation Files

| Document | Purpose |
|----------|---------|
| QUICK_START.md | Get started in 5 minutes |
| BACKEND_SETUP.md | Detailed setup guide |
| FRONTEND_INTEGRATION.md | Frontend integration |
| BACKEND_IMPLEMENTATION_SUMMARY.md | Architecture overview |
| init_database.sql | Database schema |
| config.js | Frontend API client |

## 🎓 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT Explained](https://jwt.io/introduction)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [RESTful API Design](https://restfulapi.net/)

## 📄 Project Information

- **Project Name**: UEP Multi-Tenant Boarding House Payment System
- **Version**: 1.0.0
- **Type**: Full-Stack Web Application
- **Institution**: University of Eastern Philippines
- **Department**: College of Science
- **Date Started**: March 6, 2026
- **Status**: ✅ Complete & Ready for Deployment

## 👥 Team

- Abundo, Clarissa Mae T.
- Cebuano, Irene A.
- Dela Cerna, Nicole M.
- Señar, Jennie N.
- Polo, Ariane C.
- Surio, Queensanta B.

## 📖 Next Steps

1. **Setup Backend** → Follow QUICK_START.md
2. **Test API** → Use curl or Postman
3. **Integrate Frontend** → Include config.js
4. **Build UI** → Update HTML forms
5. **Test Integration** → Verify data flow
6. **Deploy** → Follow BACKEND_SETUP.md deployment section

## 🎉 Ready to Go!

Your complete backend system is ready. Start integrating with your frontend!

```bash
mvn spring-boot:run
```

Happy coding! 🚀

---

**Last Updated**: May 17, 2026  
**Status**: ✅ Production Ready  
**Questions**: Refer to documentation or contact development team
