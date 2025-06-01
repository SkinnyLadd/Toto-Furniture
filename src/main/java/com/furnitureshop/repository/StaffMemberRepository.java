package com.furnitureshop.repository;

import com.furnitureshop.model.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {

    List<StaffMember> findByIsActiveTrue();

    List<StaffMember> findByDepartment(String department);

    List<StaffMember> findByPosition(String position);

    List<StaffMember> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email);

    List<StaffMember> findByHourlyRateBetween(BigDecimal minRate, BigDecimal maxRate);

    @Query("SELECT DISTINCT s.department FROM StaffMember s WHERE s.isActive = true")
    List<String> findDistinctDepartments();

    @Query("SELECT DISTINCT s.position FROM StaffMember s WHERE s.isActive = true")
    List<String> findDistinctPositions();

    @Query("SELECT COUNT(s) FROM StaffMember s WHERE s.isActive = true")
    Long countByIsActiveTrue();

    @Query("SELECT s FROM StaffMember s WHERE s.department = :department AND s.isActive = true")
    List<StaffMember> findActiveStaffMembersByDepartment(@Param("department") String department);

    @Query("SELECT s FROM StaffMember s WHERE s.isActive = true ORDER BY s.fullName")
    List<StaffMember> findAllActiveOrderByName();
}
