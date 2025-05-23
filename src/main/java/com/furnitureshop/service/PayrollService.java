package com.furnitureshop.service;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.PayrollRecord;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.model.entity.User;
import com.furnitureshop.repository.AssignmentRepository;
import com.furnitureshop.repository.PayrollRecordRepository;
import com.furnitureshop.repository.StaffMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    private final PayrollRecordRepository payrollRecordRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public PayrollService(PayrollRecordRepository payrollRecordRepository,
                         StaffMemberRepository staffMemberRepository,
                         AssignmentRepository assignmentRepository) {
        this.payrollRecordRepository = payrollRecordRepository;
        this.staffMemberRepository = staffMemberRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional(readOnly = true)
    public List<PayrollRecord> findAllPayrollRecords() {
        return payrollRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PayrollRecord> findPayrollRecordById(Long id) {
        return payrollRecordRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PayrollRecord> findPayrollRecordsByStaffMember(StaffMember staffMember) {
        return payrollRecordRepository.findByStaffMember(staffMember);
    }

    @Transactional(readOnly = true)
    public List<PayrollRecord> findPayrollRecordsByStatus(String status) {
        return payrollRecordRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<PayrollRecord> findPayrollRecordsByPeriod(LocalDate start, LocalDate end) {
        return payrollRecordRepository.findByPayPeriodStartAndPayPeriodEnd(start, end);
    }

    @Transactional
    public PayrollRecord createPayrollRecord(PayrollRecord payrollRecord) {
        return payrollRecordRepository.save(payrollRecord);
    }

    @Transactional
    public PayrollRecord updatePayrollRecord(PayrollRecord payrollRecord) {
        return payrollRecordRepository.save(payrollRecord);
    }

    @Transactional
    public PayrollRecord updatePayrollRecordStatus(Long id, String newStatus) {
        Optional<PayrollRecord> recordOpt = payrollRecordRepository.findById(id);
        if (recordOpt.isPresent()) {
            PayrollRecord record = recordOpt.get();
            record.setStatus(newStatus);
            return payrollRecordRepository.save(record);
        }
        return null;
    }

    @Transactional
    public void deletePayrollRecord(Long id) {
        payrollRecordRepository.deleteById(id);
    }

    @Transactional
    public List<PayrollRecord> generatePayrollForPeriod(LocalDate startDate, LocalDate endDate, User processedBy) {
        List<StaffMember> activeStaff = staffMemberRepository.findByActive(true);
        List<PayrollRecord> generatedRecords = new ArrayList<>();
        
        LocalDateTime periodStart = startDate.atStartOfDay();
        LocalDateTime periodEnd = endDate.plusDays(1).atStartOfDay();
        
        for (StaffMember staff : activeStaff) {
            // Check if payroll already exists for this staff and period
            List<PayrollRecord> existingRecords = payrollRecordRepository.findByStaffMemberAndPayPeriodStartAndPayPeriodEnd(
                    staff, startDate, endDate);
            
            if (!existingRecords.isEmpty()) {
                continue; // Skip if already processed
            }
            
            // Get all completed assignments for this staff member in the period
            List<Assignment> assignments = assignmentRepository.findStaffAssignmentsInTimeRange(
                    staff.getId(), periodStart, periodEnd);
            
            // Calculate hours worked
            BigDecimal hoursWorked = BigDecimal.ZERO;
            for (Assignment assignment : assignments) {
                if (assignment.getStatus().equals("Completed") && 
                    assignment.getCheckInTime() != null && 
                    assignment.getCheckOutTime() != null) {
                    
                    Duration duration = Duration.between(assignment.getCheckInTime(), assignment.getCheckOutTime());
                    BigDecimal hours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
                    hoursWorked = hoursWorked.add(hours);
                }
            }
            
            // Calculate pay
            BigDecimal hourlyRate = staff.getHourlyRate();
            BigDecimal grossAmount = hoursWorked.multiply(hourlyRate);
            
            // Apply standard tax rate of 15% (this would be configurable in a real system)
            BigDecimal taxRate = new BigDecimal("0.15");
            BigDecimal taxDeductions = grossAmount.multiply(taxRate);
            
            // No other deductions for this simple example
            BigDecimal otherDeductions = BigDecimal.ZERO;
            
            // Calculate net amount
            BigDecimal netAmount = grossAmount.subtract(taxDeductions).subtract(otherDeductions);
            
            // Create payroll record
            PayrollRecord payrollRecord = new PayrollRecord();
            payrollRecord.setStaffMember(staff);
            payrollRecord.setPayPeriodStart(startDate);
            payrollRecord.setPayPeriodEnd(endDate);
            payrollRecord.setHoursWorked(hoursWorked);
            payrollRecord.setHourlyRate(hourlyRate);
            payrollRecord.setGrossAmount(grossAmount);
            payrollRecord.setTaxDeductions(taxDeductions);
            payrollRecord.setOtherDeductions(otherDeductions);
            payrollRecord.setNetAmount(netAmount);
            payrollRecord.setProcessedDate(LocalDateTime.now());
            payrollRecord.setStatus("Pending");
            payrollRecord.setProcessedBy(processedBy);
            
            PayrollRecord savedRecord = payrollRecordRepository.save(payrollRecord);
            generatedRecords.add(savedRecord);
        }
        
        return generatedRecords;
    }
}
