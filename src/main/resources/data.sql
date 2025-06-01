-- PostgreSQL data initialization script for Furniture Shop Application

-- specify the schema
SET search_path TO public;

-- Disable foreign key constraints temporarily
SET session_replication_role = 'replica';

-- Clear existing data (in reverse order of dependencies)
DELETE FROM audit_logs;
DELETE FROM payments;
DELETE FROM order_line_items;
DELETE FROM sales_orders;
DELETE FROM purchase_order_items;
DELETE FROM purchase_orders;
DELETE FROM refurbishment_records;
DELETE FROM inventory_items;
DELETE FROM furniture_types;
DELETE FROM suppliers;
DELETE FROM customers;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM payroll_records;
DELETE FROM assignments;
DELETE FROM shifts;
DELETE FROM staff_members;

-- Reset sequence generators
ALTER SEQUENCE audit_logs_id_seq RESTART WITH 1;
ALTER SEQUENCE payments_id_seq RESTART WITH 1;
ALTER SEQUENCE order_line_items_id_seq RESTART WITH 1;
ALTER SEQUENCE sales_orders_id_seq RESTART WITH 1;
ALTER SEQUENCE purchase_order_items_id_seq RESTART WITH 1;
ALTER SEQUENCE purchase_orders_id_seq RESTART WITH 1;
ALTER SEQUENCE refurbishment_records_id_seq RESTART WITH 1;
ALTER SEQUENCE inventory_items_id_seq RESTART WITH 1;
ALTER SEQUENCE furniture_types_id_seq RESTART WITH 1;
ALTER SEQUENCE suppliers_id_seq RESTART WITH 1;
ALTER SEQUENCE customers_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE roles_id_seq RESTART WITH 1;
ALTER SEQUENCE payroll_records_id_seq RESTART WITH 1;
ALTER SEQUENCE assignments_id_seq RESTART WITH 1;
ALTER SEQUENCE shifts_id_seq RESTART WITH 1;
ALTER SEQUENCE staff_members_id_seq RESTART WITH 1;

-- Re-enable foreign key constraints
SET session_replication_role = 'origin';

-- Roles
INSERT INTO roles (id, name, description) VALUES 
(1, 'ROLE_ADMIN', 'Administrator with full access'),
(2, 'ROLE_MANAGER', 'Manager with access to most features'),
(3, 'ROLE_SALES', 'Sales staff with access to sales features'),
(4, 'ROLE_INVENTORY', 'Inventory staff with access to inventory features'),
(5, 'ROLE_STAFF', 'General staff with limited access');

-- Users (password is 'password' encrypted)
INSERT INTO users (id, username, password, full_name, email, phone_number, active) VALUES 
(1, 'admin', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'Admin User', 'admin@furnitureshop.com', '03001234567', true),
(2, 'manager', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'Manager User', 'manager@furnitureshop.com', '03001234568', true),
(3, 'sales', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'Sales User', 'sales@furnitureshop.com', '03001234569', true),
(4, 'inventory', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'Inventory User', 'inventory@furnitureshop.com', '03001234570', true),
(5, 'staff', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'Staff User', 'staff@furnitureshop.com', '03001234571', true);

-- User Roles
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1), -- admin has ROLE_ADMIN
(2, 2), -- manager has ROLE_MANAGER
(3, 3), -- sales has ROLE_SALES
(4, 4), -- inventory has ROLE_INVENTORY
(5, 5); -- staff has ROLE_STAFF

-- Customers
INSERT INTO customers (id, full_name, phone_number, email, address, city, credit_limit, outstanding_balance, registration_date, notes) VALUES 
(1, 'Ali Ahmed', '03001234572', 'ali@example.com', '123 Main St', 'Lahore', 50000.00, 0.00, '2025-05-15 10:00:00', 'Regular customer'),
(2, 'Fatima Khan', '03001234573', 'fatima@example.com', '456 Park Ave', 'Karachi', 100000.00, 25000.00, '2025-05-20 11:30:00', 'Premium customer'),
(3, 'Muhammad Usman', '03001234574', 'usman@example.com', '789 Garden Rd', 'Islamabad', 75000.00, 15000.00, '2025-05-10 09:15:00', 'New customer'),
(4, 'Ayesha Malik', '03001234575', 'ayesha@example.com', '101 Lake View', 'Lahore', 60000.00, 0.00, '2025-05-05 14:45:00', 'Referred by Ali'),
(5, 'Omar Farooq', '03001234576', 'omar@example.com', '202 Hill Top', 'Karachi', 80000.00, 30000.00, '2025-05-12 16:20:00', 'Corporate client');

