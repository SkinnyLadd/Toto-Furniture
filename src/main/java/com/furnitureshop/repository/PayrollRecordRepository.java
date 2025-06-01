package com.furnitureshop.repository;

import com.furnitureshop.model.entity.PayrollRecord;
import com.furnitureshop.model.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {

    List<PayrollRecord> findByStaffMember(StaffMember staffMember);

    List<PayrollRecord> findByPeriodStartBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<PayrollRecord> findByIsPaidFalse();

    List<PayrollRecord> findByIsPaidTrue();

    @Query("SELECT SUM(p.netPay) FROM PayrollRecord p WHERE p.periodStart BETWEEN :startDate AND :endDate")
    BigDecimal sumNetPayByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM PayrollRecord p WHERE p.staffMember = :staffMember AND p.periodStart BETWEEN :startDate AND :endDate")
    List<PayrollRecord> findByStaffMemberAndPeriod(@Param("staffMember") StaffMember staffMember, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(p) FROM PayrollRecord p WHERE p.generatedDate BETWEEN :startDate AND :endDate")
    Long countPayrollRecordsByGeneratedDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
