package com.furnitureshop.service;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.RefurbishmentRecord;
import com.furnitureshop.model.entity.StaffMember;
import com.furnitureshop.repository.InventoryItemRepository;
import com.furnitureshop.repository.RefurbishmentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RefurbishmentService {

    private final RefurbishmentRecordRepository refurbishmentRecordRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public RefurbishmentService(RefurbishmentRecordRepository refurbishmentRecordRepository, 
                               InventoryItemRepository inventoryItemRepository) {
        this.refurbishmentRecordRepository = refurbishmentRecordRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional(readOnly = true)
    public List<RefurbishmentRecord> findAllRefurbishmentRecords() {
        return refurbishmentRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RefurbishmentRecord> findRefurbishmentRecordById(Long id) {
        return refurbishmentRecordRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<RefurbishmentRecord> findRefurbishmentRecordsByInventoryItem(InventoryItem inventoryItem) {
        return refurbishmentRecordRepository.findByInventoryItem(inventoryItem);
    }

    @Transactional(readOnly = true)
    public List<RefurbishmentRecord> findRefurbishmentRecordsByStaffMember(StaffMember staffMember) {
        return refurbishmentRecordRepository.findByAssignedStaff(staffMember);
    }

    @Transactional(readOnly = true)
    public List<RefurbishmentRecord> findRefurbishmentRecordsByStatus(String status) {
        return refurbishmentRecordRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<RefurbishmentRecord> findRefurbishmentRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        return refurbishmentRecordRepository.findByStartDateBetween(start, end);
    }

    @Transactional
    public RefurbishmentRecord createRefurbishmentRecord(RefurbishmentRecord refurbishmentRecord) {
        // Update inventory item status to "Under Refurbishment"
        InventoryItem item = refurbishmentRecord.getInventoryItem();
        item.setStatus("Under Refurbishment");
        inventoryItemRepository.save(item);
        
        return refurbishmentRecordRepository.save(refurbishmentRecord);
    }

    @Transactional
    public RefurbishmentRecord updateRefurbishmentRecord(RefurbishmentRecord refurbishmentRecord) {
        return refurbishmentRecordRepository.save(refurbishmentRecord);
    }

    @Transactional
    public RefurbishmentRecord completeRefurbishment(Long id, LocalDateTime completionDate) {
        Optional<RefurbishmentRecord> recordOpt = refurbishmentRecordRepository.findById(id);
        if (recordOpt.isPresent()) {
            RefurbishmentRecord record = recordOpt.get();
            record.setCompletionDate(completionDate);
            record.setStatus("Completed");
            
            // Update inventory item status back to "Available"
            InventoryItem item = record.getInventoryItem();
            item.setStatus("Available");
            item.setLastRefurbishmentDate(completionDate);
            inventoryItemRepository.save(item);
            
            return refurbishmentRecordRepository.save(record);
        }
        return null;
    }

    @Transactional
    public void deleteRefurbishmentRecord(Long id) {
        refurbishmentRecordRepository.deleteById(id);
    }
}
