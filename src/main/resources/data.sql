
-- Insert roles
INSERT INTO roles (name, description) VALUES
                                          ('ROLE_ADMIN', 'Administrator with full access'),
                                          ('ROLE_MANAGER', 'Manager with access to most features'),
                                          ('ROLE_SALES', 'Sales staff with access to sales features'),
                                          ('ROLE_TECHNICIAN', 'Technician with access to inventory and refurbishment');

-- Insert users (password: password123 - bcrypt encoded)
INSERT INTO users (username, password, full_name, email, phone_number, active) VALUES
                                                                                   ('admin', '$2a$10$yfIHMg3xrN.oJZ3ODSKnI.QLA4U7Ow3Oj8hQlv1Ot9IVJpohV1Ije', 'System Administrator', 'admin@furnitureshop.com', '03001234567', true),
                                                                                   ('manager', '$2a$10$yfIHMg3xrN.oJZ3ODSKnI.QLA4U7Ow3Oj8hQlv1Ot9IVJpohV1Ije', 'Asad Khan', 'asad@furnitureshop.com', '03002345678', true),
                                                                                   ('sales1', '$2a$10$yfIHMg3xrN.oJZ3ODSKnI.QLA4U7Ow3Oj8hQlv1Ot9IVJpohV1Ije', 'Fatima Ali', 'fatima@furnitureshop.com', '03003456789', true),
                                                                                   ('sales2', '$2a$10$yfIHMg3xrN.oJZ3ODSKnI.QLA4U7Ow3Oj8hQlv1Ot9IVJpohV1Ije', 'Zainab Malik', 'zainab@furnitureshop.com', '03004567890', true),
                                                                                   ('tech1', '$2a$10$yfIHMg3xrN.oJZ3ODSKnI.QLA4U7Ow3Oj8hQlv1Ot9IVJpohV1Ije', 'Usman Ahmed', 'usman@furnitureshop.com', '03005678901', true),
                                                                                   ('tech2', '$2a$10$yfIHMg3xrN.oJZ3ODSKnI.QLA4U7Ow3Oj8hQlv1Ot9IVJpohV1Ije', 'Bilal Hassan', 'bilal@furnitureshop.com', '03006789012', true);

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1), -- admin has ROLE_ADMIN
                                              (2, 2), -- manager has ROLE_MANAGER
                                              (3, 3), -- sales1 has ROLE_SALES
                                              (4, 3), -- sales2 has ROLE_SALES
                                              (5, 4), -- tech1 has ROLE_TECHNICIAN
                                              (6, 4); -- tech2 has ROLE_TECHNICIAN

-- Insert furniture categories
INSERT INTO furniture_types (name, description, category, base_price, dimensions, material, color, min_stock_level, reorder_quantity) VALUES
                                                                                                                                          ('Executive Desk', 'Premium wooden executive desk', 'Office', 25000.00, '180x90x75 cm', 'Solid Oak', 'Dark Brown', 5, 3),
                                                                                                                                          ('Office Chair', 'Ergonomic office chair with adjustable height', 'Office', 8000.00, '65x65x110 cm', 'Leather and Metal', 'Black', 10, 5),
                                                                                                                                          ('Bookshelf', 'Tall bookshelf with 5 shelves', 'Storage', 12000.00, '90x30x180 cm', 'Engineered Wood', 'Walnut', 8, 4),
                                                                                                                                          ('Dining Table', '6-seater dining table', 'Dining', 18000.00, '180x90x75 cm', 'Solid Sheesham', 'Natural', 3, 2),
                                                                                                                                          ('Dining Chair', 'Matching chair for dining sets', 'Dining', 3500.00, '45x45x95 cm', 'Solid Sheesham and Fabric', 'Natural/Beige', 15, 8),
                                                                                                                                          ('Sofa - 3 Seater', 'Comfortable 3-seater sofa', 'Living Room', 35000.00, '210x85x90 cm', 'Wood and Fabric', 'Grey', 3, 2),
                                                                                                                                          ('Coffee Table', 'Modern coffee table with storage', 'Living Room', 9000.00, '120x60x45 cm', 'Engineered Wood and Glass', 'White', 5, 3),
                                                                                                                                          ('King Bed', 'King size bed with headboard', 'Bedroom', 40000.00, '200x180x120 cm', 'Solid Wood', 'Dark Brown', 3, 2),
                                                                                                                                          ('Wardrobe', 'Large 3-door wardrobe with mirror', 'Bedroom', 30000.00, '150x60x210 cm', 'Engineered Wood', 'White', 3, 2),
                                                                                                                                          ('Side Table', 'Bedside table with drawer', 'Bedroom', 5000.00, '45x40x55 cm', 'Engineered Wood', 'Oak', 10, 5);

