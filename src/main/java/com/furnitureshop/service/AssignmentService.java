package com.furnitureshop.service;

import com.furnitureshop.model.entity.Assignment;
import com.furnitureshop.model.entity.Shift;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> findAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Assignment> findAssignmentsByStaffMember(StaffMember staffMember) {
        return assignmentRepository.findByStaffMember(staffMember);
    }

    @Transactional(readOnly = true)
    public List<Assignment> findAssignmentsByShift(Shift shift) {
        return assignmentRepository.findByShift(shift);
    }

    @Transactional(readOnly = true)
    public List<Assignment> findAssignmentsByStatus(String status) {
        return assignmentRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Assignment> findStaffAssignmentsInTimeRange(Long staffId, LocalDateTime start, LocalDateTime end) {
        return assignmentRepository.findStaffAssignmentsInTimeRange(staffId, start, end);
    }

    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignmentStatus(Long id, String newStatus) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isPresent()) {
            Assignment assignment = assignmentOpt.get();
            assignment.setStatus(newStatus);
            return assignmentRepository.save(assignment);
        }
        return null;
    }

    @Transactional
    public Assignment recordCheckIn(Long id) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isPresent()) {
            Assignment assignment = assignmentOpt.get();
            assignment.setCheckInTime(LocalDateTime.now());
            return assignmentRepository.save(assignment);
        }
        return null;
    }

    @Transactional
    public Assignment recordCheckOut(Long id) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isPresent()) {
            Assignment assignment = assignmentOpt.get();
            assignment.setCheckOutTime(LocalDateTime.now());
            assignment.setStatus("Completed");
            return assignmentRepository.save(assignment);
        }
        return null;
    }

    @Transactional
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}
