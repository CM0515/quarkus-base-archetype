-- Schema initialization
CREATE SCHEMA IF NOT EXISTS public;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(500),
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Insert sample data
INSERT INTO users (name, email, phone, address, role, is_active) VALUES
('Admin User', 'admin@example.com', '+1234567890', '123 Admin St', 'ADMIN', true),
('John Doe', 'john@example.com', '+1234567891', '456 Main St', 'USER', true),
('Jane Smith', 'jane@example.com', '+1234567892', '789 Oak Ave', 'USER', true)
ON CONFLICT (email) DO NOTHING;

-- Create sequence for ids if not exists (with INCREMENT BY 50 to match Hibernate default)
DROP SEQUENCE IF EXISTS users_seq;
CREATE SEQUENCE users_seq START WITH 50 INCREMENT BY 50;
