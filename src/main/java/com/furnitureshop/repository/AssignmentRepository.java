package com.furnitureshop.repository;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByStaffMember(StaffMember staffMember);

    List<Assignment> findByStatus(String status);

    @Query("SELECT a FROM Assignment a WHERE a.assignedDate BETWEEN :startDate AND :endDate")
    List<Assignment> findByAssignmentDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Assignment a WHERE a.staffMember = :staffMember AND a.assignedDate BETWEEN :startDate AND :endDate")
    List<Assignment> findByStaffMemberAndAssignmentDateBetween(@Param("staffMember") StaffMember staffMember, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Assignment a WHERE a.staffMember.department = :department")
    List<Assignment> findByDepartment(@Param("department") String department);

    @Query("SELECT a FROM Assignment a WHERE a.shift.id = :shiftId")
    List<Assignment> findByShiftId(@Param("shiftId") Long shiftId);

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.staffMember = :staffMember AND a.assignedDate BETWEEN :startDate AND :endDate")
    Long countAssignmentsByStaffMemberAndDateRange(@Param("staffMember") StaffMember staffMember, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