-- Suppliers
INSERT INTO suppliers (id, name, contact_person, phone_number, email, address, city, registration_date, payment_terms, notes, active) VALUES 
(1, 'Premium Wood Supplies', 'Zain Ali', '03001234577', 'zain@premiumwood.com', '111 Industrial Area', 'Lahore', '2025-05-10 09:00:00', 'Net 30', 'Quality wood supplier', true),
(2, 'Modern Furniture Materials', 'Sana Riaz', '03001234578', 'sana@modernfurniture.com', '222 Business Park', 'Karachi', '2025-05-15 10:30:00', 'Net 45', 'Supplies modern materials', true),
(3, 'Classic Upholstery', 'Kamran Ahmed', '03001234579', 'kamran@classicupholstery.com', '333 Textile Market', 'Faisalabad', '2025-05-20 11:45:00', 'Net 15', 'Specializes in upholstery', true),
(4, 'Hardware Solutions', 'Nadia Khan', '03001234580', 'nadia@hardwaresolutions.com', '444 Tools Street', 'Islamabad', '2025-05-25 13:15:00', 'Net 30', 'Hardware and fittings supplier', true),
(5, 'Eco-Friendly Materials', 'Imran Malik', '03001234581', 'imran@ecofriendly.com', '555 Green Road', 'Lahore', '2025-05-30 14:30:00', 'Net 60', 'Sustainable materials supplier', true);

-- Furniture Types
INSERT INTO furniture_types (id, name, description, category, base_price, price, dimensions, material, color, image_url, min_stock_level, reorder_quantity, stock_level) VALUES 
(1, 'Classic Dining Table', 'Elegant dining table for 6 people', 'Dining', 25000.00, 35000.00, '180x90x75 cm', 'Solid Oak', 'Brown', 'dining_table.jpg', 5, 10, 15),
(2, 'Modern Sofa', 'Contemporary 3-seater sofa', 'Living Room', 40000.00, 55000.00, '220x85x90 cm', 'Leather', 'Black', 'modern_sofa.jpg', 3, 5, 8),
(3, 'King Size Bed', 'Luxurious king size bed with storage', 'Bedroom', 60000.00, 80000.00, '200x180x120 cm', 'Sheesham Wood', 'Dark Brown', 'king_bed.jpg', 2, 4, 6),
(4, 'Office Desk', 'Professional office desk with drawers', 'Office', 20000.00, 28000.00, '150x70x75 cm', 'MDF with Veneer', 'Walnut', 'office_desk.jpg', 8, 15, 20),
(5, 'Bookshelf', 'Tall bookshelf with 5 shelves', 'Storage', 15000.00, 22000.00, '90x30x180 cm', 'Pine Wood', 'Natural', 'bookshelf.jpg', 10, 20, 25),
(6, 'Coffee Table', 'Round coffee table with glass top', 'Living Room', 12000.00, 18000.00, '90x90x45 cm', 'Metal and Glass', 'Silver', 'coffee_table.jpg', 7, 12, 15),
(7, 'Dining Chair', 'Comfortable dining chair with cushion', 'Dining', 5000.00, 7500.00, '45x50x95 cm', 'Solid Oak', 'Brown', 'dining_chair.jpg', 20, 30, 40),
(8, 'Wardrobe', 'Spacious 3-door wardrobe', 'Bedroom', 45000.00, 65000.00, '150x60x210 cm', 'Sheesham Wood', 'Dark Brown', 'wardrobe.jpg', 4, 8, 10);

-- Inventory Items
INSERT INTO inventory_items (id, furniture_type_id, serial_number, condition, location, purchase_price, acquisition_date, last_refurbishment_date, status, notes) VALUES 
(1, 1, 'DT001', 'New', 'Warehouse A', 20000.00, '2025-05-01 10:00:00', null, 'Available', 'In perfect condition'),
(2, 1, 'DT002', 'New', 'Warehouse A', 20000.00, '2025-05-01 10:00:00', null, 'Available', 'In perfect condition'),
(3, 2, 'MS001', 'New', 'Warehouse B', 35000.00, '2025-05-05 11:30:00', null, 'Available', 'Display model'),
(4, 2, 'MS002', 'New', 'Warehouse B', 35000.00, '2025-05-05 11:30:00', null, 'Available', 'In original packaging'),
(5, 3, 'KB001', 'New', 'Warehouse C', 50000.00, '2025-05-10 09:15:00', null, 'Available', 'Premium model'),
(6, 4, 'OD001', 'New', 'Warehouse A', 18000.00, '2025-05-15 14:45:00', null, 'Available', 'With cable management'),
(7, 5, 'BS001', 'New', 'Warehouse B', 12000.00, '2025-05-20 16:20:00', null, 'Available', 'Adjustable shelves'),
(8, 6, 'CT001', 'New', 'Warehouse C', 10000.00, '2025-05-25 10:00:00', null, 'Available', 'Tempered glass top'),
(9, 7, 'DC001', 'New', 'Warehouse A', 4000.00, '2025-05-30 11:30:00', null, 'Available', 'Stackable design'),
(10, 7, 'DC002', 'New', 'Warehouse A', 4000.00, '2025-05-30 11:30:00', null, 'Available', 'Stackable design'),
(11, 7, 'DC003', 'New', 'Warehouse A', 4000.00, '2025-05-30 11:30:00', null, 'Available', 'Stackable design'),
(12, 7, 'DC004', 'New', 'Warehouse A', 4000.00, '2025-05-30 11:30:00', null, 'Available', 'Stackable design'),
(13, 8, 'WD001', 'New', 'Warehouse C', 40000.00, '2025-05-05 09:15:00', null, 'Available', 'With mirror');

