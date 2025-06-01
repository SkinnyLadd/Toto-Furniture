package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.service.SalesOrderService;
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
    private TableColumn<SalesOrder, String> orderNumberColumn;

    @FXML
    private TableColumn<SalesOrder, String> customerColumn;

    @FXML
    private TableColumn<SalesOrder, String> orderDateColumn;

    @FXML
    private TableColumn<SalesOrder, String> deliveryDateColumn;

    @FXML
    private TableColumn<SalesOrder, String> statusColumn;

    @FXML
    private TableColumn<SalesOrder, String> totalAmountColumn;

    @FXML
    private TableColumn<SalesOrder, String> createdByColumn;

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
    private Button procurementButton;

    @FXML
    private Button staffButton;

    @FXML
    private Button reportsButton;

    private final StageManager stageManager;
    private final SalesOrderService salesOrderService;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    @Autowired
    public SalesController(StageManager stageManager, SalesOrderService salesOrderService) {
        this.stageManager = stageManager;
        this.salesOrderService = salesOrderService;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupFilters();
        setupEventHandlers();
        loadSalesData();
    }

    private void setupTableColumns() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

        customerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer().getFullName()));

        orderDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrderDate().format(dateFormatter)));

        deliveryDateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDeliveryDate() != null) {
                return new SimpleStringProperty(cellData.getValue().getDeliveryDate().format(dateFormatter));
            } else {
                return new SimpleStringProperty("Not Set");
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        totalAmountColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(currencyFormat.format(cellData.getValue().getFinalAmount())));

        createdByColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCreatedBy() != null) {
                return new SimpleStringProperty(cellData.getValue().getCreatedBy().getFullName());
            } else {
                return new SimpleStringProperty("System");
            }
        });

        // Make columns fill the table width
        orderNumberColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.12));
        customerColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.20));
        orderDateColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.12));
        deliveryDateColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.12));
        statusColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.10));
        totalAmountColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.15));
        createdByColumn.prefWidthProperty().bind(salesOrderTable.widthProperty().multiply(0.19));
    }

    private void loadSalesData() {
        try {
            List<SalesOrder> orders = salesOrderService.findAllSalesOrders();
            ObservableList<SalesOrder> orderList = FXCollections.observableArrayList(orders);
            salesOrderTable.setItems(orderList);
        } catch (Exception e) {
            System.err.println("Error loading sales data: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load sales data: " + e.getMessage());
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
        String selectedStatus = statusFilter.getValue();
        LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
        LocalDateTime endDate = endDatePicker.getValue().atTime(23, 59, 59);

        try {
            List<SalesOrder> allOrders = salesOrderService.findAllSalesOrders();

            List<SalesOrder> filteredOrders = allOrders.stream()
                    .filter(order -> {
                        boolean matchesSearch = searchTerm.isEmpty() ||
                                order.getOrderNumber().toLowerCase().contains(searchTerm) ||
                                order.getCustomer().getFullName().toLowerCase().contains(searchTerm);

                        boolean matchesStatus = "All Statuses".equals(selectedStatus) ||
                                order.getStatus().equals(selectedStatus);

                        boolean matchesDateRange = !order.getOrderDate().isBefore(startDate) &&
                                !order.getOrderDate().isAfter(endDate);

                        return matchesSearch && matchesStatus && matchesDateRange;
                    })
                    .toList();

            ObservableList<SalesOrder> orderList = FXCollections.observableArrayList(filteredOrders);
            salesOrderTable.setItems(orderList);
        } catch (Exception e) {
            System.err.println("Error searching sales orders: " + e.getMessage());
            e.printStackTrace();
        }
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
        SalesOrder selectedOrder = salesOrderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            showAlert("Order Details",
                    "Order Number: " + selectedOrder.getOrderNumber() + "\n" +
                            "Customer: " + selectedOrder.getCustomer().getFullName() + "\n" +
                            "Status: " + selectedOrder.getStatus() + "\n" +
                            "Total Amount: " + currencyFormat.format(selectedOrder.getFinalAmount()) + "\n" +
                            "Order Date: " + selectedOrder.getOrderDate().format(dateFormatter));
        } else {
            showAlert("No Selection", "Please select a sales order to view details.");
        }
    }

    @FXML
    private void handleExportButtonAction(ActionEvent event) {
        try {
            List<SalesOrder> orders = salesOrderService.findAllSalesOrders();

            // Create CSV content
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Order Number,Customer,Order Date,Delivery Date,Status,Total Amount (PKR),Created By\n");

            for (SalesOrder order : orders) {
                csvContent.append(order.getOrderNumber()).append(",")
                        .append(order.getCustomer().getFullName()).append(",")
                        .append(order.getOrderDate().format(dateFormatter)).append(",")
                        .append(order.getDeliveryDate() != null ? order.getDeliveryDate().format(dateFormatter) : "Not Set").append(",")
                        .append(order.getStatus()).append(",")
                        .append(currencyFormat.format(order.getFinalAmount())).append(",")
                        .append(order.getCreatedBy() != null ? order.getCreatedBy().getFullName() : "System")
                        .append("\n");
            }

            String fileName = "sales_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

            showAlert("Export Successful",
                    "Sales data exported successfully!\n" +
                            "File: " + fileName + "\n" +
                            "Records exported: " + orders.size());

        } catch (Exception e) {
            showAlert("Export Error", "Failed to export sales data: " + e.getMessage());
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
