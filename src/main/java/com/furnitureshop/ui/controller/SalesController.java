package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.service.SalesOrderService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SalesController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetButton;

    @FXML
    private TableView<SalesOrder> salesOrderTable;

    @FXML
    private Button newOrderButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button salesButton;

    @FXML
    private Button staffButton;

    private final StageManager stageManager;
    private final SalesOrderService salesOrderService;

    @Autowired
    public SalesController(StageManager stageManager, SalesOrderService salesOrderService) {
        this.stageManager = stageManager;
        this.salesOrderService = salesOrderService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSalesData();
        setupEventHandlers();
        setupFilters();
    }

    private void loadSalesData() {
        try {
            List<SalesOrder> orders = salesOrderService.findAllSalesOrders();
            // TODO: Set up table data binding
        } catch (Exception e) {
            System.err.println("Error loading sales data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        dashboardButton.setOnAction(this::handleDashboardButtonAction);
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);

        searchButton.setOnAction(this::handleSearchButtonAction);
        resetButton.setOnAction(this::handleResetButtonAction);
        newOrderButton.setOnAction(this::handleNewOrderButtonAction);
        viewDetailsButton.setOnAction(this::handleViewDetailsButtonAction);
        exportButton.setOnAction(this::handleExportButtonAction);
    }

    private void setupFilters() {
        // Set up status filter
        statusFilter.getItems().addAll(
                "All Statuses",
                "Pending",
                "Confirmed",
                "Delivered",
                "Cancelled"
        );
        statusFilter.setValue("All Statuses");

        // Set up date pickers
        startDatePicker.setValue(java.time.LocalDate.now().minusMonths(1));
        endDatePicker.setValue(java.time.LocalDate.now());
    }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.DASHBOARD);
    }

    @FXML
    private void handleInventoryButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.INVENTORY);
    }

    @FXML
    private void handleSalesButtonAction(ActionEvent event) {
        // Already on sales, no need to navigate
    }

    @FXML
    private void handleStaffButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.STAFF_SCHEDULE);
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        // Clear security context
        SecurityContextHolder.clearContext();
        // Return to login screen
        stageManager.switchScene(FXMLView.LOGIN);
    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        // TODO: Implement search functionality
        System.out.println("Search button clicked");

        String searchTerm = searchField.getText();
        String status = statusFilter.getValue();
        LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
        LocalDateTime endDate = endDatePicker.getValue().atTime(23, 59, 59);

        System.out.println("Search term: " + searchTerm);
        System.out.println("Status: " + status);
        System.out.println("Date range: " + startDate + " to " + endDate);
    }

    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        searchField.clear();
        statusFilter.setValue("All Statuses");
        startDatePicker.setValue(java.time.LocalDate.now().minusMonths(1));
        endDatePicker.setValue(java.time.LocalDate.now());
        loadSalesData();
    }

    @FXML
    private void handleNewOrderButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.ORDER_ENTRY);
    }

    @FXML
    private void handleViewDetailsButtonAction(ActionEvent event) {
        // TODO: Implement view details functionality
        System.out.println("View details button clicked");
    }

    @FXML
    private void handleExportButtonAction(ActionEvent event) {
        // TODO: Implement export functionality
        System.out.println("Export button clicked");
    }
}
