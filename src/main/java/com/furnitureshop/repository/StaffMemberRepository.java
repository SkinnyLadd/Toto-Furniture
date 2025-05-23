package com.furnitureshop.repository;

import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {
    Optional<StaffMember> findByUser(User user);
    List<StaffMember> findByDepartment(String department);
    List<StaffMember> findByPosition(String position);
    List<StaffMember> findByActive(boolean active);
}
