package com.furnitureshop.service;

import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.repository.StaffMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffMemberRepository staffMemberRepository;

    public List<StaffMember> findAllStaffMembers() {
        return staffMemberRepository.findAll();
    }

    public List<StaffMember> findActiveStaffMembers() {
        return staffMemberRepository.findByIsActiveTrue();
    }

    public List<StaffMember> findStaffMembersByDepartment(String department) {
        return staffMemberRepository.findByDepartment(department);
    }

    public List<StaffMember> findStaffMembersByPosition(String position) {
        return staffMemberRepository.findByPosition(position);
    }

    public StaffMember createStaffMember(StaffMember staffMember) {
        staffMember.setCreatedDate(LocalDateTime.now());
        staffMember.setUpdatedDate(LocalDateTime.now());
        staffMember.setIsActive(true);

        // Set default hourly rate if not provided
        if (staffMember.getHourlyRate() == null) {
            staffMember.setHourlyRate(new BigDecimal("500.00")); // Default PKR 500 per hour
        }

        return staffMemberRepository.save(staffMember);
    }

    public StaffMember updateStaffMember(StaffMember staffMember) {
        staffMember.setUpdatedDate(LocalDateTime.now());
        return staffMemberRepository.save(staffMember);
    }

    public Optional<StaffMember> findStaffMemberById(Long id) {
        return staffMemberRepository.findById(id);
    }

    public void deleteStaffMember(Long id) {
        staffMemberRepository.deleteById(id);
    }

    public void deactivateStaffMember(Long id) {
        Optional<StaffMember> staffMember = staffMemberRepository.findById(id);
        if (staffMember.isPresent()) {
            StaffMember sm = staffMember.get();
            sm.setIsActive(false);
            sm.setUpdatedDate(LocalDateTime.now());
            staffMemberRepository.save(sm);
        }
    }

    public List<StaffMember> searchStaffMembers(String searchTerm) {
        return staffMemberRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm);
    }

    public List<String> findDistinctDepartments() {
        return staffMemberRepository.findDistinctDepartments();
    }

    public List<String> findDistinctPositions() {
        return staffMemberRepository.findDistinctPositions();
    }

    public Long countActiveStaffMembers() {
        return staffMemberRepository.countByIsActiveTrue();
    }

    public List<StaffMember> findStaffMembersByHourlyRateRange(BigDecimal minRate, BigDecimal maxRate) {
        return staffMemberRepository.findByHourlyRateBetween(minRate, maxRate);
    }
}
