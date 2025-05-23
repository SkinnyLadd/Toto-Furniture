

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- User roles mapping table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Furniture types table
CREATE TABLE IF NOT EXISTS furniture_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    dimensions VARCHAR(100),
    material VARCHAR(100),
    color VARCHAR(50),
    image_url VARCHAR(255),
    min_stock_level INT NOT NULL,
    reorder_quantity INT NOT NULL
);

-- Inventory items table
CREATE TABLE IF NOT EXISTS inventory_items (
    id SERIAL PRIMARY KEY,
    furniture_type_id BIGINT NOT NULL,
    serial_number VARCHAR(50) NOT NULL UNIQUE,
    condition VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL,
    purchase_price DECIMAL(10, 2) NOT NULL,
    acquisition_date TIMESTAMP NOT NULL,
    last_refurbishment_date TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    FOREIGN KEY (furniture_type_id) REFERENCES furniture_types(id)
);

-- Staff members table
CREATE TABLE IF NOT EXISTS staff_members (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    position VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    joining_date DATE NOT NULL,
    termination_date DATE,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    bank_account_details VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Refurbishment records table
CREATE TABLE IF NOT EXISTS refurbishment_records (
    id SERIAL PRIMARY KEY,
    inventory_item_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    completion_date TIMESTAMP,
    description TEXT NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    assigned_staff_id BIGINT,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id),
    FOREIGN KEY (assigned_staff_id) REFERENCES staff_members(id)
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(100),
    credit_limit DECIMAL(10, 2),
    outstanding_balance DECIMAL(10, 2) DEFAULT 0.00,
    registration_date TIMESTAMP NOT NULL,
    notes TEXT
);

-- Sales orders table
CREATE TABLE IF NOT EXISTS sales_orders (
    id SERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    delivery_date TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2),
    tax_amount DECIMAL(10, 2) NOT NULL,
    final_amount DECIMAL(10, 2) NOT NULL,
    created_by BIGINT,
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Order line items table
CREATE TABLE IF NOT EXISTS order_line_items (
    id SERIAL PRIMARY KEY,
    sales_order_id BIGINT NOT NULL,
    inventory_item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    line_total DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2),
    notes TEXT,
    FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id),
    FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id)
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    sales_order_id BIGINT NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    reference_number VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    received_by BIGINT,
    notes TEXT,
    FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id),
    FOREIGN KEY (received_by) REFERENCES users(id)
);

-- Suppliers table
CREATE TABLE IF NOT EXISTS suppliers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(100),
    registration_date TIMESTAMP NOT NULL,
    payment_terms VARCHAR(100),
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Purchase orders table
CREATE TABLE IF NOT EXISTS purchase_orders (
    id SERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    expected_delivery_date TIMESTAMP,
    actual_delivery_date TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_by BIGINT,
    notes TEXT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Purchase order items table
CREATE TABLE IF NOT EXISTS purchase_order_items (
    id SERIAL PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL,
    furniture_type_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    received_quantity INT NOT NULL DEFAULT 0,
    unit_price DECIMAL(10, 2) NOT NULL,
    line_total DECIMAL(10, 2) NOT NULL,
    notes TEXT,
    FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    FOREIGN KEY (furniture_type_id) REFERENCES furniture_types(id)
);

-- Shifts table
CREATE TABLE IF NOT EXISTS shifts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    description TEXT,
    recurring BOOLEAN NOT NULL DEFAULT FALSE,
    recurrence_pattern VARCHAR(50),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Assignments table
CREATE TABLE IF NOT EXISTS assignments (
    id SERIAL PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,
    assigned_date TIMESTAMP NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    FOREIGN KEY (staff_id) REFERENCES staff_members(id),
    FOREIGN KEY (shift_id) REFERENCES shifts(id)
);

-- Payroll records table
CREATE TABLE IF NOT EXISTS payroll_records (
    id SERIAL PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    pay_period_start DATE NOT NULL,
    pay_period_end DATE NOT NULL,
    hours_worked DECIMAL(10, 2) NOT NULL,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    gross_amount DECIMAL(10, 2) NOT NULL,
    tax_deductions DECIMAL(10, 2) NOT NULL,
    other_deductions DECIMAL(10, 2) NOT NULL,
    net_amount DECIMAL(10, 2) NOT NULL,
    processed_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    processed_by BIGINT,
    notes TEXT,
    FOREIGN KEY (staff_id) REFERENCES staff_members(id),
    FOREIGN KEY (processed_by) REFERENCES users(id)
);

-- Audit logs table
CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    details TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    user_id BIGINT,
    ip_address VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