-- Staff Members
INSERT INTO staff_members (id, full_name, email, phone, address, department, position, hourly_rate, hire_date, is_active, created_date, updated_date) VALUES 
(1, 'Asad Khan', 'asad@furnitureshop.com', '03001234582', '123 Staff Housing', 'Sales', 'Senior Sales Executive', 500.00, '2025-05-15 09:00:00', true, '2025-05-15 09:00:00', '2025-05-15 09:00:00'),
(2, 'Zara Ali', 'zara@furnitureshop.com', '03001234583', '456 Staff Housing', 'Inventory', 'Inventory Manager', 600.00, '2025-05-20 10:30:00', true, '2025-05-20 10:30:00', '2025-05-20 10:30:00'),
(3, 'Bilal Ahmed', 'bilal@furnitureshop.com', '03001234584', '789 Staff Housing', 'Customer Service', 'Customer Service Representative', 400.00, '2025-05-25 11:45:00', true, '2025-05-25 11:45:00', '2025-05-25 11:45:00'),
(4, 'Hina Malik', 'hina@furnitureshop.com', '03001234585', '101 Staff Housing', 'Administration', 'Office Administrator', 450.00, '2025-05-30 13:15:00', true, '2025-05-30 13:15:00', '2025-05-30 13:15:00'),
(5, 'Faisal Raza', 'faisal@furnitureshop.com', '03001234586', '202 Staff Housing', 'Delivery', 'Delivery Team Lead', 550.00, '2025-05-05 14:30:00', true, '2025-05-05 14:30:00', '2025-05-05 14:30:00');

-- Shifts
INSERT INTO shifts (id, shift_name, start_time, end_time, description, is_active, created_date, updated_date) VALUES 
(1, 'Morning', '2025-05-01 09:00:00', '2025-05-01 17:00:00', 'Regular morning shift', true, '2025-05-25 10:00:00', '2025-05-25 10:00:00'),
(2, 'Evening', '2025-05-01 17:00:00', '2025-05-02 01:00:00', 'Regular evening shift', true, '2025-05-25 10:30:00', '2025-05-25 10:30:00'),
(3, 'Weekend', '2025-05-02 10:00:00', '2025-05-02 18:00:00', 'Weekend shift', true, '2025-05-25 11:00:00', '2025-05-25 11:00:00');

-- Assignments
INSERT INTO assignments (id, staff_id, shift_id, assigned_date, check_in_time, check_out_time, status, notes, created_date, updated_date) VALUES 
(1, 1, 1, '2025-05-01 08:30:00', '2025-05-01 08:55:00', '2025-05-01 17:05:00', 'Completed', 'On time', '2025-05-28 10:00:00', '2025-05-01 17:10:00'),
(2, 2, 1, '2025-05-01 08:30:00', '2025-05-01 08:50:00', '2025-05-01 17:00:00', 'Completed', 'On time', '2025-05-28 10:15:00', '2025-05-01 17:05:00'),
(3, 3, 2, '2025-05-01 16:30:00', '2025-05-01 16:45:00', '2025-05-02 01:10:00', 'Completed', 'Overtime 10 minutes', '2025-05-28 10:30:00', '2025-05-02 01:15:00'),
(4, 4, 3, '2025-05-02 09:30:00', '2025-05-02 09:45:00', '2025-05-02 18:00:00', 'Completed', 'On time', '2025-05-28 10:45:00', '2025-05-02 18:05:00'),
(5, 5, 3, '2025-05-02 09:30:00', '2025-05-02 09:50:00', '2025-05-02 18:10:00', 'Completed', 'Overtime 10 minutes', '2025-05-28 11:00:00', '2025-05-02 18:15:00');

