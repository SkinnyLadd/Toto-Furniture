package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.model.entity.Shift;
import com.furnitureshop.model.entity.PayrollRecord;
import com.furnitureshop.service.AssignmentService;
import com.furnitureshop.service.StaffService;
import com.furnitureshop.service.ShiftService;
import com.furnitureshop.service.PayrollService;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

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
    private ShiftService shiftService;

    @Autowired
    private PayrollService payrollService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        printScheduleButton.setOnAction(this::handlePrintScheduleAction);
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
    private void handleProcurementButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.PROCUREMENT);
    }

    @FXML
    private void handleReportsButtonAction(ActionEvent event) {
        stageManager.switchScene(FXMLView.REPORTS);
    }

    @FXML
    private void handleViewButtonAction(ActionEvent event) {
        loadScheduleData();
    }

    @FXML
    private void handleAddShiftButtonAction(ActionEvent event) {
        try {
            // Create a new shift
            Shift newShift = new Shift();
            newShift.setShiftName("Morning Shift");
            newShift.setStartTime(LocalDateTime.now().withHour(9).withMinute(0));
            newShift.setEndTime(LocalDateTime.now().withHour(17).withMinute(0));
            newShift.setDescription("Regular morning shift");

            shiftService.createShift(newShift);

            showAlert("Success", "New shift created successfully!\nShift: " + newShift.getShiftName());
            loadScheduleData(); // Refresh the table
        } catch (Exception e) {
            showAlert("Error", "Failed to create shift: " + e.getMessage());
        }
    }

    @FXML
    private void handleAssignStaffButtonAction(ActionEvent event) {
        StaffMember selectedStaff = staffFilter.getValue();
        if (selectedStaff != null) {
            try {
                // Get available shifts
                List<Shift> availableShifts = shiftService.findAvailableShifts();
                if (!availableShifts.isEmpty()) {
                    Shift shift = availableShifts.get(0); // Use first available shift

                    // Create assignment
                    Assignment assignment = new Assignment();
                    assignment.setStaffMember(selectedStaff);
                    assignment.setShift(shift);
                    assignment.setAssignedDate(LocalDateTime.now());
                    assignment.setStatus("Assigned");

                    assignmentService.createAssignment(assignment);

                    showAlert("Success",
                            "Staff assigned successfully!\n" +
                                    "Staff: " + selectedStaff.getFullName() + "\n" +
                                    "Shift: " + shift.getShiftName());

                    loadScheduleData(); // Refresh the table
                } else {
                    showAlert("No Shifts Available", "No shifts available for assignment. Please create a shift first.");
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to assign staff: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a staff member to assign.");
        }
    }

    @FXML
    private void handleGeneratePayrollButtonAction(ActionEvent event) {
        try {
            LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
            LocalDateTime endDate = endDatePicker.getValue().atTime(23, 59, 59);

            List<StaffMember> activeStaff = staffService.findActiveStaffMembers();
            int payrollRecordsGenerated = 0;

            for (StaffMember staff : activeStaff) {
                // Calculate hours worked in the period
                List<Assignment> assignments = assignmentService.findAssignmentsByStaffMemberAndDateRange(
                        staff, startDate, endDate);

                if (!assignments.isEmpty()) {
                    PayrollRecord payroll = new PayrollRecord();
                    payroll.setStaffMember(staff);
                    payroll.setPeriodStart(startDate);
                    payroll.setPeriodEnd(endDate);
                    payroll.setHoursWorked(assignments.size() * 8); // Assuming 8 hours per assignment
                    payroll.setHourlyRate(staff.getHourlyRate());
                    payroll.setGrossPay(payroll.getHourlyRate().multiply(new BigDecimal(payroll.getHoursWorked())));
                    payroll.setNetPay(payroll.getGrossPay().multiply(new BigDecimal("0.83"))); // 17% tax deduction
                    payroll.setGeneratedDate(LocalDateTime.now());

                    payrollService.generatePayroll(payroll);
                    payrollRecordsGenerated++;
                }
            }

            showAlert("Success",
                    "Payroll generated successfully!\n" +
                            "Records generated: " + payrollRecordsGenerated + "\n" +
                            "Period: " + startDate.format(dateFormatter) + " to " + endDate.format(dateFormatter));

        } catch (Exception e) {
            showAlert("Error", "Failed to generate payroll: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrintScheduleAction(ActionEvent event) {
        // TODO: Implement print schedule functionality
        System.out.println("Print schedule button clicked");
    }

    @FXML
    private void handleExportScheduleButtonAction(ActionEvent event) {
        // TODO: Implement export schedule functionality
        System.out.println("Export schedule button clicked");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
