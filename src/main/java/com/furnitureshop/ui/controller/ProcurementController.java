package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.PurchaseOrder;
import com.furnitureshop.model.entity.Supplier;
import com.furnitureshop.service.PurchaseOrderService;
import com.furnitureshop.service.SupplierService;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class ProcurementController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private ComboBox<Supplier> supplierFilter;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetButton;

    @FXML
    private TableView<PurchaseOrder> purchaseOrderTable;

    @FXML
    private TableColumn<PurchaseOrder, String> orderNumberColumn;

    @FXML
    private TableColumn<PurchaseOrder, String> supplierColumn;

    @FXML
    private TableColumn<PurchaseOrder, String> orderDateColumn;

    @FXML
    private TableColumn<PurchaseOrder, String> statusColumn;

    @FXML
    private TableColumn<PurchaseOrder, String> totalAmountColumn;

    @FXML
    private Button newPurchaseOrderButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button receiveItemsButton;

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
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    @Autowired
    public ProcurementController(StageManager stageManager,
                                 PurchaseOrderService purchaseOrderService,
                                 SupplierService supplierService) {
        this.stageManager = stageManager;
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupFilters();
        setupEventHandlers();
        loadPurchaseOrderData();
    }

    private void setupTableColumns() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

        supplierColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSupplier().getName()));

        orderDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrderDate().format(dateFormatter)));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        totalAmountColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(currencyFormat.format(cellData.getValue().getTotalAmount())));

        // Make columns fill the table width
        orderNumberColumn.prefWidthProperty().bind(purchaseOrderTable.widthProperty().multiply(0.15));
        supplierColumn.prefWidthProperty().bind(purchaseOrderTable.widthProperty().multiply(0.25));
        orderDateColumn.prefWidthProperty().bind(purchaseOrderTable.widthProperty().multiply(0.15));
        statusColumn.prefWidthProperty().bind(purchaseOrderTable.widthProperty().multiply(0.15));
        totalAmountColumn.prefWidthProperty().bind(purchaseOrderTable.widthProperty().multiply(0.30));
    }

    private void setupFilters() {
        // Set up status filter
        statusFilter.getItems().addAll(
                "All Statuses",
                "Draft",
                "Sent",
                "Partially Received",
                "Fully Received",
                "Cancelled"
        );
        statusFilter.setValue("All Statuses");

        // Set up supplier filter
        try {
            List<Supplier> suppliers = supplierService.findActiveSuppliers();
            supplierFilter.getItems().add(null); // Add "All Suppliers" option
            supplierFilter.getItems().addAll(suppliers);

            // Custom cell factory for supplier display
            supplierFilter.setCellFactory(listView -> new ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    if (empty || supplier == null) {
                        setText("All Suppliers");
                    } else {
                        setText(supplier.getName() + " - " + supplier.getCity());
                    }
                }
            });

            supplierFilter.setButtonCell(new ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    if (empty || supplier == null) {
                        setText("All Suppliers");
                    } else {
                        setText(supplier.getName() + " - " + supplier.getCity());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading suppliers: " + e.getMessage());
            e.printStackTrace();
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
        newPurchaseOrderButton.setOnAction(this::handleNewPurchaseOrderButtonAction);
        viewDetailsButton.setOnAction(this::handleViewDetailsButtonAction);
        receiveItemsButton.setOnAction(this::handleReceiveItemsButtonAction);
        exportButton.setOnAction(this::handleExportButtonAction);
    }

    private void loadPurchaseOrderData() {
        try {
            List<PurchaseOrder> orders = purchaseOrderService.findAllPurchaseOrders();
            ObservableList<PurchaseOrder> orderList = FXCollections.observableArrayList(orders);
            purchaseOrderTable.setItems(orderList);
        } catch (Exception e) {
            System.err.println("Error loading purchase order data: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load purchase order data: " + e.getMessage());
        }
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
        stageManager.switchScene(FXMLView.SALES);
    }

    @FXML
    private void handleProcurementButtonAction(ActionEvent event) {
        // Already on procurement, no need to navigate
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
        Supplier selectedSupplier = supplierFilter.getValue();

        try {
            List<PurchaseOrder> allOrders = purchaseOrderService.findAllPurchaseOrders();

            List<PurchaseOrder> filteredOrders = allOrders.stream()
                    .filter(order -> {
                        boolean matchesSearch = searchTerm.isEmpty() ||
                                order.getOrderNumber().toLowerCase().contains(searchTerm) ||
                                order.getSupplier().getName().toLowerCase().contains(searchTerm);

                        boolean matchesStatus = "All Statuses".equals(selectedStatus) ||
                                order.getStatus().equals(selectedStatus);

                        boolean matchesSupplier = selectedSupplier == null ||
                                order.getSupplier().getId().equals(selectedSupplier.getId());

                        return matchesSearch && matchesStatus && matchesSupplier;
                    })
                    .toList();

            ObservableList<PurchaseOrder> orderList = FXCollections.observableArrayList(filteredOrders);
            purchaseOrderTable.setItems(orderList);
        } catch (Exception e) {
            System.err.println("Error searching purchase orders: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        searchField.clear();
        statusFilter.setValue("All Statuses");
        supplierFilter.setValue(null);
        loadPurchaseOrderData();
    }

    @FXML
    private void handleNewPurchaseOrderButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.NEW_PURCHASE_ORDER);
    }

    @FXML
    private void handleViewDetailsButtonAction(ActionEvent event) {
        PurchaseOrder selectedOrder = purchaseOrderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            showAlert("Purchase Order Details",
                    "Order Number: " + selectedOrder.getOrderNumber() + "\n" +
                            "Supplier: " + selectedOrder.getSupplier().getName() + "\n" +
                            "Status: " + selectedOrder.getStatus() + "\n" +
                            "Total Amount: " + currencyFormat.format(selectedOrder.getTotalAmount()));
        } else {
            showAlert("No Selection", "Please select a purchase order to view details.");
        }
    }

    @FXML
    private void handleReceiveItemsButtonAction(ActionEvent event) {
        PurchaseOrder selectedOrder = purchaseOrderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            if ("Sent".equals(selectedOrder.getStatus()) || "Partially Received".equals(selectedOrder.getStatus())) {
                try {
                    // Update order status to received
                    selectedOrder.setStatus("Fully Received");
                    purchaseOrderService.updatePurchaseOrder(selectedOrder);

                    showAlert("Success", "Items received successfully! Order status updated to 'Fully Received'.");
                    loadPurchaseOrderData(); // Refresh the table
                } catch (Exception e) {
                    showAlert("Error", "Failed to receive items: " + e.getMessage());
                }
            } else {
                showAlert("Invalid Status", "Can only receive items for orders with status 'Sent' or 'Partially Received'.");
            }
        } else {
            showAlert("No Selection", "Please select a purchase order to receive items.");
        }
    }

    @FXML
    private void handleExportButtonAction(ActionEvent event) {
        showAlert("Feature Coming Soon", "Export functionality will be implemented soon.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
