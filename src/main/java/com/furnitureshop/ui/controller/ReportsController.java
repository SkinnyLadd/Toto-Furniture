package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.service.*;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class ReportsController implements Initializable {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button generateReportButton;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalInventoryLabel;

    @FXML
    private Label lowStockItemsLabel;

    @FXML
    private PieChart salesByStatusChart;

    @FXML
    private BarChart<String, Number> monthlySalesChart;

    @FXML
    private TableView<TopSellingItem> topSellingItemsTable;

    @FXML
    private TableView<LowStockItem> lowStockReportTable;

    @FXML
    private Button exportSalesReportButton;

    @FXML
    private Button exportInventoryReportButton;

    @FXML
    private Button printReportButton;

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
    private final InventoryService inventoryService;
    private final FurnitureTypeService furnitureTypeService;
    private final NumberFormat currencyFormat;

    @Autowired
    public ReportsController(StageManager stageManager,
                             SalesOrderService salesOrderService,
                             InventoryService inventoryService,
                             FurnitureTypeService furnitureTypeService) {
        this.stageManager = stageManager;
        this.salesOrderService = salesOrderService;
        this.inventoryService = inventoryService;
        this.furnitureTypeService = furnitureTypeService;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDatePickers();
        setupEventHandlers();
        setupTables();
        loadReportData();
        setupCharts();
    }

    private void setupDatePickers() {
        startDatePicker.setValue(java.time.LocalDate.now().minusMonths(1));
        endDatePicker.setValue(java.time.LocalDate.now());
    }

    private void setupEventHandlers() {
        dashboardButton.setOnAction(this::handleDashboardButtonAction);
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        procurementButton.setOnAction(this::handleProcurementButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);
        reportsButton.setOnAction(this::handleReportsButtonAction);

        generateReportButton.setOnAction(this::handleGenerateReportButtonAction);
        exportSalesReportButton.setOnAction(this::handleExportSalesReportButtonAction);
        exportInventoryReportButton.setOnAction(this::handleExportInventoryReportButtonAction);
        printReportButton.setOnAction(this::handlePrintReportButtonAction);
    }

    private void setupTables() {
        // Setup top selling items table
        TableColumn<TopSellingItem, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemNameCol.setPrefWidth(200);

        TableColumn<TopSellingItem, String> quantitySoldCol = new TableColumn<>("Quantity Sold");
        quantitySoldCol.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        quantitySoldCol.setPrefWidth(100);

        TableColumn<TopSellingItem, String> revenueCol = new TableColumn<>("Revenue (PKR)");
        revenueCol.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        revenueCol.setPrefWidth(100);

        topSellingItemsTable.getColumns().clear();
        topSellingItemsTable.getColumns().addAll(itemNameCol, quantitySoldCol, revenueCol);

        // Setup low stock report table
        TableColumn<LowStockItem, String> lowStockItemNameCol = new TableColumn<>("Item Name");
        lowStockItemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        lowStockItemNameCol.setPrefWidth(200);

        TableColumn<LowStockItem, String> currentStockCol = new TableColumn<>("Current Stock");
        currentStockCol.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
        currentStockCol.setPrefWidth(100);

        TableColumn<LowStockItem, String> minLevelCol = new TableColumn<>("Min Level");
        minLevelCol.setCellValueFactory(new PropertyValueFactory<>("minLevel"));
        minLevelCol.setPrefWidth(100);

        lowStockReportTable.getColumns().clear();
        lowStockReportTable.getColumns().addAll(lowStockItemNameCol, currentStockCol, minLevelCol);
    }

    private void loadReportData() {
        try {
            LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
            LocalDateTime endDate = endDatePicker.getValue().atTime(23, 59, 59);

            // Load sales data
            Double totalSales = salesOrderService.calculateTotalSalesForPeriod(startDate, endDate);
            totalSalesLabel.setText(currencyFormat.format(totalSales != null ? totalSales : 0.0));

            // Load order count
            int totalOrders = salesOrderService.findSalesOrdersByDateRange(startDate, endDate).size();
            totalOrdersLabel.setText(String.valueOf(totalOrders));

            // Load inventory data
            int totalInventory = inventoryService.findAllInventoryItems().size();
            totalInventoryLabel.setText(String.valueOf(totalInventory));

            // Load low stock items
            List<FurnitureType> lowStockItems = furnitureTypeService.findLowStockItems();
            lowStockItemsLabel.setText(String.valueOf(lowStockItems.size()));

            // Load table data
            loadTopSellingItems();
            loadLowStockItems();

        } catch (Exception e) {
            System.err.println("Error loading report data: " + e.getMessage());
            e.printStackTrace();

            // Set default values on error
            totalSalesLabel.setText("PKR 0.00");
            totalOrdersLabel.setText("0");
            totalInventoryLabel.setText("0");
            lowStockItemsLabel.setText("0");
        }
    }

    private void loadTopSellingItems() {
        try {
            // Get all sales orders and calculate top selling items
            List<SalesOrder> allOrders = salesOrderService.findAllSalesOrders();
            Map<String, TopSellingItem> itemSalesMap = new HashMap<>();

            for (SalesOrder order : allOrders) {
                if (order.getOrderLineItems() != null) {
                    order.getOrderLineItems().forEach(lineItem -> {
                        String itemName = lineItem.getInventoryItem().getFurnitureType().getName();
                        TopSellingItem item = itemSalesMap.getOrDefault(itemName,
                                new TopSellingItem(itemName, 0, 0.0));

                        item.setQuantitySold(item.getQuantitySold() + lineItem.getQuantity());
                        item.setRevenue(item.getRevenue() + lineItem.getSubtotal().doubleValue());

                        itemSalesMap.put(itemName, item);
                    });
                }
            }

            // Convert to list and sort by quantity sold
            List<TopSellingItem> topItems = itemSalesMap.values().stream()
                    .sorted((a, b) -> Integer.compare(b.getQuantitySold(), a.getQuantitySold()))
                    .limit(10)
                    .collect(Collectors.toList());

            ObservableList<TopSellingItem> topSellingData = FXCollections.observableArrayList(topItems);
            topSellingItemsTable.setItems(topSellingData);

        } catch (Exception e) {
            System.err.println("Error loading top selling items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadLowStockItems() {
        try {
            List<FurnitureType> lowStockTypes = furnitureTypeService.findLowStockItems();

            List<LowStockItem> lowStockItems = lowStockTypes.stream()
                    .map(ft -> new LowStockItem(
                            ft.getName(),
                            ft.getStockLevel(),
                            ft.getMinStockLevel()
                    ))
                    .collect(Collectors.toList());

            ObservableList<LowStockItem> lowStockData = FXCollections.observableArrayList(lowStockItems);
            lowStockReportTable.setItems(lowStockData);

        } catch (Exception e) {
            System.err.println("Error loading low stock items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupCharts() {
        // Setup sales by status pie chart
        salesByStatusChart.getData().clear();
        try {
            int pendingOrders = salesOrderService.findSalesOrdersByStatus("Pending").size();
            int confirmedOrders = salesOrderService.findSalesOrdersByStatus("Confirmed").size();
            int deliveredOrders = salesOrderService.findSalesOrdersByStatus("Delivered").size();
            int cancelledOrders = salesOrderService.findSalesOrdersByStatus("Cancelled").size();

            if (pendingOrders > 0) salesByStatusChart.getData().add(new PieChart.Data("Pending", pendingOrders));
            if (confirmedOrders > 0) salesByStatusChart.getData().add(new PieChart.Data("Confirmed", confirmedOrders));
            if (deliveredOrders > 0) salesByStatusChart.getData().add(new PieChart.Data("Delivered", deliveredOrders));
            if (cancelledOrders > 0) salesByStatusChart.getData().add(new PieChart.Data("Cancelled", cancelledOrders));

        } catch (Exception e) {
            System.err.println("Error setting up sales chart: " + e.getMessage());
            e.printStackTrace();
        }

        // Setup monthly sales bar chart
        monthlySalesChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Sales (PKR)");

        try {
            // Add data for the last 6 months
            LocalDateTime now = LocalDateTime.now();
            for (int i = 5; i >= 0; i--) {
                LocalDateTime monthStart = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime monthEnd = monthStart.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

                Double monthlySales = salesOrderService.calculateTotalSalesForPeriod(monthStart, monthEnd);
                String monthName = monthStart.getMonth().name().substring(0, 3);

                series.getData().add(new XYChart.Data<>(monthName, monthlySales != null ? monthlySales : 0.0));
            }
        } catch (Exception e) {
            System.err.println("Error setting up monthly sales chart: " + e.getMessage());
            e.printStackTrace();
        }

        monthlySalesChart.getData().add(series);
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
        stageManager.switchScene(FXMLView.PROCUREMENT);
    }

    @FXML
    private void handleStaffButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.STAFF_SCHEDULE);
    }

    @FXML
    private void handleReportsButtonAction(ActionEvent event) {
        // Already on reports, no need to navigate
    }

    @FXML
    private void handleGenerateReportButtonAction(ActionEvent event) {
        loadReportData();
        setupCharts();
        showAlert("Report Generated", "Report has been generated successfully with current data.");
    }

    @FXML
    private void handleExportSalesReportButtonAction(ActionEvent event) {
        try {
            String fileName = "sales_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            FileWriter writer = new FileWriter(fileName);

            // Write header
            writer.append("Order ID,Customer,Order Date,Status,Total Amount\n");

            // Write data
            List<SalesOrder> orders = salesOrderService.findAllSalesOrders();
            for (SalesOrder order : orders) {
                writer.append(String.valueOf(order.getId())).append(",");
                writer.append(order.getCustomer().getFullName()).append(",");
                writer.append(order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(",");
                writer.append(order.getStatus()).append(",");
                writer.append(String.valueOf(order.getTotalAmount())).append("\n");
            }

            writer.flush();
            writer.close();

            showAlert("Export Successful", "Sales report exported to: " + fileName);

        } catch (IOException e) {
            showAlert("Export Failed", "Failed to export sales report: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportInventoryReportButtonAction(ActionEvent event) {
        try {
            String fileName = "inventory_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            FileWriter writer = new FileWriter(fileName);

            // Write header
            writer.append("Item Name,Category,Current Stock,Min Stock Level,Price\n");

            // Write data
            List<FurnitureType> furnitureTypes = furnitureTypeService.findAllFurnitureTypes();
            for (FurnitureType ft : furnitureTypes) {
                writer.append(ft.getName()).append(",");
                writer.append(ft.getCategory()).append(",");
                writer.append(String.valueOf(ft.getStockLevel())).append(",");
                writer.append(String.valueOf(ft.getMinStockLevel())).append(",");
                writer.append(String.valueOf(ft.getPrice())).append("\n");
            }

            writer.flush();
            writer.close();

            showAlert("Export Successful", "Inventory report exported to: " + fileName);

        } catch (IOException e) {
            showAlert("Export Failed", "Failed to export inventory report: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrintReportButtonAction(ActionEvent event) {
        showAlert("Print Report", "Print functionality will open the system print dialog.\nThis feature requires additional printer configuration.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper classes for table data
    public static class TopSellingItem {
        private String itemName;
        private int quantitySold;
        private double revenue;

        public TopSellingItem(String itemName, int quantitySold, double revenue) {
            this.itemName = itemName;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
        }

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }

        public int getQuantitySold() { return quantitySold; }
        public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }

        public double getRevenue() { return revenue; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
    }

    public static class LowStockItem {
        private String itemName;
        private int currentStock;
        private int minLevel;

        public LowStockItem(String itemName, int currentStock, int minLevel) {
            this.itemName = itemName;
            this.currentStock = currentStock;
            this.minLevel = minLevel;
        }

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }

        public int getCurrentStock() { return currentStock; }
        public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

        public int getMinLevel() { return minLevel; }
        public void setMinLevel(int minLevel) { this.minLevel = minLevel; }
    }
}
