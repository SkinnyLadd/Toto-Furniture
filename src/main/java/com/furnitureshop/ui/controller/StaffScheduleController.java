package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.service.AssignmentService;
import com.furnitureshop.service.StaffService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class StaffScheduleController implements Initializable {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<StaffMember> staffFilter;

    @FXML
    private ComboBox<String> departmentFilter;

    @FXML
    private Button viewButton;

    @FXML
    private TableView<Assignment> scheduleTable;

    @FXML
    private Button addShiftButton;

    @FXML
    private Button assignStaffButton;

    @FXML
    private Button generatePayrollButton;

    @FXML
    private Button printScheduleButton;

    @FXML
    private Button exportScheduleButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button salesButton;

    @FXML
    private Button staffButton;

    private final StageManager stageManager;
    private final StaffService staffService;
    private final AssignmentService assignmentService;

    @Autowired
    public StaffScheduleController(StageManager stageManager,
                                   StaffService staffService,
                                   AssignmentService assignmentService) {
        this.stageManager = stageManager;
        this.staffService = staffService;
        this.assignmentService = assignmentService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFilters();
        setupEventHandlers();
        loadScheduleData();
    }

    private void setupFilters() {
        // Set up staff filter
        try {
            List<StaffMember> staffMembers = staffService.findActiveStaffMembers();
            staffFilter.getItems().add(null); // Add "All Staff" option
            staffFilter.getItems().addAll(staffMembers);
        } catch (Exception e) {
            System.err.println("Error loading staff members: " + e.getMessage());
            e.printStackTrace();
        }

        // Set up department filter
        departmentFilter.getItems().addAll(
                "All Departments",
                "Management",
                "Sales",
                "Technical"
        );
        departmentFilter.setValue("All Departments");

        // Set up date pickers
        startDatePicker.setValue(java.time.LocalDate.now());
        endDatePicker.setValue(java.time.LocalDate.now().plusDays(7));
    }

    private void setupEventHandlers() {
        dashboardButton.setOnAction(this::handleDashboardButtonAction);
        inventoryButton.setOnAction(this::handleInventoryButtonAction);
        salesButton.setOnAction(this::handleSalesButtonAction);
        staffButton.setOnAction(this::handleStaffButtonAction);

        viewButton.setOnAction(this::handleViewButtonAction);
        addShiftButton.setOnAction(this::handleAddShiftButtonAction);
        assignStaffButton.setOnAction(this::handleAssignStaffButtonAction);
        generatePayrollButton.setOnAction(this::handleGeneratePayrollButtonAction);
        printScheduleButton.setOnAction(this::handlePrintScheduleButtonAction);
        exportScheduleButton.setOnAction(this::handleExportScheduleButtonAction);
    }

    private void loadScheduleData() {
        try {
            LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
            LocalDateTime endDate = endDatePicker.getValue().atTime(23, 59, 59);

            StaffMember selectedStaff = staffFilter.getValue();
            String selectedDepartment = departmentFilter.getValue();

            List<Assignment> assignments;

            if (selectedStaff != null) {
                // Filter by staff member
                assignments = assignmentService.findAssignmentsByStaffMember(selectedStaff);
            } else if (!"All Departments".equals(selectedDepartment)) {
                // Filter by department
                // TODO: Implement department filtering
                assignments = assignmentService.findAllAssignments();
            } else {
                // No filters
                assignments = assignmentService.findAllAssignments();
            }

            // TODO: Set up table data binding
        } catch (Exception e) {
            System.err.println("Error loading schedule data: " + e.getMessage());
            e.printStackTrace();
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
    private void handleStaffButtonAction(ActionEvent event) {
        // Already on staff schedule, no need to navigate
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        // Clear security context
        SecurityContextHolder.clearContext();
        // Return to login screen
        stageManager.switchScene(FXMLView.LOGIN);
    }

    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        loadScheduleData();
    }

    @FXML
    private void handleAddShiftButtonAction(ActionEvent event) {
        // TODO: Implement add shift functionality
        System.out.println("Add shift button clicked");
    }

    @FXML
    private void handleAssignStaffButtonAction(ActionEvent event) {
        // TODO: Implement assign staff functionality
        System.out.println("Assign staff button clicked");
    }

    @FXML
    private void handleGeneratePayrollButtonAction(ActionEvent event) {
        // TODO: Implement generate payroll functionality
        System.out.println("Generate payroll button clicked");
    }

    @FXML
    private void handlePrintScheduleButtonAction(ActionEvent event) {
        // TODO: Implement print schedule functionality
        System.out.println("Print schedule button clicked");
    }

    @FXML
    private void handleExportScheduleButtonAction(ActionEvent event) {
        // TODO: Implement export schedule functionality
        System.out.println("Export schedule button clicked");
    }
}