-- Insert sample suppliers
INSERT INTO suppliers (name, contact_person, phone_number, email, address, city, registration_date, payment_terms, active) VALUES
                                                                                                                               ('Lahore Wood Works', 'Ahmed Khan', '03001234567', 'ahmed@lahorewood.com', '123 Industrial Area', 'Lahore', NOW(), 'Net 30', true),
                                                                                                                               ('Karachi Furniture Supplies', 'Fatima Ali', '03009876543', 'fatima@karachifurniture.com', '456 Commercial Zone', 'Karachi', NOW(), 'Net 15', true),
                                                                                                                               ('Islamabad Timber', 'Usman Malik', '03331234567', 'usman@islamabadtimber.com', '789 Timber Market', 'Islamabad', NOW(), 'COD', true);

-- Insert sample customers
INSERT INTO customers (full_name, phone_number, email, address, city, credit_limit, outstanding_balance, registration_date) VALUES
                                                                                                                                ('Muhammad Ali', '03121234567', 'mali@example.com', '123 Main St', 'Lahore', 50000.00, 0.00, NOW()),
                                                                                                                                ('Ayesha Khan', '03339876543', 'akhan@example.com', '456 Park Ave', 'Karachi', 100000.00, 0.00, NOW()),
                                                                                                                                ('Imran Ahmed', '03451234567', 'iahmed@example.com', '789 Business Bay', 'Islamabad', 75000.00, 0.00, NOW()),
                                                                                                                                ('Sana Malik', '03002345678', 'smalik@example.com', '101 Garden Town', 'Lahore', 60000.00, 0.00, NOW()),
                                                                                                                                ('Tariq Mahmood', '03331122334', 'tmahmood@example.com', '202 DHA Phase 5', 'Karachi', 120000.00, 0.00, NOW()),
                                                                                                                                ('Nadia Hussain', '03009988776', 'nhussain@example.com', '303 F-10', 'Islamabad', 80000.00, 0.00, NOW());

-- Insert staff members
INSERT INTO staff_members (user_id, position, department, joining_date, hourly_rate, emergency_contact, emergency_phone, active) VALUES
                                                                                                                                     (2, 'Store Manager', 'Management', '2022-01-01', 1000.00, 'Amir Khan', '03001112233', true),
                                                                                                                                     (3, 'Sales Associate', 'Sales', '2022-02-15', 500.00, 'Saima Ali', '03002223344', true),
                                                                                                                                     (4, 'Sales Associate', 'Sales', '2022-03-10', 500.00, 'Hamza Malik', '03003334455', true),
                                                                                                                                     (5, 'Furniture Technician', 'Technical', '2022-01-20', 600.00, 'Yasir Ahmed', '03004445566', true),
                                                                                                                                     (6, 'Furniture Technician', 'Technical', '2022-04-05', 600.00, 'Kamran Hassan', '03005556677', true);

