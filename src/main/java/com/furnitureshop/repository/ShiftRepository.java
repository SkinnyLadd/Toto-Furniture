package com.furnitureshop.repository;

import com.furnitureshop.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByIsActiveTrue();

    List<Shift> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Shift> findByShiftNameContainingIgnoreCase(String shiftName);

    @Query("SELECT s FROM Shift s WHERE s.startTime <= :currentTime AND s.endTime >= :currentTime AND s.isActive = true")
    List<Shift> findCurrentActiveShifts(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT s FROM Shift s WHERE s.isActive = true ORDER BY s.startTime")
    List<Shift> findAllActiveShiftsOrderByStartTime();
}
