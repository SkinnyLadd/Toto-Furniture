package com.furnitureshop.service;

import com.furnitureshop.model.entity.PayrollRecord;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.repository.PayrollRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    @Autowired
    private PayrollRecordRepository payrollRecordRepository;

    public List<PayrollRecord> findAllPayrollRecords() {
        return payrollRecordRepository.findAll();
    }

    public List<PayrollRecord> findPayrollRecordsByStaffMember(StaffMember staffMember) {
        return payrollRecordRepository.findByStaffMember(staffMember);
    }

    public List<PayrollRecord> findPayrollRecordsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return payrollRecordRepository.findByPeriodStartBetween(startDate, endDate);
    }

    public PayrollRecord generatePayroll(PayrollRecord payrollRecord) {
        payrollRecord.setGeneratedDate(LocalDateTime.now());

        // Calculate gross pay if not already set
        if (payrollRecord.getGrossPay() == null) {
            BigDecimal grossPay = payrollRecord.getHourlyRate()
                    .multiply(new BigDecimal(payrollRecord.getHoursWorked()));
            payrollRecord.setGrossPay(grossPay);
        }

        // Calculate net pay if not already set (assuming 17% tax deduction)
        if (payrollRecord.getNetPay() == null) {
            BigDecimal netPay = payrollRecord.getGrossPay()
                    .multiply(new BigDecimal("0.83"));
            payrollRecord.setNetPay(netPay);
        }

        return payrollRecordRepository.save(payrollRecord);
    }

    public PayrollRecord updatePayrollRecord(PayrollRecord payrollRecord) {
        return payrollRecordRepository.save(payrollRecord);
    }

    public Optional<PayrollRecord> findPayrollRecordById(Long id) {
        return payrollRecordRepository.findById(id);
    }

    public void deletePayrollRecord(Long id) {
        payrollRecordRepository.deleteById(id);
    }

    public BigDecimal calculateTotalPayrollForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<PayrollRecord> records = findPayrollRecordsByPeriod(startDate, endDate);
        return records.stream()
                .map(PayrollRecord::getNetPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<PayrollRecord> findUnpaidPayrollRecords() {
        return payrollRecordRepository.findByIsPaidFalse();
    }

    public void markPayrollAsPaid(Long payrollId) {
        Optional<PayrollRecord> record = payrollRecordRepository.findById(payrollId);
        if (record.isPresent()) {
            PayrollRecord payroll = record.get();
            payroll.setIsPaid(true);
            payroll.setPaidDate(LocalDateTime.now());
            payrollRecordRepository.save(payroll);
        }
    }
}
