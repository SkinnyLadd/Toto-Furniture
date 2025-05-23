package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.service.InventoryService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class InventoryController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetButton;

    @FXML
    private TableView<InventoryItem> inventoryTable;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Button addItemButton;

    @FXML
    private Button refurbishButton;

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
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(StageManager stageManager, InventoryService inventoryService) {
        this.stageManager = stageManager;
        this.inventoryService = inventoryService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadInventoryData();
        setupEventHandlers();
        setupFilters();
    }

    private void loadInventoryData() {
        try {
            List<InventoryItem> items = inventoryService.findAllInventoryItems();
            // TODO: Set up table data binding
            totalItemsLabel.setText(String.valueOf(items.size()));
        } catch (Exception e) {
            System.err.println("Error loading inventory data: " + e.getMessage());
            e.printStackTrace();
            totalItemsLabel.setText("0");
        }
    }

    private void setupEventHandlers() {
        dashboardButton.setOnAction(this::handleDashboardButtonAction);
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);

        searchButton.setOnAction(this::handleSearchButtonAction);
        resetButton.setOnAction(this::handleResetButtonAction);
        addItemButton.setOnAction(this::handleAddItemButtonAction);
        refurbishButton.setOnAction(this::handleRefurbishButtonAction);
        exportButton.setOnAction(this::handleExportButtonAction);
    }

    private void setupFilters() {
        // Set up category filter
        categoryFilter.getItems().addAll(
                "All Categories",
                "Office",
                "Living Room",
                "Dining",
                "Bedroom",
                "Storage"
        );
        categoryFilter.setValue("All Categories");

        // Set up status filter
        statusFilter.getItems().addAll(
                "All Statuses",
                "Available",
                "Sold",
                "Reserved",
                "Under Refurbishment"
        );
        statusFilter.setValue("All Statuses");
    }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.DASHBOARD);
    }

    @FXML
    private void handleInventoryButtonAction(ActionEvent event) {
        // Already on inventory, no need to navigate
    }

    @FXML
    private void handleSalesButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.SALES);
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
    }

    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        searchField.clear();
        categoryFilter.setValue("All Categories");
        statusFilter.setValue("All Statuses");
        loadInventoryData();
    }

    @FXML
    private void handleAddItemButtonAction(ActionEvent event) {
        // TODO: Implement add item functionality
        System.out.println("Add item button clicked");
        stageManager.switchScene(FXMLView.INVENTORY_DETAIL);
    }

    @FXML
    private void handleRefurbishButtonAction(ActionEvent event) {
        // TODO: Implement refurbish functionality
        System.out.println("Refurbish button clicked");
    }

    @FXML
    private void handleExportButtonAction(ActionEvent event) {
        // TODO: Implement export functionality
        System.out.println("Export button clicked");
    }
}
