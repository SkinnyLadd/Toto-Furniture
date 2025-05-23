package com.furnitureshop.ui.view;

import java.util.ResourceBundle;

public enum FXMLView {
    LOGIN {
        @Override
        public String getTitle() {
            return "Login";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/login.fxml";
        }
    },
    DASHBOARD {
        @Override
        public String getTitle() {
            return "Dashboard";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/dashboard.fxml";
        }
    },
    INVENTORY {
        @Override
        public String getTitle() {
            return "Inventory Management";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/inventory.fxml";
        }
    },
    INVENTORY_DETAIL {
        @Override
        public String getTitle() {
            return "Inventory Item Details";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/inventory-detail.fxml";
        }
    },
    SALES {
        @Override
        public String getTitle() {
            return "Sales Management";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/sales.fxml";
        }
    },
    ORDER_ENTRY {
        @Override
        public String getTitle() {
            return "New Order";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/order-entry.fxml";
        }
    },
    STAFF_SCHEDULE {
        @Override
        public String getTitle() {
            return "Staff Schedule";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/staff-schedule.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();
    
    public ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("bundle");
    }
}
