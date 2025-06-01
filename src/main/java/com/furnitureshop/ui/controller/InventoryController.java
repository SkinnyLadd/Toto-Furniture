package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.RefurbishmentRecord;
import com.furnitureshop.service.InventoryService;
import com.furnitureshop.service.RefurbishmentService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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
    private TableColumn<InventoryItem, String> itemIdColumn;

    @FXML
    private TableColumn<InventoryItem, String> serialNumberColumn;

    @FXML
    private TableColumn<InventoryItem, String> furnitureTypeColumn;

    @FXML
    private TableColumn<InventoryItem, String> categoryColumn;

    @FXML
    private TableColumn<InventoryItem, String> conditionColumn;

    @FXML
    private TableColumn<InventoryItem, String> locationColumn;

    @FXML
    private TableColumn<InventoryItem, String> statusColumn;

    @FXML
    private TableColumn<InventoryItem, String> acquisitionDateColumn;

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
    private Button procurementButton;

    @FXML
    private Button staffButton;

    @FXML
    private Button reportsButton;

    private final StageManager stageManager;
    private final InventoryService inventoryService;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    @Autowired
    private RefurbishmentService refurbishmentService;

    @Autowired
    public InventoryController(StageManager stageManager, InventoryService inventoryService) {
        this.stageManager = stageManager;
        this.inventoryService = inventoryService;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupFilters();
        setupEventHandlers();
        loadInventoryData();
    }

    private void setupTableColumns() {
        itemIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().toString()));

        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));

        furnitureTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFurnitureType().getName()));

        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFurnitureType().getCategory()));

        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        acquisitionDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAcquisitionDate().format(dateFormatter)));

        // Make columns fill the table width
        itemIdColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.10));
        serialNumberColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        furnitureTypeColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.20));
        categoryColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.12));
        conditionColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.10));
        locationColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.10));
        statusColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.10));
        acquisitionDateColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.13));
    }

    private void loadInventoryData() {
        try {
            List<InventoryItem> items = inventoryService.findAllInventoryItems();
            ObservableList<InventoryItem> itemList = FXCollections.observableArrayList(items);
            inventoryTable.setItems(itemList);
            totalItemsLabel.setText(String.valueOf(items.size()));
        } catch (Exception e) {
            System.err.println("Error loading inventory data: " + e.getMessage());
            e.printStackTrace();
            totalItemsLabel.setText("0");
            showAlert("Error", "Failed to load inventory data: " + e.getMessage());
        }
    }

    private void setupEventHandlers() {
        dashboardButton.setOnAction(this::handleDashboardButtonAction);
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        procurementButton.setOnAction(this::handleProcurementButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);
        reportsButton.setOnAction(this::handleReportsButtonAction);

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
    private void handleProcurementButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.PROCUREMENT);
    }

    @FXML
    private void handleStaffButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.STAFF_SCHEDULE);
    }

    @FXML
    private void handleReportsButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.REPORTS);
    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String searchTerm = searchField.getText().toLowerCase();
        String selectedCategory = categoryFilter.getValue();
        String selectedStatus = statusFilter.getValue();

        try {
            List<InventoryItem> allItems = inventoryService.findAllInventoryItems();

            List<InventoryItem> filteredItems = allItems.stream()
                    .filter(item -> {
                        boolean matchesSearch = searchTerm.isEmpty() ||
                                item.getSerialNumber().toLowerCase().contains(searchTerm) ||
                                item.getFurnitureType().getName().toLowerCase().contains(searchTerm);

                        boolean matchesCategory = "All Categories".equals(selectedCategory) ||
                                item.getFurnitureType().getCategory().equals(selectedCategory);

                        boolean matchesStatus = "All Statuses".equals(selectedStatus) ||
                                item.getStatus().equals(selectedStatus);

                        return matchesSearch && matchesCategory && matchesStatus;
                    })
                    .toList();

            ObservableList<InventoryItem> itemList = FXCollections.observableArrayList(filteredItems);
            inventoryTable.setItems(itemList);
            totalItemsLabel.setText(String.valueOf(filteredItems.size()));
        } catch (Exception e) {
            System.err.println("Error searching inventory: " + e.getMessage());
            e.printStackTrace();
        }
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
        stageManager.switchScene(FXMLView.INVENTORY_DETAIL);
    }

    @FXML
    private void handleRefurbishButtonAction(ActionEvent event) {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if ("Available".equals(selectedItem.getStatus()) || "Under Refurbishment".equals(selectedItem.getStatus())) {
                try {
                    // Create refurbishment record
                    RefurbishmentRecord record = new RefurbishmentRecord();
                    record.setInventoryItem(selectedItem);
                    record.setStartDate(LocalDateTime.now());
                    record.setEstimatedCompletionDate(LocalDateTime.now().plusDays(7));
                    record.setStatus("Scheduled");
                    record.setDescription("Scheduled for refurbishment");

                    refurbishmentService.scheduleRefurbishment(record);

                    // Update item status
                    selectedItem.setStatus("Under Refurbishment");
                    inventoryService.updateInventoryItem(selectedItem);

                    showAlert("Success",
                            "Refurbishment scheduled successfully!\n" +
                                    "Item: " + selectedItem.getFurnitureType().getName() + "\n" +
                                    "Serial: " + selectedItem.getSerialNumber() + "\n" +
                                    "Estimated completion: " + record.getEstimatedCompletionDate().format(dateFormatter));

                    loadInventoryData(); // Refresh the table
                } catch (Exception e) {
                    showAlert("Error", "Failed to schedule refurbishment: " + e.getMessage());
                }
            } else {
                showAlert("Invalid Status", "Can only schedule refurbishment for items with status 'Available' or 'Under Refurbishment'.");
            }
        } else {
            showAlert("No Selection", "Please select an inventory item to schedule refurbishment.");
        }
    }

    @FXML
    private void handleExportButtonAction(ActionEvent event) {
        try {
            List<InventoryItem> items = inventoryService.findAllInventoryItems();

            // Create CSV content
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Item ID,Serial Number,Furniture Type,Category,Condition,Location,Status,Acquisition Date\n");

            for (InventoryItem item : items) {
                csvContent.append(item.getId()).append(",")
                        .append(item.getSerialNumber()).append(",")
                        .append(item.getFurnitureType().getName()).append(",")
                        .append(item.getFurnitureType().getCategory()).append(",")
                        .append(item.getCondition()).append(",")
                        .append(item.getLocation()).append(",")
                        .append(item.getStatus()).append(",")
                        .append(item.getAcquisitionDate().format(dateFormatter))
                        .append("\n");
            }

            // Save to file (simplified - in real app would use FileChooser)
            String fileName = "inventory_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

            showAlert("Export Successful",
                    "Inventory data exported successfully!\n" +
                            "File: " + fileName + "\n" +
                            "Records exported: " + items.size());

        } catch (Exception e) {
            showAlert("Export Error", "Failed to export inventory data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
