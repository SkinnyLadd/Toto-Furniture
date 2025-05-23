package com.furnitureshop.repository;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.Shift;
import com.furnitureshop.model.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByStaffMember(StaffMember staffMember);
    List<Assignment> findByShift(Shift shift);
    List<Assignment> findByStatus(String status);
    
    @Query("SELECT a FROM Assignment a WHERE a.staffMember.id = :staffId AND " +
           "((a.shift.startTime BETWEEN :start AND :end) OR (a.shift.endTime BETWEEN :start AND :end))")
    List<Assignment> findStaffAssignmentsInTimeRange(Long staffId, LocalDateTime start, LocalDateTime end);
}
