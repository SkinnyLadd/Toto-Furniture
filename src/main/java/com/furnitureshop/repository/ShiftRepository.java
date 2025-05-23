package com.furnitureshop.repository;

import com.furnitureshop.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByActive(boolean active);
    
    @Query("SELECT s FROM Shift s WHERE s.startTime BETWEEN :start AND :end OR s.endTime BETWEEN :start AND :end")
    List<Shift> findShiftsInTimeRange(LocalDateTime start, LocalDateTime end);
}
