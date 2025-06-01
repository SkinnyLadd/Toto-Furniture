package com.furnitureshop.service;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.RefurbishmentRecord;
import com.furnitureshop.repository.RefurbishmentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RefurbishmentService {

    private final RefurbishmentRecordRepository refurbishmentRecordRepository;

    @Autowired
    public RefurbishmentService(RefurbishmentRecordRepository refurbishmentRecordRepository) {
        this.refurbishmentRecordRepository = refurbishmentRecordRepository;
    }

    public RefurbishmentRecord scheduleRefurbishment(RefurbishmentRecord record) {
        if (record.getStartDate() == null) {
            record.setStartDate(LocalDateTime.now());
        }
        if (record.getEstimatedCompletionDate() == null) {
            record.setEstimatedCompletionDate(LocalDateTime.now().plusDays(7));
        }
        if (record.getStatus() == null) {
            record.setStatus("Scheduled");
        }
        return refurbishmentRecordRepository.save(record);
    }

    public RefurbishmentRecord createRefurbishmentRecord(RefurbishmentRecord record) {
        return refurbishmentRecordRepository.save(record);
    }

    public RefurbishmentRecord updateRefurbishmentRecord(RefurbishmentRecord record) {
        return refurbishmentRecordRepository.save(record);
    }

    public Optional<RefurbishmentRecord> findRefurbishmentRecordById(Long id) {
        return refurbishmentRecordRepository.findById(id);
    }

    public List<RefurbishmentRecord> findAllRefurbishmentRecords() {
        return refurbishmentRecordRepository.findAll();
    }

    public List<RefurbishmentRecord> findRefurbishmentRecordsByStatus(String status) {
        return refurbishmentRecordRepository.findByStatus(status);
    }

    public List<RefurbishmentRecord> findRefurbishmentRecordsByInventoryItemId(Long inventoryItemId) {
        return refurbishmentRecordRepository.findByInventoryItemId(inventoryItemId);
    }

    public void deleteRefurbishmentRecord(Long id) {
        refurbishmentRecordRepository.deleteById(id);
    }

    public RefurbishmentRecord completeRefurbishment(Long id, String workPerformed, String notes) {
        Optional<RefurbishmentRecord> recordOpt = refurbishmentRecordRepository.findById(id);
        if (recordOpt.isPresent()) {
            RefurbishmentRecord record = recordOpt.get();
            record.setStatus("Completed");
            record.setCompletionDate(LocalDateTime.now());
            record.setWorkPerformed(workPerformed);
            record.setNotes(notes);
            return refurbishmentRecordRepository.save(record);
        }
        throw new RuntimeException("Refurbishment record not found with id: " + id);
    }

    public List<RefurbishmentRecord> findActiveRefurbishmentRecords() {
        return refurbishmentRecordRepository.findByStatusIn(List.of("Scheduled", "In Progress"));
    }

    public List<RefurbishmentRecord> findRefurbishmentRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return refurbishmentRecordRepository.findByStartDateBetween(startDate, endDate);
    }

    public List<RefurbishmentRecord> findRefurbishmentRecordsByInventoryItem(InventoryItem item) {
        return refurbishmentRecordRepository.findByInventoryItem(item);
    }
}
