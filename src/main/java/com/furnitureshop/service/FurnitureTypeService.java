package com.furnitureshop.service;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.repository.FurnitureTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FurnitureTypeService {

    private final FurnitureTypeRepository furnitureTypeRepository;

    @Autowired
    public FurnitureTypeService(FurnitureTypeRepository furnitureTypeRepository) {
        this.furnitureTypeRepository = furnitureTypeRepository;
    }

    public FurnitureType createFurnitureType(FurnitureType furnitureType) {
        return furnitureTypeRepository.save(furnitureType);
    }

    public FurnitureType updateFurnitureType(FurnitureType furnitureType) {
        return furnitureTypeRepository.save(furnitureType);
    }

    public Optional<FurnitureType> findFurnitureTypeById(Long id) {
        return furnitureTypeRepository.findById(id);
    }

    public List<FurnitureType> findAllFurnitureTypes() {
        return furnitureTypeRepository.findAll();
    }

    public List<FurnitureType> findFurnitureTypesByCategory(String category) {
        return furnitureTypeRepository.findByCategory(category);
    }

    public List<FurnitureType> findFurnitureTypesByName(String name) {
        return furnitureTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<FurnitureType> findLowStockItems() {
        return furnitureTypeRepository.findLowStockItems();
    }

    public void deleteFurnitureType(Long id) {
        furnitureTypeRepository.deleteById(id);
    }

    public List<String> findAllCategories() {
        return furnitureTypeRepository.findDistinctCategories();
    }

    public void updateStockLevel(Long furnitureTypeId, Integer newStockLevel) {
        Optional<FurnitureType> furnitureTypeOpt = furnitureTypeRepository.findById(furnitureTypeId);
        if (furnitureTypeOpt.isPresent()) {
            FurnitureType furnitureType = furnitureTypeOpt.get();
            furnitureType.setStockLevel(newStockLevel);
            furnitureTypeRepository.save(furnitureType);
        }
    }

    public void decreaseStock(Long furnitureTypeId, Integer quantity) {
        Optional<FurnitureType> furnitureTypeOpt = furnitureTypeRepository.findById(furnitureTypeId);
        if (furnitureTypeOpt.isPresent()) {
            FurnitureType furnitureType = furnitureTypeOpt.get();
            int currentStock = furnitureType.getStockLevel();
            if (currentStock >= quantity) {
                furnitureType.setStockLevel(currentStock - quantity);
                furnitureTypeRepository.save(furnitureType);
            } else {
                throw new RuntimeException("Insufficient stock. Available: " + currentStock + ", Required: " + quantity);
            }
        }
    }

    public void increaseStock(Long furnitureTypeId, Integer quantity) {
        Optional<FurnitureType> furnitureTypeOpt = furnitureTypeRepository.findById(furnitureTypeId);
        if (furnitureTypeOpt.isPresent()) {
            FurnitureType furnitureType = furnitureTypeOpt.get();
            furnitureType.setStockLevel(furnitureType.getStockLevel() + quantity);
            furnitureTypeRepository.save(furnitureType);
        }
    }

    public boolean isLowStock(Long furnitureTypeId) {
        Optional<FurnitureType> furnitureTypeOpt = furnitureTypeRepository.findById(furnitureTypeId);
        if (furnitureTypeOpt.isPresent()) {
            FurnitureType furnitureType = furnitureTypeOpt.get();
            return furnitureType.getStockLevel() <= furnitureType.getMinStockLevel();
        }
        return false;
    }
}