-- Insert inventory items
INSERT INTO inventory_items (furniture_type_id, serial_number, condition, location, purchase_price, acquisition_date, status, notes) VALUES
                                                                                                                                         (1, 'ED-001-2023', 'New', 'Showroom', 20000.00, '2023-01-15', 'Available', 'Display model'),
                                                                                                                                         (1, 'ED-002-2023', 'New', 'Warehouse', 20000.00, '2023-01-15', 'Available', NULL),
                                                                                                                                         (1, 'ED-003-2023', 'New', 'Warehouse', 20000.00, '2023-01-15', 'Available', NULL),
                                                                                                                                         (2, 'OC-001-2023', 'New', 'Showroom', 6000.00, '2023-01-20', 'Available', 'Display model'),
                                                                                                                                         (2, 'OC-002-2023', 'New', 'Warehouse', 6000.00, '2023-01-20', 'Available', NULL),
                                                                                                                                         (2, 'OC-003-2023', 'New', 'Warehouse', 6000.00, '2023-01-20', 'Available', NULL),
                                                                                                                                         (2, 'OC-004-2023', 'New', 'Warehouse', 6000.00, '2023-01-20', 'Available', NULL),
                                                                                                                                         (2, 'OC-005-2023', 'New', 'Warehouse', 6000.00, '2023-01-20', 'Available', NULL),
                                                                                                                                         (3, 'BS-001-2023', 'New', 'Showroom', 10000.00, '2023-02-05', 'Available', 'Display model'),
                                                                                                                                         (3, 'BS-002-2023', 'New', 'Warehouse', 10000.00, '2023-02-05', 'Available', NULL),
                                                                                                                                         (3, 'BS-003-2023', 'New', 'Warehouse', 10000.00, '2023-02-05', 'Available', NULL),
                                                                                                                                         (4, 'DT-001-2023', 'New', 'Showroom', 15000.00, '2023-02-10', 'Available', 'Display model'),
                                                                                                                                         (4, 'DT-002-2023', 'New', 'Warehouse', 15000.00, '2023-02-10', 'Available', NULL),
                                                                                                                                         (5, 'DC-001-2023', 'New', 'Showroom', 3000.00, '2023-02-10', 'Available', 'Display model'),
                                                                                                                                         (5, 'DC-002-2023', 'New', 'Showroom', 3000.00, '2023-02-10', 'Available', 'Display model'),
                                                                                                                                         (5, 'DC-003-2023', 'New', 'Warehouse', 3000.00, '2023-02-10', 'Available', NULL),
                                                                                                                                         (5, 'DC-004-2023', 'New', 'Warehouse', 3000.00, '2023-02-10', 'Available', NULL),
                                                                                                                                         (5, 'DC-005-2023', 'New', 'Warehouse', 3000.00, '2023-02-10', 'Available', NULL),
                                                                                                                                         (5, 'DC-006-2023', 'New', 'Warehouse', 3000.00, '2023-02-10', 'Available', NULL),
                                                                                                                                         (6, 'SF-001-2023', 'New', 'Showroom', 30000.00, '2023-03-01', 'Available', 'Display model'),
                                                                                                                                         (6, 'SF-002-2023', 'New', 'Warehouse', 30000.00, '2023-03-01', 'Available', NULL),
                                                                                                                                         (7, 'CT-001-2023', 'New', 'Showroom', 7500.00, '2023-03-05', 'Available', 'Display model'),
                                                                                                                                         (7, 'CT-002-2023', 'New', 'Warehouse', 7500.00, '2023-03-05', 'Available', NULL),
                                                                                                                                         (7, 'CT-003-2023', 'New', 'Warehouse', 7500.00, '2023-03-05', 'Available', NULL),
                                                                                                                                         (8, 'KB-001-2023', 'New', 'Showroom', 35000.00, '2023-03-15', 'Available', 'Display model'),
                                                                                                                                         (8, 'KB-002-2023', 'New', 'Warehouse', 35000.00, '2023-03-15', 'Available', NULL),
                                                                                                                                         (9, 'WD-001-2023', 'New', 'Showroom', 25000.00, '2023-03-20', 'Available', 'Display model'),
                                                                                                                                         (9, 'WD-002-2023', 'New', 'Warehouse', 25000.00, '2023-03-20', 'Available', NULL),
                                                                                                                                         (10, 'ST-001-2023', 'New', 'Showroom', 4000.00, '2023-03-25', 'Available', 'Display model'),
                                                                                                                                         (10, 'ST-002-2023', 'New', 'Showroom', 4000.00, '2023-03-25', 'Available', 'Display model'),
                                                                                                                                         (10, 'ST-003-2023', 'New', 'Warehouse', 4000.00, '2023-03-25', 'Available', NULL),
                                                                                                                                         (10, 'ST-004-2023', 'New', 'Warehouse', 4000.00, '2023-03-25', 'Available', NULL),
                                                                                                                                         (10, 'ST-005-2023', 'New', 'Warehouse', 4000.00, '2023-03-25', 'Available', NULL);

-- Insert some used/refurbished items
INSERT INTO inventory_items (furniture_type_id, serial_number, condition, location, purchase_price, acquisition_date, status, notes) VALUES
                                                                                                                                         (1, 'ED-101-2022', 'Used', 'Warehouse', 15000.00, '2022-06-10', 'Under Refurbishment', 'Minor scratches on surface'),
                                                                                                                                         (4, 'DT-101-2022', 'Used', 'Warehouse', 12000.00, '2022-07-15', 'Under Refurbishment', 'Needs polishing'),
                                                                                                                                         (6, 'SF-101-2022', 'Used', 'Warehouse', 20000.00, '2022-08-20', 'Under Refurbishment', 'Upholstery needs replacement');

