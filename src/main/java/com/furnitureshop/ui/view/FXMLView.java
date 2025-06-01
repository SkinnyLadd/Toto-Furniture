package com.furnitureshop.ui.view;

import java.util.ResourceBundle;

public enum FXMLView {
    LOGIN {
        @Override
        public String getTitle() {
            return "Login - Furniture Shop Management";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/login.fxml";
        }
    },
    DASHBOARD {
        @Override
        public String getTitle() {
            return "Dashboard - Furniture Shop Management";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/dashboard.fxml";
        }
    },
    INVENTORY {
        @Override
        public String getTitle() {
            return "Inventory Management - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/inventory.fxml";
        }
    },
    INVENTORY_DETAIL {
        @Override
        public String getTitle() {
            return "Inventory Item Details - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/inventory-detail.fxml";
        }
    },
    SALES {
        @Override
        public String getTitle() {
            return "Sales Management - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/sales.fxml";
        }
    },
    ORDER_ENTRY {
        @Override
        public String getTitle() {
            return "New Sales Order - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/order-entry.fxml";
        }
    },
    PROCUREMENT {
        @Override
        public String getTitle() {
            return "Procurement Management - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/procurement.fxml";
        }
    },
    STAFF_SCHEDULE {
        @Override
        public String getTitle() {
            return "Staff Schedule - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/staff-schedule.fxml";
        }
    },
    REPORTS {
        @Override
        public String getTitle() {
            return "Reports & Analytics - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/reports.fxml";
        }
    },
    NEW_PURCHASE_ORDER {
        @Override
        public String getTitle() {
            return "New Purchase Order - Furniture Shop";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/new-purchase-order.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    public ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("bundle");
    }
}