-- Payroll Records
INSERT INTO payroll_records (id, staff_member_id, period_start, period_end, hours_worked, hourly_rate, gross_pay, net_pay, tax_deduction, other_deductions, bonus, is_paid, paid_date, generated_date, notes) VALUES 
(1, 1, '2025-05-01 00:00:00', '2025-05-15 23:59:59', 80, 500.00, 40000.00, 36000.00, 4000.00, 0.00, 0.00, true, '2025-05-16 10:00:00', '2025-05-16 09:00:00', 'Regular pay'),
(2, 2, '2025-05-01 00:00:00', '2025-05-15 23:59:59', 80, 600.00, 48000.00, 43200.00, 4800.00, 0.00, 0.00, true, '2025-05-16 10:15:00', '2025-05-16 09:15:00', 'Regular pay'),
(3, 3, '2025-05-01 00:00:00', '2025-05-15 23:59:59', 82, 400.00, 32800.00, 29520.00, 3280.00, 0.00, 0.00, true, '2025-05-16 10:30:00', '2025-05-16 09:30:00', 'Including 2 hours overtime'),
(4, 4, '2025-05-01 00:00:00', '2025-05-15 23:59:59', 80, 450.00, 36000.00, 32400.00, 3600.00, 0.00, 0.00, true, '2025-05-16 10:45:00', '2025-05-16 09:45:00', 'Regular pay'),
(5, 5, '2025-05-01 00:00:00', '2025-05-15 23:59:59', 81, 550.00, 44550.00, 40095.00, 4455.00, 0.00, 0.00, true, '2025-05-16 11:00:00', '2025-05-16 10:00:00', 'Including 1 hour overtime');

-- Sales Orders
INSERT INTO sales_orders (id, order_number, customer_id, order_date, updated_date, delivery_date, status, total_amount, discount_amount, tax_amount, final_amount, created_by, notes) VALUES 
(1, 'SO-2025-001', 1, '2025-05-10 10:00:00', '2025-05-10 10:30:00', '2025-05-15 14:00:00', 'Delivered', 35000.00, 0.00, 5950.00, 40950.00, 3, 'First purchase'),
(2, 'SO-2025-002', 2, '2025-05-12 11:30:00', '2025-05-12 12:00:00', '2025-05-17 15:30:00', 'Delivered', 55000.00, 5000.00, 8500.00, 58500.00, 1, 'Repeat customer'),
(3, 'SO-2025-003', 3, '2025-05-15 09:15:00', '2025-05-15 09:45:00', '2025-05-20 13:00:00', 'Confirmed', 80000.00, 0.00, 13600.00, 93600.00, 1, 'Special order'),
(4, 'SO-2025-004', 4, '2025-05-18 14:45:00', '2025-05-18 15:15:00', '2025-05-23 16:00:00', 'Pending', 28000.00, 0.00, 4760.00, 32760.00, 3, 'Standard delivery'),
(5, 'SO-2025-005', 5, '2025-05-20 16:20:00', '2025-05-20 16:50:00', '2025-05-25 10:00:00', 'Pending', 110000.00, 10000.00, 17000.00, 117000.00, 1, 'Corporate order');

-- Order Line Items
INSERT INTO order_line_items (id, sales_order_id, inventory_item_id, quantity, unit_price, line_total, discount_amount, notes) VALUES 
(1, 1, 1, 1, 35000.00, 35000.00, 0.00, 'Standard dining table'),
(2, 2, 3, 1, 55000.00, 55000.00, 5000.00, 'Display model with discount'),
(3, 3, 5, 1, 80000.00, 80000.00, 0.00, 'Premium king size bed'),
(4, 4, 6, 1, 28000.00, 28000.00, 0.00, 'Standard office desk'),
(5, 5, 2, 1, 35000.00, 35000.00, 0.00, 'Additional dining table'),
(6, 5, 4, 1, 55000.00, 55000.00, 5000.00, 'Modern sofa with discount'),
(7, 5, 8, 1, 18000.00, 18000.00, 0.00, 'Coffee table'),
(8, 5, 13, 1, 65000.00, 65000.00, 5000.00, 'Wardrobe with discount');

