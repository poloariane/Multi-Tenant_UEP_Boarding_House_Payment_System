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

    static setOwnerId(ownerId) {
        localStorage.setItem('ownerId', ownerId);
    }

    static getOwnerId() {
        return localStorage.getItem('ownerId');
    }

    static setUserRole(role) {
        localStorage.setItem('userRole', role);
    }

    static getUserRole() {
        return localStorage.getItem('userRole');
    }

    static clear() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('ownerId');
        localStorage.removeItem('userRole');
    }
}

// API Helper Function
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
            TokenManager.clear();
            window.location.href = '/login.html';
            return null;
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || `API Error: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API Call Error:', error);
        throw error;
    }
}

// ============== Authentication Functions ==============

async function loginOwner(username, password) {
    try {
        const response = await apiCall(API_ENDPOINTS.AUTH_LOGIN_OWNER, 'POST', {
            username,
            password
        });

        if (response && response.token) {
            TokenManager.setToken(response.token);
            TokenManager.setOwnerId(response.ownerId);
            TokenManager.setUserRole(response.role);
            return response;
        }
    } catch (error) {
        console.error('Login failed:', error);
        throw error;
    }
}

async function loginTenant(username, password) {
    try {
        const response = await apiCall(API_ENDPOINTS.AUTH_LOGIN_TENANT, 'POST', {
            username,
            password
        });

        if (response && response.token) {
            TokenManager.setToken(response.token);
            TokenManager.setOwnerId(response.ownerId);
            TokenManager.setUserRole(response.role);
            return response;
        }
    } catch (error) {
        console.error('Login failed:', error);
        throw error;
    }
}

async function registerOwner(ownerData) {
    try {
        return await apiCall(API_ENDPOINTS.AUTH_REGISTER_OWNER, 'POST', ownerData);
    } catch (error) {
        console.error('Registration failed:', error);
        throw error;
    }
}

function logout() {
    TokenManager.clear();
    window.location.href = '/login.html';
}

function isAuthenticated() {
    return TokenManager.hasToken();
}

function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = '/login.html';
    }
}

// ============== Tenant Functions ==============

async function loadTenants() {
    try {
        return await apiCall(API_ENDPOINTS.TENANTS, 'GET');
    } catch (error) {
        console.error('Failed to load tenants:', error);
        throw error;
    }
}

async function loadActiveTenants() {
    try {
        return await apiCall(API_ENDPOINTS.TENANTS_ACTIVE, 'GET');
    } catch (error) {
        console.error('Failed to load active tenants:', error);
        throw error;
    }
}

async function addTenant(tenantData) {
    try {
        return await apiCall(API_ENDPOINTS.TENANTS, 'POST', tenantData);
    } catch (error) {
        console.error('Failed to add tenant:', error);
        throw error;
    }
}

async function getTenant(id) {
    try {
        return await apiCall(`${API_ENDPOINTS.TENANTS}/${id}`, 'GET');
    } catch (error) {
        console.error('Failed to get tenant:', error);
        throw error;
    }
}

async function updateTenant(id, tenantData) {
    try {
        return await apiCall(`${API_ENDPOINTS.TENANTS}/${id}`, 'PUT', tenantData);
    } catch (error) {
        console.error('Failed to update tenant:', error);
        throw error;
    }
}

async function deactivateTenant(id) {
    try {
        return await apiCall(`${API_ENDPOINTS.TENANTS}/${id}`, 'DELETE');
    } catch (error) {
        console.error('Failed to deactivate tenant:', error);
        throw error;
    }
}

// ============== Room Functions ==============

async function loadRooms() {
    try {
        return await apiCall(API_ENDPOINTS.ROOMS, 'GET');
    } catch (error) {
        console.error('Failed to load rooms:', error);
        throw error;
    }
}

async function loadActiveRooms() {
    try {
        return await apiCall(API_ENDPOINTS.ROOMS_ACTIVE, 'GET');
    } catch (error) {
        console.error('Failed to load active rooms:', error);
        throw error;
    }
}

async function addRoom(roomData) {
    try {
        return await apiCall(API_ENDPOINTS.ROOMS, 'POST', roomData);
    } catch (error) {
        console.error('Failed to add room:', error);
        throw error;
    }
}

async function getRoom(id) {
    try {
        return await apiCall(`${API_ENDPOINTS.ROOMS}/${id}`, 'GET');
    } catch (error) {
        console.error('Failed to get room:', error);
        throw error;
    }
}

async function updateRoom(id, roomData) {
    try {
        return await apiCall(`${API_ENDPOINTS.ROOMS}/${id}`, 'PUT', roomData);
    } catch (error) {
        console.error('Failed to update room:', error);
        throw error;
    }
}

async function deleteRoom(id) {
    try {
        return await apiCall(`${API_ENDPOINTS.ROOMS}/${id}`, 'DELETE');
    } catch (error) {
        console.error('Failed to delete room:', error);
        throw error;
    }
}

// ============== Payment Functions ==============

async function loadPayments() {
    try {
        return await apiCall(API_ENDPOINTS.PAYMENTS, 'GET');
    } catch (error) {
        console.error('Failed to load payments:', error);
        throw error;
    }
}

async function getPaymentStatus() {
    try {
        return await apiCall(API_ENDPOINTS.PAYMENTS_STATUS, 'GET');
    } catch (error) {
        console.error('Failed to get payment status:', error);
        throw error;
    }
}

async function getUnpaidPayments() {
    try {
        return await apiCall(API_ENDPOINTS.PAYMENTS_UNPAID, 'GET');
    } catch (error) {
        console.error('Failed to get unpaid payments:', error);
        throw error;
    }
}

async function getOverduePayments() {
    try {
        return await apiCall(API_ENDPOINTS.PAYMENTS_OVERDUE, 'GET');
    } catch (error) {
        console.error('Failed to get overdue payments:', error);
        throw error;
    }
}

async function getTenantPayments(tenantId) {
    try {
        return await apiCall(`${API_ENDPOINTS.PAYMENTS}/tenant/${tenantId}`, 'GET');
    } catch (error) {
        console.error('Failed to get tenant payments:', error);
        throw error;
    }
}

async function recordPayment(paymentData) {
    try {
        return await apiCall(API_ENDPOINTS.PAYMENTS, 'POST', paymentData);
    } catch (error) {
        console.error('Failed to record payment:', error);
        throw error;
    }
}

async function updatePayment(id, paymentData) {
    try {
        return await apiCall(`${API_ENDPOINTS.PAYMENTS}/${id}`, 'PUT', paymentData);
    } catch (error) {
        console.error('Failed to update payment:', error);
        throw error;
    }
}

// ============== Notification Functions ==============

async function sendNotification(userId, title, message, type) {
    try {
        const url = new URL(API_ENDPOINTS.NOTIFICATIONS);
        url.searchParams.append('userId', userId);
        url.searchParams.append('title', title);
        url.searchParams.append('message', message);
        url.searchParams.append('type', type);
        return await apiCall(url.toString(), 'POST');
    } catch (error) {
        console.error('Failed to send notification:', error);
        throw error;
    }
}

async function getUserNotifications(userId) {
    try {
        return await apiCall(`${API_ENDPOINTS.NOTIFICATIONS}/user/${userId}`, 'GET');
    } catch (error) {
        console.error('Failed to get notifications:', error);
        throw error;
    }
}

async function getUnreadNotifications(userId) {
    try {
        return await apiCall(`${API_ENDPOINTS.NOTIFICATIONS}/user/${userId}/unread`, 'GET');
    } catch (error) {
        console.error('Failed to get unread notifications:', error);
        throw error;
    }
}

async function markNotificationAsRead(notificationId) {
    try {
        return await apiCall(`${API_ENDPOINTS.NOTIFICATIONS}/${notificationId}/read`, 'PUT');
    } catch (error) {
        console.error('Failed to mark notification as read:', error);
        throw error;
    }
}

async function markAllNotificationsAsRead(userId) {
    try {
        return await apiCall(`${API_ENDPOINTS.NOTIFICATIONS}/user/${userId}/read-all`, 'PUT');
    } catch (error) {
        console.error('Failed to mark all notifications as read:', error);
        throw error;
    }
}

// ============== Health Check ==============

async function checkBackendHealth() {
    try {
        return await apiCall(API_ENDPOINTS.HEALTH, 'GET');
    } catch (error) {
        console.error('Backend health check failed:', error);
        return null;
    }
}

// ============== Global Error Handler ==============

window.addEventListener('fetch', (event) => {
    if (event.response && event.response.status === 401) {
        TokenManager.clear();
        window.location.href = '/login.html';
    }
});

// Auto-check authentication on page load
document.addEventListener('DOMContentLoaded', () => {
    const publicPages = ['login', 'register', 'index'];
    const currentPage = window.location.pathname.split('/').pop().split('.')[0];
    
    if (!publicPages.includes(currentPage) && !isAuthenticated()) {
        window.location.href = '/login.html';
    }
});
