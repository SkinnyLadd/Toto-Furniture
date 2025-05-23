package com.furnitureshop.service;

import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.repository.StaffMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    private final StaffMemberRepository staffMemberRepository;

    @Autowired
    public StaffService(StaffMemberRepository staffMemberRepository) {
        this.staffMemberRepository = staffMemberRepository;
    }

    @Transactional(readOnly = true)
    public List<StaffMember> findAllStaffMembers() {
        return staffMemberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<StaffMember> findActiveStaffMembers() {
        return staffMemberRepository.findByActive(true);
    }

    @Transactional(readOnly = true)
    public Optional<StaffMember> findStaffMemberById(Long id) {
        return staffMemberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StaffMember> findStaffMembersByDepartment(String department) {
        return staffMemberRepository.findByDepartment(department);
    }

    @Transactional(readOnly = true)
    public List<StaffMember> findStaffMembersByPosition(String position) {
        return staffMemberRepository.findByPosition(position);
    }

    @Transactional
    public StaffMember createStaffMember(StaffMember staffMember) {
        return staffMemberRepository.save(staffMember);
    }

    @Transactional
    public StaffMember updateStaffMember(StaffMember staffMember) {
        return staffMemberRepository.save(staffMember);
    }

    @Transactional
    public void deleteStaffMember(Long id) {
        staffMemberRepository.deleteById(id);
    }

    @Transactional
    public StaffMember deactivateStaffMember(Long id) {
        Optional<StaffMember> staffOpt = staffMemberRepository.findById(id);
        if (staffOpt.isPresent()) {
            StaffMember staff = staffOpt.get();
            staff.setActive(false);
            return staffMemberRepository.save(staff);
        }
        return null;
    }
}
