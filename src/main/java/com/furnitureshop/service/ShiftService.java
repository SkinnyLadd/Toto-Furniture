package com.furnitureshop.service;

import com.furnitureshop.model.entity.Shift;
import com.furnitureshop.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Transactional(readOnly = true)
    public List<Shift> findAllShifts() {
        return shiftRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Shift> findActiveShifts() {
        return shiftRepository.findByActive(true);
    }

    @Transactional(readOnly = true)
    public Optional<Shift> findShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Shift> findShiftsInTimeRange(LocalDateTime start, LocalDateTime end) {
        return shiftRepository.findShiftsInTimeRange(start, end);
    }

    @Transactional
    public Shift createShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift updateShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Transactional
    public void deleteShift(Long id) {
        shiftRepository.deleteById(id);
    }

    @Transactional
    public Shift deactivateShift(Long id) {
        Optional<Shift> shiftOpt = shiftRepository.findById(id);
        if (shiftOpt.isPresent()) {
            Shift shift = shiftOpt.get();
            shift.setActive(false);
            return shiftRepository.save(shift);
        }
        return null;
    }
}
