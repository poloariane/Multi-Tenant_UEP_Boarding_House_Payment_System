-- UEP BHPS Database Initial Setup
-- Run this script after creating the database

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS uep_bhps;
USE uep_bhps;

-- Owners Table
CREATE TABLE IF NOT EXISTS owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(36) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    boarding_house_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_number VARCHAR(20),
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Users Table (Tenants and Admins)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(36) NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'TENANT') NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owners(owner_id)
);

-- Rooms Table
CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(36) NOT NULL,
    room_number VARCHAR(50) NOT NULL,
    description TEXT,
    monthly_rate DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owners(owner_id),
    UNIQUE KEY unique_room_per_owner (owner_id, room_number)
);

-- Tenants Table
CREATE TABLE IF NOT EXISTS tenants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contact_number VARCHAR(20),
    address VARCHAR(255),
    room_id BIGINT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    inactive_date TIMESTAMP NULL,
    FOREIGN KEY (owner_id) REFERENCES owners(owner_id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

-- Payments Table
CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(36) NOT NULL,
    tenant_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PAID', 'UNPAID', 'OVERDUE') NOT NULL,
    month INT,
    payment_date TIMESTAMP NULL,
    due_date TIMESTAMP NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owners(owner_id),
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(36) NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('PAYMENT_REMINDER', 'PAYMENT_CONFIRMATION', 'GENERAL_ANNOUNCEMENT') NOT NULL,
    read_flag BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owners(owner_id)
);

-- Create indexes for better performance
CREATE INDEX idx_owner_id ON owners(owner_id);
CREATE INDEX idx_users_owner_id ON users(owner_id);
CREATE INDEX idx_rooms_owner_id ON rooms(owner_id);
CREATE INDEX idx_tenants_owner_id ON tenants(owner_id);
CREATE INDEX idx_tenants_email ON tenants(email);
CREATE INDEX idx_payments_owner_id ON payments(owner_id);
CREATE INDEX idx_payments_tenant_id ON payments(tenant_id);
CREATE INDEX idx_notifications_owner_id ON notifications(owner_id);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
