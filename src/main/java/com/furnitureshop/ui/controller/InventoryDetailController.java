package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.RefurbishmentRecord;
import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.service.FurnitureTypeService;
import com.furnitureshop.service.InventoryService;
import com.furnitureshop.service.RefurbishmentService;
import com.furnitureshop.service.SalesOrderService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.collections.FXCollections;
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
public class InventoryDetailController implements Initializable {

    @FXML
    private TextField itemIdField;

    @FXML
    private TextField serialNumberField;

    @FXML
    private ComboBox<FurnitureType> furnitureTypeCombo;

    @FXML
    private ComboBox<String> conditionCombo;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private DatePicker acquisitionDatePicker;

    @FXML
    private TextField purchasePriceField;

    @FXML
    private TextField lastRefurbishmentField;

    @FXML
    private TextArea notesArea;

    @FXML
    private TableView<RefurbishmentRecord> refurbishmentTable;

    @FXML
    private TableView<SalesOrder> salesHistoryTable;

    @FXML
    private Button backButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addRefurbishmentButton;

    private final StageManager stageManager;
    private final InventoryService inventoryService;
    private final FurnitureTypeService furnitureTypeService;
    private final RefurbishmentService refurbishmentService;
    private final SalesOrderService salesOrderService;

    private InventoryItem currentItem;

    @Autowired
    public InventoryDetailController(StageManager stageManager,
                                     InventoryService inventoryService,
                                     FurnitureTypeService furnitureTypeService,
                                     RefurbishmentService refurbishmentService,
                                     SalesOrderService salesOrderService) {
        this.stageManager = stageManager;
        this.inventoryService = inventoryService;
        this.furnitureTypeService = furnitureTypeService;
        this.refurbishmentService = refurbishmentService;
        this.salesOrderService = salesOrderService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupEventHandlers();
    }

    private void setupComboBoxes() {
        // Set up furniture type combo
        try {
            List<FurnitureType> furnitureTypes = furnitureTypeService.findAllFurnitureTypes();
            furnitureTypeCombo.getItems().addAll(furnitureTypes);
        } catch (Exception e) {
            System.err.println("Error loading furniture types: " + e.getMessage());
            e.printStackTrace();
        }

        // Set up condition combo
        conditionCombo.getItems().addAll(
                "New",
                "Used",
                "Refurbished",
                "Damaged"
        );

        // Set up status combo
        statusCombo.getItems().addAll(
                "Available",
                "Sold",
                "Reserved",
                "Under Refurbishment"
        );
    }

    private void setupEventHandlers() {
        backButton.setOnAction(this::handleBackButtonAction);
        saveButton.setOnAction(this::handleSaveButtonAction);
        deleteButton.setOnAction(this::handleDeleteButtonAction);
        addRefurbishmentButton.setOnAction(this::handleAddRefurbishmentButtonAction);
    }

    public void loadItem(Long itemId) {
        if (itemId != null) {
            try {
                inventoryService.findInventoryItemById(itemId).ifPresent(item -> {
                    currentItem = item;
                    populateFields(item);
                    loadRefurbishmentHistory(item);
                    // TODO: Load sales history
                });
            } catch (Exception e) {
                System.err.println("Error loading inventory item: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // New item
            currentItem = null;
            clearFields();
        }
    }

    private void populateFields(InventoryItem item) {
        itemIdField.setText(item.getId().toString());
        serialNumberField.setText(item.getSerialNumber());
        furnitureTypeCombo.setValue(item.getFurnitureType());
        conditionCombo.setValue(item.getCondition());
        locationField.setText(item.getLocation());
        statusCombo.setValue(item.getStatus());
        acquisitionDatePicker.setValue(item.getAcquisitionDate().toLocalDate());
        purchasePriceField.setText(item.getPurchasePrice().toString());

        if (item.getLastRefurbishmentDate() != null) {
            lastRefurbishmentField.setText(item.getLastRefurbishmentDate().toString());
        } else {
            lastRefurbishmentField.setText("Never");
        }

        notesArea.setText(item.getNotes());
    }

    private void clearFields() {
        itemIdField.clear();
        serialNumberField.clear();
        furnitureTypeCombo.setValue(null);
        conditionCombo.setValue("New");
        locationField.clear();
        statusCombo.setValue("Available");
        acquisitionDatePicker.setValue(java.time.LocalDate.now());
        purchasePriceField.clear();
        lastRefurbishmentField.setText("Never");
        notesArea.clear();
    }



    private void loadRefurbishmentHistory(InventoryItem item) {
        try {
            List<RefurbishmentRecord> records = refurbishmentService.findRefurbishmentRecordsByInventoryItem(item);
            // TODO: Set up table data binding
        } catch (Exception e) {
            System.err.println("Error loading refurbishment history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.INVENTORY);
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            if (currentItem == null) {
                // Create a new inventory item
                InventoryItem newItem = new InventoryItem();
                populateItemFromFields(newItem);
                inventoryService.createInventoryItem(newItem);
                showAlert("Success", "New inventory item saved successfully!");
            } else {
                // Update the existing inventory item
                populateItemFromFields(currentItem);
                inventoryService.updateInventoryItem(currentItem);
                showAlert("Success", "Inventory item updated successfully!");
            }
            stageManager.switchScene(FXMLView.INVENTORY);
        } catch (Exception e) {
            showAlert("Error", "Failed to save inventory item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSalesHistory(InventoryItem item) {
        try {
            List<SalesOrder> salesHistory = salesOrderService.findSalesOrdersByInventoryItem(item);
            salesHistoryTable.getItems().setAll(FXCollections.observableArrayList(salesHistory));
        } catch (Exception e) {
            System.err.println("Error loading sales history: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void populateItemFromFields(InventoryItem item) {
        item.setSerialNumber(serialNumberField.getText());
        item.setFurnitureType(furnitureTypeCombo.getValue());
        item.setCondition(conditionCombo.getValue());
        item.setLocation(locationField.getText());
        item.setStatus(statusCombo.getValue());
        item.setAcquisitionDate(acquisitionDatePicker.getValue().atStartOfDay());
        item.setPurchasePrice(BigDecimal.valueOf(Double.parseDouble(purchasePriceField.getText())));
        item.setNotes(notesArea.getText());
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        // TODO: Implement delete functionality
        System.out.println("Delete button clicked");
        stageManager.switchScene(FXMLView.INVENTORY);
    }

    @FXML
    private void handleAddRefurbishmentButtonAction(ActionEvent event) {
        // TODO: Implement add refurbishment functionality
        System.out.println("Add refurbishment button clicked");
    }
}
