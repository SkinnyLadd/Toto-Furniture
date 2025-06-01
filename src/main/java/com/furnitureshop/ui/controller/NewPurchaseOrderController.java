package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.PurchaseOrder;
import com.furnitureshop.model.entity.Supplier;
import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.service.PurchaseOrderService;
import com.furnitureshop.service.SupplierService;
import com.furnitureshop.service.FurnitureTypeService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class NewPurchaseOrderController implements Initializable {

    @FXML
    private TextField orderNumberField;

    @FXML
    private ComboBox<Supplier> supplierCombo;

    @FXML
    private DatePicker orderDatePicker;

    @FXML
    private DatePicker expectedDeliveryPicker;

    @FXML
    private ComboBox<FurnitureType> itemTypeCombo;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField unitPriceField;

    @FXML
    private Button addItemButton;

    @FXML
    private TableView<PurchaseOrderItem> itemsTable;

    @FXML
    private TextField subtotalField;

    @FXML
    private TextField taxField;

    @FXML
    private TextField totalField;

    @FXML
    private TextArea notesArea;

    @FXML
    private Button saveDraftButton;

    @FXML
    private Button createOrderButton;

    @FXML
    private Button backButton;

    private final StageManager stageManager;
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final FurnitureTypeService furnitureTypeService;
    private final NumberFormat currencyFormat;
    private final ObservableList<PurchaseOrderItem> orderItems;

    @Autowired
    public NewPurchaseOrderController(StageManager stageManager,
                                      PurchaseOrderService purchaseOrderService,
                                      SupplierService supplierService,
                                      FurnitureTypeService furnitureTypeService) {
        this.stageManager = stageManager;
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
        this.furnitureTypeService = furnitureTypeService;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
        this.orderItems = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupEventHandlers();
        setupInitialValues();
        setupTable();
    }

    private void setupComboBoxes() {
        // Setup supplier combo
        try {
            List<Supplier> suppliers = supplierService.findActiveSuppliers();
            supplierCombo.getItems().addAll(suppliers);

            supplierCombo.setCellFactory(listView -> new ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    if (empty || supplier == null) {
                        setText(null);
                    } else {
                        setText(supplier.getName() + " - " + supplier.getCity());
                    }
                }
            });

            supplierCombo.setButtonCell(new ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    if (empty || supplier == null) {
                        setText("Select Supplier");
                    } else {
                        setText(supplier.getName() + " - " + supplier.getCity());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading suppliers: " + e.getMessage());
        }

        // Setup furniture type combo
        try {
            List<FurnitureType> furnitureTypes = furnitureTypeService.findAllFurnitureTypes();
            itemTypeCombo.getItems().addAll(furnitureTypes);

            itemTypeCombo.setCellFactory(listView -> new ListCell<FurnitureType>() {
                @Override
                protected void updateItem(FurnitureType type, boolean empty) {
                    super.updateItem(type, empty);
                    if (empty || type == null) {
                        setText(null);
                    } else {
                        setText(type.getName() + " - " + type.getCategory());
                    }
                }
            });

            itemTypeCombo.setButtonCell(new ListCell<FurnitureType>() {
                @Override
                protected void updateItem(FurnitureType type, boolean empty) {
                    super.updateItem(type, empty);
                    if (empty || type == null) {
                        setText("Select Item Type");
                    } else {
                        setText(type.getName() + " - " + type.getCategory());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading furniture types: " + e.getMessage());
        }
    }

    private void setupEventHandlers() {
        backButton.setOnAction(this::handleBackButtonAction);
        addItemButton.setOnAction(this::handleAddItemButtonAction);
        saveDraftButton.setOnAction(this::handleSaveDraftButtonAction);
        createOrderButton.setOnAction(this::handleCreateOrderButtonAction);

        itemTypeCombo.setOnAction(this::handleItemTypeChange);
    }

    private void setupInitialValues() {
        orderNumberField.setText("PO-" + System.currentTimeMillis());
        orderDatePicker.setValue(java.time.LocalDate.now());
        expectedDeliveryPicker.setValue(java.time.LocalDate.now().plusDays(7));

        subtotalField.setText("0.00");
        taxField.setText("0.00");
        totalField.setText("0.00");
    }

    private void setupTable() {
        itemsTable.setItems(orderItems);
    }

    private void handleItemTypeChange(ActionEvent event) {
        FurnitureType selectedType = itemTypeCombo.getValue();
        if (selectedType != null) {
            unitPriceField.setText(selectedType.getBasePrice().toString());
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.PROCUREMENT);
    }

    @FXML
    private void handleAddItemButtonAction(ActionEvent event) {
        FurnitureType selectedType = itemTypeCombo.getValue();
        String quantityStr = quantityField.getText();
        String priceStr = unitPriceField.getText();

        if (selectedType != null && !quantityStr.isEmpty() && !priceStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                BigDecimal unitPrice = new BigDecimal(priceStr);

                // Create purchase order item (simplified)
                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setFurnitureType(selectedType);
                item.setQuantity(quantity);
                item.setUnitPrice(unitPrice);
                item.setLineTotal(unitPrice.multiply(new BigDecimal(quantity)));

                orderItems.add(item);

                // Clear fields
                itemTypeCombo.setValue(null);
                quantityField.clear();
                unitPriceField.clear();

                calculateTotals();

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter valid numbers for quantity and price.");
            }
        } else {
            showAlert("Missing Information", "Please select an item type and enter quantity and price.");
        }
    }

    @FXML
    private void handleSaveDraftButtonAction(ActionEvent event) {
        if (validateOrder()) {
            try {
                PurchaseOrder order = createPurchaseOrder();
                order.setStatus("Draft");
                purchaseOrderService.savePurchaseOrder(order);
                showAlert("Success", "Purchase order saved as draft successfully!");
                stageManager.switchScene(FXMLView.PROCUREMENT);
            } catch (Exception e) {
                showAlert("Error", "Failed to save purchase order: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCreateOrderButtonAction(ActionEvent event) {
        if (validateOrder()) {
            try {
                PurchaseOrder order = createPurchaseOrder();
                order.setStatus("Sent");
                purchaseOrderService.savePurchaseOrder(order);
                showAlert("Success", "Purchase order created and sent successfully!");
                stageManager.switchScene(FXMLView.PROCUREMENT);
            } catch (Exception e) {
                showAlert("Error", "Failed to create purchase order: " + e.getMessage());
            }
        }
    }

    private boolean validateOrder() {
        if (supplierCombo.getValue() == null) {
            showAlert("Validation Error", "Please select a supplier.");
            return false;
        }

        if (orderItems.isEmpty()) {
            showAlert("Validation Error", "Please add at least one item to the order.");
            return false;
        }

        return true;
    }

    private PurchaseOrder createPurchaseOrder() {
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNumber(orderNumberField.getText());
        order.setSupplier(supplierCombo.getValue());
        order.setOrderDate(orderDatePicker.getValue().atStartOfDay());
        order.setExpectedDeliveryDate(expectedDeliveryPicker.getValue().atStartOfDay());
        order.setNotes(notesArea.getText());

        BigDecimal total = new BigDecimal(totalField.getText());
        order.setTotalAmount(total);

        return order;
    }

    private void calculateTotals() {
        BigDecimal subtotal = orderItems.stream()
                .map(PurchaseOrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tax = subtotal.multiply(new BigDecimal("0.17")); // 17% tax
        BigDecimal total = subtotal.add(tax);

        subtotalField.setText(subtotal.toString());
        taxField.setText(tax.toString());
        totalField.setText(total.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for table items
    public static class PurchaseOrderItem {
        private FurnitureType furnitureType;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;

        // Getters and setters
        public FurnitureType getFurnitureType() { return furnitureType; }
        public void setFurnitureType(FurnitureType furnitureType) { this.furnitureType = furnitureType; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getLineTotal() { return lineTotal; }
        public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
    }
}
