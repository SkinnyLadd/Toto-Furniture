package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.service.FurnitureTypeService;
import com.furnitureshop.service.InventoryService;
import com.furnitureshop.service.SalesOrderService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javafx.scene.chart.XYChart;
import java.util.Map;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class DashboardController implements Initializable {

    @FXML
    private Label totalInventoryLabel;

    @FXML
    private Label lowStockItemsLabel;

    @FXML
    private Label pendingOrdersLabel;

    @FXML
    private Label monthlySalesLabel;

    @FXML
    private TableView<InventoryItem> recentInventoryTable;

    @FXML
    private TableView<SalesOrder> recentOrdersTable;

    @FXML
    private PieChart inventoryStatusChart;

    @FXML
    private BarChart<String, Number> monthlySalesChart;

    @FXML
    private VBox lowStockAlertBox;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button salesButton;

    @FXML
    private Button staffButton;

    @FXML
    private Button procurementButton;

    @FXML
    private Button reportsButton;

    private final StageManager stageManager;
    private final InventoryService inventoryService;
    private final FurnitureTypeService furnitureTypeService;
    private final SalesOrderService salesOrderService;

    @Autowired
    public DashboardController(StageManager stageManager,
                               InventoryService inventoryService,
                               FurnitureTypeService furnitureTypeService,
                               SalesOrderService salesOrderService) {
        this.stageManager = stageManager;
        this.inventoryService = inventoryService;
        this.furnitureTypeService = furnitureTypeService;
        this.salesOrderService = salesOrderService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardData();
        setupEventHandlers();
    }

    private void loadDashboardData() {
        try {
            // Load total inventory count
            List<InventoryItem> allItems = inventoryService.findAllInventoryItems();
            totalInventoryLabel.setText(String.valueOf(allItems.size()));

            // Load low stock items
            List<FurnitureType> lowStockItems = furnitureTypeService.findLowStockItems();
            lowStockItemsLabel.setText(String.valueOf(lowStockItems.size()));

            // Load pending orders
            List<SalesOrder> pendingOrders = salesOrderService.findSalesOrdersByStatus("Pending");
            pendingOrdersLabel.setText(String.valueOf(pendingOrders.size()));

            // Load monthly sales
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            Double monthlySales = salesOrderService.calculateTotalSalesForPeriod(startOfMonth, LocalDateTime.now());
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
            monthlySalesLabel.setText(currencyFormat.format(monthlySales != null ? monthlySales : 0.0));

            // Load Monthly Sales Chart
            loadMonthlySalesChart();

            System.out.println("Dashboard data loaded successfully:");
            System.out.println("Total Inventory: " + allItems.size());
            System.out.println("Low Stock Items: " + lowStockItems.size());
            System.out.println("Pending Orders: " + pendingOrders.size());
            System.out.println("Monthly Sales: " + (monthlySales != null ? monthlySales : 0.0));

        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();

            // Set default values on error
            totalInventoryLabel.setText("0");
            lowStockItemsLabel.setText("0");
            pendingOrdersLabel.setText("0");
            monthlySalesLabel.setText("PKR 0.00");
        }
    }

    private void loadMonthlySalesChart() {
        try {
            // Fetch sales data grouped by month
            Map<String, Double> monthlySalesData = salesOrderService.getMonthlySalesData();

            // Clear existing data
            monthlySalesChart.getData().clear();

            // Create a new series for the chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Monthly Sales");

            // Populate the series with data
            for (Map.Entry<String, Double> entry : monthlySalesData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // Add the series to the chart
            monthlySalesChart.getData().add(series);

            System.out.println("Monthly sales chart loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error loading monthly sales chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);
        procurementButton.setOnAction(this::handleProcurementButtonAction);
        reportsButton.setOnAction(this::handleReportsButtonAction);
    }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        // Already on dashboard, no need to navigate
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
    private void handleStaffButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.STAFF_SCHEDULE);
    }

    @FXML
    private void handleProcurementButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.PROCUREMENT);
    }

    @FXML
    private void handleReportsButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.REPORTS);
    }
}
