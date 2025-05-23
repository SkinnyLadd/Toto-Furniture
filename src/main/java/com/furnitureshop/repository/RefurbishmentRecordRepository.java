package com.furnitureshop.repository;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.RefurbishmentRecord;
import com.furnitureshop.model.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefurbishmentRecordRepository extends JpaRepository<RefurbishmentRecord, Long> {
    List<RefurbishmentRecord> findByInventoryItem(InventoryItem inventoryItem);
    List<RefurbishmentRecord> findByAssignedStaff(StaffMember staffMember);
    List<RefurbishmentRecord> findByStatus(String status);
    List<RefurbishmentRecord> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
}