-- Insert refurbishment records
INSERT INTO refurbishment_records (inventory_item_id, start_date, description, cost, assigned_staff_id, status, notes) VALUES
                                                                                                                           (31, NOW() - INTERVAL '5 day', 'Repair scratches and polish surface', 2000.00, 5, 'In Progress', 'Expected to complete in 3 days'),
                                                                                                                           (32, NOW() - INTERVAL '3 day', 'Polish and treat wood', 1500.00, 5, 'In Progress', 'Expected to complete in 2 days'),
                                                                                                                           (33, NOW() - INTERVAL '7 day', 'Replace upholstery fabric', 5000.00, 6, 'In Progress', 'Waiting for fabric delivery');

-- Insert shifts
INSERT INTO shifts (name, start_time, end_time, description, recurring, recurrence_pattern, active) VALUES
                                                                                                        ('Morning Shift', NOW()::date + '09:00:00'::time, NOW()::date + '17:00:00'::time, 'Regular day shift', true, 'Weekdays', true),
                                                                                                        ('Evening Shift', NOW()::date + '13:00:00'::time, NOW()::date + '21:00:00'::time, 'Evening coverage', true, 'Weekdays', true),
                                                                                                        ('Weekend Shift', NOW()::date + '10:00:00'::time, NOW()::date + '18:00:00'::time, 'Weekend coverage', true, 'Weekends', true);

-- Insert assignments
INSERT INTO assignments (staff_id, shift_id, assigned_date, status, notes) VALUES
                                                                               (1, 1, NOW(), 'Assigned', NULL),
                                                                               (2, 1, NOW(), 'Assigned', NULL),
                                                                               (3, 2, NOW(), 'Assigned', NULL),
                                                                               (4, 2, NOW(), 'Assigned', NULL),
                                                                               (5, 3, NOW() + INTERVAL '2 day', 'Assigned', NULL);

-- Insert sample sales orders
INSERT INTO sales_orders (order_number, customer_id, order_date, status, total_amount, tax_amount, final_amount, created_by) VALUES
                                                                                                                                 ('ORD-20230001', 1, NOW() - INTERVAL '30 day', 'Delivered', 25000.00, 4250.00, 29250.00, 3),
                                                                                                                                 ('ORD-20230002', 2, NOW() - INTERVAL '15 day', 'Delivered', 38500.00, 6545.00, 45045.00, 4),
                                                                                                                                 ('ORD-20230003', 3, NOW() - INTERVAL '7 day', 'Confirmed', 40000.00, 6800.00, 46800.00, 3),
                                                                                                                                 ('ORD-20230004', 4, NOW() - INTERVAL '3 day', 'Pending', 9000.00, 1530.00, 10530.00, 4);

-- Insert order line items (simplified - not linking to actual inventory items)
INSERT INTO order_line_items (sales_order_id, inventory_item_id, quantity, unit_price, line_total) VALUES
                                                                                                       (1, 1, 1, 25000.00, 25000.00),
                                                                                                       (2, 4, 1, 8000.00, 8000.00),
                                                                                                       (2, 13, 1, 18000.00, 18000.00),
                                                                                                       (2, 14, 5, 2500.00, 12500.00),
                                                                                                       (3, 25, 1, 40000.00, 40000.00),
                                                                                                       (4, 22, 1, 9000.00, 9000.00);

-- Insert payments
INSERT INTO payments (sales_order_id, payment_date, amount, payment_method, status, received_by) VALUES
                                                                                                     (1, NOW() - INTERVAL '30 day', 29250.00, 'Cash', 'Completed', 3),
                                                                                                     (2, NOW() - INTERVAL '15 day', 45045.00, 'Credit Card', 'Completed', 4),
                                                                                                     (3, NOW() - INTERVAL '7 day', 20000.00, 'Bank Transfer', 'Completed', 3);

