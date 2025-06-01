package com.furnitureshop.repository;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.RefurbishmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefurbishmentRecordRepository extends JpaRepository<RefurbishmentRecord, Long> {

    List<RefurbishmentRecord> findByStatus(String status);

    List<RefurbishmentRecord> findByInventoryItemId(Long inventoryItemId);

    List<RefurbishmentRecord> findByStatusIn(List<String> statuses);

    List<RefurbishmentRecord> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT rr FROM RefurbishmentRecord rr WHERE rr.estimatedCompletionDate < :currentDate AND rr.status IN ('Scheduled', 'In Progress')")
    List<RefurbishmentRecord> findOverdueRefurbishments(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT rr FROM RefurbishmentRecord rr WHERE rr.assignedTechnician = :technician")
    List<RefurbishmentRecord> findByAssignedTechnician(@Param("technician") String technician);

    List<RefurbishmentRecord> findByInventoryItem(InventoryItem item);
}
