package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.Customer;
import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.service.CustomerService;
import com.furnitureshop.service.FurnitureTypeService;
import com.furnitureshop.service.InventoryService;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class OrderEntryController implements Initializable {

    @FXML
    private TextField orderNumberField;

    @FXML
    private ComboBox<Customer> customerCombo;

    @FXML
    private DatePicker orderDatePicker;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private DatePicker deliveryDatePicker;

    @FXML
    private TextField salesPersonField;

    @FXML
    private ComboBox<FurnitureType> itemTypeCombo;

    @FXML
    private ComboBox<InventoryItem> itemCombo;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField priceField;

    @FXML
    private Button addItemButton;

    @FXML
    private TableView<?> orderItemsTable;

    @FXML
    private TextField subtotalField;

    @FXML
    private TextField discountField;

    @FXML
    private TextField taxField;

    @FXML
    private TextField totalField;

    @FXML
    private TextArea notesArea;

    @FXML
    private Button saveAsDraftButton;

    @FXML
    private Button createOrderButton;

    @FXML
    private Button backButton;

    private final StageManager stageManager;
    private final SalesOrderService salesOrderService;
    private final CustomerService customerService;
    private final FurnitureTypeService furnitureTypeService;
    private final InventoryService inventoryService;

    @Autowired
    public OrderEntryController(StageManager stageManager,
                                SalesOrderService salesOrderService,
                                CustomerService customerService,
                                FurnitureTypeService furnitureTypeService,
                                InventoryService inventoryService) {
        this.stageManager = stageManager;
        this.salesOrderService = salesOrderService;
        this.customerService = customerService;
        this.furnitureTypeService = furnitureTypeService;
        this.inventoryService = inventoryService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupEventHandlers();
        setupInitialValues();
    }

    private void setupComboBoxes() {
        // Set up customer combo
        try {
            List<Customer> customers = customerService.findAllCustomers();
            customerCombo.getItems().addAll(customers);
        } catch (Exception e) {
            System.err.println("Error loading customers: " + e.getMessage());
            e.printStackTrace();
        }

        // Set up status combo
        statusCombo.getItems().addAll(
                "Pending",
                "Confirmed",
                "Delivered",
                "Cancelled"
        );
        statusCombo.setValue("Pending");

        // Set up furniture type combo
        try {
            List<FurnitureType> furnitureTypes = furnitureTypeService.findAllFurnitureTypes();
            itemTypeCombo.getItems().addAll(furnitureTypes);
        } catch (Exception e) {
            System.err.println("Error loading furniture types: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        backButton.setOnAction(this::handleBackButtonAction);
        saveAsDraftButton.setOnAction(this::handleSaveAsDraftButtonAction);
        createOrderButton.setOnAction(this::handleCreateOrderButtonAction);
        addItemButton.setOnAction(this::handleAddItemButtonAction);

        // Set up furniture type change listener
        itemTypeCombo.setOnAction(this::handleFurnitureTypeChange);

        // Set up discount field change listener
        discountField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotals();
        });
    }

    private void setupInitialValues() {
        orderDatePicker.setValue(java.time.LocalDate.now());
        salesPersonField.setText("Current User"); // TODO: Get from security context

        // Set initial values for calculation fields
        subtotalField.setText("0.00");
        discountField.setText("0.00");
        taxField.setText("0.00");
        totalField.setText("0.00");
    }

    private void handleFurnitureTypeChange(ActionEvent event) {
        FurnitureType selectedType = itemTypeCombo.getValue();
        if (selectedType != null) {
            try {
                List<InventoryItem> availableItems = inventoryService.findAvailableItemsByFurnitureTypeId(selectedType.getId());
                itemCombo.getItems().clear();
                itemCombo.getItems().addAll(availableItems);

                // Set default price
                priceField.setText(selectedType.getBasePrice().toString());
            } catch (Exception e) {
                System.err.println("Error loading available items: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void calculateTotals() {
        try {
            // Get subtotal from order items
            BigDecimal subtotal = new BigDecimal("0.00"); // TODO: Calculate from order items

            // Get discount
            BigDecimal discount = new BigDecimal(discountField.getText().isEmpty() ? "0.00" : discountField.getText());

            // Calculate tax (17% VAT)
            BigDecimal taxRate = new BigDecimal("0.17");
            BigDecimal taxableAmount = subtotal.subtract(discount);
            BigDecimal tax = taxableAmount.multiply(taxRate);

            // Calculate total
            BigDecimal total = taxableAmount.add(tax);

            // Update fields
            subtotalField.setText(subtotal.toString());
            taxField.setText(tax.toString());
            totalField.setText(total.toString());
        } catch (NumberFormatException e) {
            System.err.println("Error calculating totals: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.SALES);
    }

    @FXML
    private void handleSaveAsDraftButtonAction(ActionEvent event) {
        // TODO: Implement save as draft functionality
        System.out.println("Save as draft button clicked");
        stageManager.switchScene(FXMLView.SALES);
    }

    @FXML
    private void handleCreateOrderButtonAction(ActionEvent event) {
        // TODO: Implement create order functionality
        System.out.println("Create order button clicked");
        stageManager.switchScene(FXMLView.SALES);
    }

    @FXML
    private void handleAddItemButtonAction(ActionEvent event) {
        // TODO: Implement add item functionality
        System.out.println("Add item button clicked");

        InventoryItem selectedItem = itemCombo.getValue();
        String quantityStr = quantityField.getText();
        String priceStr = priceField.getText();

        if (selectedItem != null && !quantityStr.isEmpty() && !priceStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                BigDecimal price = new BigDecimal(priceStr);
                BigDecimal lineTotal = price.multiply(new BigDecimal(quantity));

                System.out.println("Item: " + selectedItem.getFurnitureType().getName());
                System.out.println("Quantity: " + quantity);
                System.out.println("Price: " + price);
                System.out.println("Line Total: " + lineTotal);

                // TODO: Add to order items table

                // Clear selection
                itemCombo.setValue(null);
                quantityField.clear();

                // Recalculate totals
                calculateTotals();
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + e.getMessage());
            }
        }
    }
}
