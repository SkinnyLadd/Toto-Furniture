package com.furnitureshop.service;

import com.furnitureshop.model.entity.Shift;
import com.furnitureshop.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    public List<Shift> findAllShifts() {
        return shiftRepository.findAll();
    }

    public List<Shift> findAvailableShifts() {
        return shiftRepository.findByIsActiveTrue();
    }

    public Shift createShift(Shift shift) {
        shift.setCreatedDate(LocalDateTime.now());
        shift.setUpdatedDate(LocalDateTime.now());
        shift.setIsActive(true);
        return shiftRepository.save(shift);
    }

    public Shift updateShift(Shift shift) {
        shift.setUpdatedDate(LocalDateTime.now());
        return shiftRepository.save(shift);
    }

    public Optional<Shift> findShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    public void deleteShift(Long id) {
        shiftRepository.deleteById(id);
    }

    public List<Shift> findShiftsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return shiftRepository.findByStartTimeBetween(startTime, endTime);
    }

    public List<Shift> findActiveShifts() {
        return shiftRepository.findByIsActiveTrue();
    }

    public void deactivateShift(Long id) {
        Optional<Shift> shift = shiftRepository.findById(id);
        if (shift.isPresent()) {
            Shift s = shift.get();
            s.setIsActive(false);
            s.setUpdatedDate(LocalDateTime.now());
            shiftRepository.save(s);
        }
    }
}