-- Payments
INSERT INTO payments (id, sales_order_id, payment_date, amount, payment_method, reference_number, status, received_by, notes) VALUES 
(1, 1, '2025-05-10 10:30:00', 40950.00, 'Cash', 'PAY-2023-001', 'Completed', 3, 'Full payment'),
(2, 2, '2025-05-12 12:00:00', 30000.00, 'Credit Card', 'PAY-2023-002', 'Completed', 1, 'Partial payment'),
(3, 2, '2025-05-17 15:30:00', 28500.00, 'Bank Transfer', 'PAY-2023-003', 'Completed', 1, 'Final payment'),
(4, 3, '2025-05-15 09:45:00', 50000.00, 'Cash', 'PAY-2023-004', 'Completed', 1, 'Deposit'),
(5, 5, '2025-05-20 16:50:00', 117000.00, 'Bank Transfer', 'PAY-2023-005', 'Completed', 1, 'Full payment - corporate account');

-- Purchase Orders
INSERT INTO purchase_orders (id, order_number, supplier_id, order_date, expected_delivery_date, actual_delivery_date, status, total_amount, created_by, notes) VALUES 
(1, 'PO-2023-001', 1, '2025-05-01 09:00:00', '2025-05-10 14:00:00', '2025-05-10 14:30:00', 'Fully Received', 100000.00, 2, 'Regular order'),
(2, 'PO-2023-002', 2, '2025-05-05 10:30:00', '2025-05-15 15:30:00', '2025-05-15 16:00:00', 'Fully Received', 150000.00, 2, 'Bulk order'),
(3, 'PO-2023-003', 3, '2025-05-10 11:45:00', '2025-05-20 13:00:00', '2025-05-20 13:30:00', 'Fully Received', 80000.00, 2, 'Upholstery materials'),
(4, 'PO-2023-004', 4, '2025-05-15 13:15:00', '2025-05-25 16:00:00', '2025-05-25 16:30:00', 'Fully Received', 50000.00, 2, 'Hardware supplies'),
(5, 'PO-2023-005', 5, '2025-05-20 14:30:00', '2025-05-30 10:00:00', null, 'Sent', 120000.00, 2, 'Eco-friendly materials');

-- Purchase Order Items
INSERT INTO purchase_order_items (id, purchase_order_id, furniture_type_id, quantity, received_quantity, unit_price, line_total, notes) VALUES 
(1, 1, 1, 5, 5, 20000.00, 100000.00, 'Dining tables'),
(2, 2, 2, 3, 3, 35000.00, 105000.00, 'Modern sofas'),
(3, 2, 3, 1, 1, 45000.00, 45000.00, 'King size bed'),
(4, 3, 4, 4, 4, 20000.00, 80000.00, 'Office desks'),
(5, 4, 5, 4, 4, 12500.00, 50000.00, 'Bookshelves'),
(6, 5, 6, 5, 0, 10000.00, 50000.00, 'Coffee tables'),
(7, 5, 7, 10, 0, 4000.00, 40000.00, 'Dining chairs'),
(8, 5, 8, 1, 0, 30000.00, 30000.00, 'Wardrobe');

-- Refurbishment Records
INSERT INTO refurbishment_records (id, inventory_item_id, start_date, completion_date, estimated_completion_date, status, description, cost, work_performed, notes, assigned_technician) VALUES 
(1, 3, '2025-05-25 10:00:00', '2025-05-27 15:00:00', '2025-05-27 17:00:00', 'Completed', 'Minor scratches on surface', 2000.00, 'Sanding and polishing', 'Display model refurbished', 'Ahmed Ali'),
(2, 8, '2025-05-26 11:30:00', '2025-05-28 16:30:00', '2025-05-28 17:00:00', 'Completed', 'Glass top replacement', 3000.00, 'Replaced glass top', 'New tempered glass installed', 'Saad Khan'),
(3, 4, '2025-05-27 09:15:00', null, '2025-05-30 17:00:00', 'In Progress', 'Upholstery repair', 5000.00, 'Replacing fabric', 'Special fabric ordered', 'Nadia Malik');

-- Audit Logs
INSERT INTO audit_logs (id, action, entity_type, entity_id, details, timestamp, user_id, ip_address) VALUES 
(1, 'CREATE', 'SalesOrder', 1, 'Created sales order SO-2023-001', '2025-05-10 10:00:00', 3, '192.168.1.100'),
(2, 'UPDATE', 'SalesOrder', 1, 'Updated status to Delivered', '2025-05-15 14:30:00', 3, '192.168.1.100'),
(3, 'CREATE', 'Payment', 1, 'Received payment for SO-2023-001', '2025-05-10 10:30:00', 3, '192.168.1.100'),
(4, 'CREATE', 'PurchaseOrder', 1, 'Created purchase order PO-2023-001', '2025-05-01 09:00:00', 2, '192.168.1.101'),
(5, 'UPDATE', 'PurchaseOrder', 1, 'Updated status to Fully Received', '2025-05-10 14:30:00', 2, '192.168.1.101');
