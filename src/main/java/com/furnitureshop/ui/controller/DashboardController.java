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

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
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
        monthlySalesLabel.setText(String.format("$%.2f", monthlySales != null ? monthlySales : 0.0));

        // Load recent inventory items and orders
        // This would be implemented with TableView data binding

        // Load charts
        // This would be implemented with JavaFX chart data binding
    }

    private void setupEventHandlers() {
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);
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
    private void handleLogoutButtonAction(ActionEvent event) {
        // Clear security context
        SecurityContextHolder.clearContext();
        // Return to login screen
        stageManager.switchScene(FXMLView.LOGIN);
    }
}
