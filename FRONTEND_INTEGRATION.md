# UEP BHPS Frontend - Backend Integration Guide

## Overview
This guide explains how to connect the existing frontend (HTML/CSS/JavaScript) with the newly created Spring Boot backend.

## Backend URL Configuration

The backend API runs on `http://localhost:8080` by default.

### Frontend Configuration File
Create a file at `src/main/resources/static/assets/js/config.js`:

```javascript
// Backend API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API Endpoints
const API_ENDPOINTS = {
    // Authentication
    AUTH_LOGIN_OWNER: `${API_BASE_URL}/auth/login/owner`,
    AUTH_LOGIN_TENANT: `${API_BASE_URL}/auth/login/tenant`,
    AUTH_REGISTER_OWNER: `${API_BASE_URL}/auth/register/owner`,

    // Tenants
    TENANTS: `${API_BASE_URL}/tenants`,
    TENANTS_ACTIVE: `${API_BASE_URL}/tenants/active`,

    // Rooms
    ROOMS: `${API_BASE_URL}/rooms`,
    ROOMS_ACTIVE: `${API_BASE_URL}/rooms/active`,

    // Payments
    PAYMENTS: `${API_BASE_URL}/payments`,
    PAYMENTS_STATUS: `${API_BASE_URL}/payments/status`,
    PAYMENTS_UNPAID: `${API_BASE_URL}/payments/unpaid`,
    PAYMENTS_OVERDUE: `${API_BASE_URL}/payments/overdue`,

    // Notifications
    NOTIFICATIONS: `${API_BASE_URL}/notifications`,

    // Health
    HEALTH: `${API_BASE_URL}/health`,
};

// Token Management
class TokenManager {
    static setToken(token) {
        localStorage.setItem('authToken', token);
    }

    static getToken() {
        return localStorage.getItem('authToken');
    }

    static removeToken() {
        localStorage.removeItem('authToken');
    }

    static hasToken() {
        return !!localStorage.getItem('authToken');
    }
}

// API Helper Function
//
async function apiCall(endpoint, method = 'GET', data = null) {
    const token = TokenManager.getToken();
    const headers = {
        'Content-Type': 'application/json',
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
        method,
        headers,
    };

    if (data) {
        config.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(endpoint, config);
        
        if (response.status === 401) {
            TokenManager.removeToken();
            window.location.href = '/login.html';
            return null;
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'API Error');
        }

        return await response.json();
    } catch (error) {
        console.error('API Call Error:', error);
        throw error;
    }
}
```

## Update JavaScript Files

### Update `sidebar-active.js`
```javascript
// Keep your existing navigation logic
// Add token verification on page load

document.addEventListener('DOMContentLoaded', function() {
    // Check if user is authenticated
    if (!TokenManager.hasToken() && !window.location.pathname.includes('login') && !window.location.pathname.includes('register')) {
        window.location.href = '/login.html';
    }
});
```

### Update `javascript.js` - Login Example
```javascript
// Login function
async function loginOwner(username, password) {
    try {
        const response = await apiCall(API_ENDPOINTS.AUTH_LOGIN_OWNER, 'POST', {
            username,
            password
        });

        if (response && response.token) {
            TokenManager.setToken(response.token);
            localStorage.setItem('ownerId', response.ownerId);
            localStorage.setItem('userRole', response.role);
            window.location.href = '/dashboard.html';
        }
    } catch (error) {
        alert('Login failed: ' + error.message);
    }
}

// Logout function
function logout() {
    TokenManager.removeToken();
    localStorage.removeItem('ownerId');
    localStorage.removeItem('userRole');
    window.location.href = '/login.html';
}

// Load tenants example
async function loadTenants() {
    try {
        const tenants = await apiCall(API_ENDPOINTS.TENANTS, 'GET');
        // Update your UI with tenants data
        console.log('Tenants:', tenants);
    } catch (error) {
        console.error('Failed to load tenants:', error);
    }
}

// Add tenant example
async function addTenant(firstName, lastName, email, contactNumber, address) {
    try {
        const response = await apiCall(API_ENDPOINTS.TENANTS, 'POST', {
            firstName,
            lastName,
            email,
            contactNumber,
            address
        });
        return response;
    } catch (error) {
        console.error('Failed to add tenant:', error);
    }
}

// Get payment status
async function getPaymentStatus() {
    try {
        const statuses = await apiCall(API_ENDPOINTS.PAYMENTS_STATUS, 'GET');
        return statuses;
    } catch (error) {
        console.error('Failed to get payment status:', error);
    }
}

// Record payment
async function recordPayment(tenantId, amount, status, month, dueDate, notes) {
    try {
        const response = await apiCall(API_ENDPOINTS.PAYMENTS, 'POST', {
            tenantId,
            amount,
            status,
            month,
            dueDate,
            notes
        });
        return response;
    } catch (error) {
        console.error('Failed to record payment:', error);
    }
}

// Send notification
async function sendNotification(userId, title, message, type) {
    try {
        const response = await apiCall(
            `${API_ENDPOINTS.NOTIFICATIONS}?userId=${userId}&title=${title}&message=${message}&type=${type}`,
            'POST'
        );
        return response;
    } catch (error) {
        console.error('Failed to send notification:', error);
    }
}
```

