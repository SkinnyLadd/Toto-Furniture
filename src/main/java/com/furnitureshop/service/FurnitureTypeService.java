package com.furnitureshop.service;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.repository.FurnitureTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FurnitureTypeService {

    private final FurnitureTypeRepository furnitureTypeRepository;

    @Autowired
    public FurnitureTypeService(FurnitureTypeRepository furnitureTypeRepository) {
        this.furnitureTypeRepository = furnitureTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<FurnitureType> findAllFurnitureTypes() {
        return furnitureTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<FurnitureType> findFurnitureTypeById(Long id) {
        return furnitureTypeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<FurnitureType> findFurnitureTypesByCategory(String category) {
        return furnitureTypeRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<FurnitureType> findLowStockItems() {
        return furnitureTypeRepository.findLowStockItems();
    }

    @Transactional
    public FurnitureType createFurnitureType(FurnitureType furnitureType) {
        return furnitureTypeRepository.save(furnitureType);
    }

    @Transactional
    public FurnitureType updateFurnitureType(FurnitureType furnitureType) {
        return furnitureTypeRepository.save(furnitureType);
    }

    @Transactional
    public void deleteFurnitureType(Long id) {
        furnitureTypeRepository.deleteById(id);
    }
}