-- Insert purchase orders
INSERT INTO purchase_orders (order_number, supplier_id, order_date, expected_delivery_date, status, total_amount, created_by) VALUES
                                                                                                                                  ('PO-20230001', 1, NOW() - INTERVAL '45 day', NOW() - INTERVAL '30 day', 'Fully Received', 100000.00, 2),
                                                                                                                                  ('PO-20230002', 2, NOW() - INTERVAL '20 day', NOW() - INTERVAL '5 day', 'Fully Received', 75000.00, 2),
                                                                                                                                  ('PO-20230003', 3, NOW() - INTERVAL '10 day', NOW() + INTERVAL '5 day', 'Sent', 50000.00, 2);

-- Insert purchase order items
INSERT INTO purchase_order_items (purchase_order_id, furniture_type_id, quantity, received_quantity, unit_price, line_total) VALUES
                                                                                                                                 (1, 1, 3, 3, 20000.00, 60000.00),
                                                                                                                                 (1, 2, 5, 5, 8000.00, 40000.00),
                                                                                                                                 (2, 3, 3, 3, 10000.00, 30000.00),
                                                                                                                                 (2, 4, 2, 2, 15000.00, 30000.00),
                                                                                                                                 (2, 5, 5, 5, 3000.00, 15000.00),
                                                                                                                                 (3, 6, 1, 0, 30000.00, 30000.00),
                                                                                                                                 (3, 7, 2, 0, 10000.00, 20000.00);

-- Insert audit logs
INSERT INTO audit_logs (action, entity_type, entity_id, details, timestamp, user_id, ip_address) VALUES
                                                                                                     ('CREATE', 'SalesOrder', 1, 'Created new sales order ORD-20230001', NOW() - INTERVAL '30 day', 3, '192.168.1.100'),
                                                                                                     ('UPDATE', 'SalesOrder', 1, 'Updated status to Delivered', NOW() - INTERVAL '29 day', 2, '192.168.1.101'),
                                                                                                     ('CREATE', 'Payment', 1, 'Received payment for order ORD-20230001', NOW() - INTERVAL '30 day', 3, '192.168.1.100'),
                                                                                                     ('CREATE', 'SalesOrder', 2, 'Created new sales order ORD-20230002', NOW() - INTERVAL '15 day', 4, '192.168.1.102'),
                                                                                                     ('UPDATE', 'SalesOrder', 2, 'Updated status to Delivered', NOW() - INTERVAL '14 day', 2, '192.168.1.101'),
                                                                                                     ('CREATE', 'Payment', 2, 'Received payment for order ORD-20230002', NOW() - INTERVAL '15 day', 4, '192.168.1.102'),
                                                                                                     ('CREATE', 'SalesOrder', 3, 'Created new sales order ORD-20230003', NOW() - INTERVAL '7 day', 3, '192.168.1.100'),
                                                                                                     ('CREATE', 'Payment', 3, 'Received partial payment for order ORD-20230003', NOW() - INTERVAL '7 day', 3, '192.168.1.100'),
                                                                                                     ('CREATE', 'SalesOrder', 4, 'Created new sales order ORD-20230004', NOW() - INTERVAL '3 day', 4, '192.168.1.102'),
                                                                                                     ('CREATE', 'PurchaseOrder', 1, 'Created new purchase order PO-20230001', NOW() - INTERVAL '45 day', 2, '192.168.1.101'),
                                                                                                     ('UPDATE', 'PurchaseOrder', 1, 'Updated status to Fully Received', NOW() - INTERVAL '30 day', 2, '192.168.1.101'),
                                                                                                     ('CREATE', 'PurchaseOrder', 2, 'Created new purchase order PO-20230002', NOW() - INTERVAL '20 day', 2, '192.168.1.101'),
                                                                                                     ('UPDATE', 'PurchaseOrder', 2, 'Updated status to Fully Received', NOW() - INTERVAL '5 day', 2, '192.168.1.101'),
                                                                                                     ('CREATE', 'PurchaseOrder', 3, 'Created new purchase order PO-20230003', NOW() - INTERVAL '10 day', 2, '192.168.1.101'),
                                                                                                     ('CREATE', 'RefurbishmentRecord', 1, 'Created refurbishment record for item ED-101-2022', NOW() - INTERVAL '5 day', 5, '192.168.1.103'),
                                                                                                     ('CREATE', 'RefurbishmentRecord', 2, 'Created refurbishment record for item DT-101-2022', NOW() - INTERVAL '3 day', 5, '192.168.1.103'),
                                                                                                     ('CREATE', 'RefurbishmentRecord', 3, 'Created refurbishment record for item SF-101-2022', NOW() - INTERVAL '7 day', 6, '192.168.1.104');
