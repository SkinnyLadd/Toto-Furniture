package com.furnitureshop.service;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> findAssignmentsByStaffMember(StaffMember staffMember) {
        return assignmentRepository.findByStaffMember(staffMember);
    }

    public List<Assignment> findAssignmentsByStaffMemberAndDateRange(StaffMember staffMember, LocalDateTime startDate, LocalDateTime endDate) {
        return assignmentRepository.findByStaffMemberAndAssignmentDateBetween(staffMember, startDate, endDate);
    }

    public List<Assignment> findAssignmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return assignmentRepository.findByAssignmentDateBetween(startDate, endDate);
    }


    public Assignment createAssignment(Assignment assignment) {
        assignment.setCreatedDate(LocalDateTime.now());
        assignment.setUpdatedDate(LocalDateTime.now());
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Assignment assignment) {
        assignment.setUpdatedDate(LocalDateTime.now());
        return assignmentRepository.save(assignment);
    }

    public Optional<Assignment> findAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    public List<Assignment> findAssignmentsByStatus(String status) {
        return assignmentRepository.findByStatus(status);
    }

    public List<Assignment> findActiveAssignments() {
        return assignmentRepository.findByStatus("Active");
    }
}
