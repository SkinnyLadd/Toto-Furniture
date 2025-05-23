package com.furnitureshop.repository;

import com.furnitureshop.model.entity.PayrollRecord;
import com.furnitureshop.model.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {
    List<PayrollRecord> findByStaffMember(StaffMember staffMember);
    List<PayrollRecord> findByStatus(String status);
    List<PayrollRecord> findByPayPeriodStartAndPayPeriodEnd(LocalDate start, LocalDate end);
    List<PayrollRecord> findByStaffMemberAndPayPeriodStartAndPayPeriodEnd(StaffMember staffMember, LocalDate start, LocalDate end);
}