## HTML Form Integration Examples

### Login Form (login.html)
```html
<form id="loginForm" onsubmit="handleLogin(event)">
    <input type="text" id="username" placeholder="Username" required>
    <input type="password" id="password" placeholder="Password" required>
    <button type="submit">Login</button>
</form>

<script>
async function handleLogin(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    await loginOwner(username, password);
}
</script>
```

### Add Tenant Form (add-tenant.html)
```html
<form id="addTenantForm" onsubmit="handleAddTenant(event)">
    <input type="text" id="firstName" placeholder="First Name" required>
    <input type="text" id="lastName" placeholder="Last Name" required>
    <input type="email" id="email" placeholder="Email" required>
    <input type="tel" id="contactNumber" placeholder="Contact Number">
    <input type="text" id="address" placeholder="Address">
    <button type="submit">Add Tenant</button>
</form>

<script>
async function handleAddTenant(event) {
    event.preventDefault();
    const tenant = await addTenant(
        document.getElementById('firstName').value,
        document.getElementById('lastName').value,
        document.getElementById('email').value,
        document.getElementById('contactNumber').value,
        document.getElementById('address').value
    );
    if (tenant) {
        alert('Tenant added successfully!');
        loadTenants(); // Refresh tenant list
    }
}
</script>
```

### Dashboard (dashboard.html)
```html
<div id="paymentStatus">
    <!-- Payment status will be loaded here -->
</div>

<script>
async function loadDashboard() {
    try {
        const status = await getPaymentStatus();
        const html = status.map(p => `
            <div class="payment-item">
                <h3>${p.tenantName}</h3>
                <p>Status: <span class="status ${p.status.toLowerCase()}">${p.status}</span></p>
                <p>Amount: ${p.amount}</p>
                <p>Due: ${p.dueDate}</p>
            </div>
        `).join('');
        document.getElementById('paymentStatus').innerHTML = html;
    } catch (error) {
        console.error('Failed to load dashboard:', error);
    }
}

// Load on page load
document.addEventListener('DOMContentLoaded', loadDashboard);
</script>
```

## CORS Configuration
The backend is already configured to accept requests from any origin. If you need to restrict CORS, modify `src/main/java/com/uepbh/config/SecurityConfig.java`:

```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://yourdomain.com"));
```

## Environment Variables (Optional)
Create `.env` file in the project root:
```
BACKEND_URL=http://localhost:8080
API_BASE_URL=http://localhost:8080/api
JWT_EXPIRATION=86400000
```

## Troubleshooting

### CORS Errors
- Ensure backend is running on port 8080
- Check browser console for detailed error messages
- Verify token is being sent in Authorization header

### 401 Unauthorized
- Token may have expired (24-hour validity)
- User needs to login again
- Check if Authorization header is properly formatted: `Bearer YOUR_TOKEN`

### 404 Not Found
- Verify endpoint URL is correct
- Check backend is running: `curl http://localhost:8080/api/health`
- Ensure route exists in controller

## Testing in Browser
```javascript
// Test API connection in browser console
fetch('http://localhost:8080/api/health')
  .then(r => r.json())
  .then(d => console.log(d))
  .catch(e => console.error(e));
```

## Next Steps
1. Replace static HTML with dynamic data from backend
2. Implement form validation on frontend
3. Add loading indicators during API calls
4. Implement error notifications
5. Add pagination for large datasets
6. Implement real-time notifications with WebSocket
