package com.furnitureshop.service;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.repository.FurnitureTypeRepository;
import com.furnitureshop.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final FurnitureTypeRepository furnitureTypeRepository;

    @Autowired
    public InventoryService(InventoryItemRepository inventoryItemRepository, FurnitureTypeRepository furnitureTypeRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.furnitureTypeRepository = furnitureTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryItem> findAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<InventoryItem> findInventoryItemById(Long id) {
        return inventoryItemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<InventoryItem> findInventoryItemBySerialNumber(String serialNumber) {
        return inventoryItemRepository.findBySerialNumber(serialNumber);
    }

    @Transactional(readOnly = true)
    public List<InventoryItem> findInventoryItemsByStatus(String status) {
        return inventoryItemRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<InventoryItem> findAvailableItemsByFurnitureTypeId(Long furnitureTypeId) {
        return inventoryItemRepository.findAvailableItemsByFurnitureTypeId(furnitureTypeId);
    }

    @Transactional(readOnly = true)
    public Long countAvailableItemsByFurnitureTypeId(Long furnitureTypeId) {
        return inventoryItemRepository.countAvailableItemsByFurnitureTypeId(furnitureTypeId);
    }

    @Transactional
    public InventoryItem createInventoryItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.save(inventoryItem);
    }

    @Transactional
    public InventoryItem updateInventoryItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.save(inventoryItem);
    }

//    @Transactional
//    public void deleteInventoryItem(Long id) {
//    }

    @Transactional
    public void updateInventoryItemStatus(Long id, String newStatus) {
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(id);
        if (itemOpt.isPresent()) {
            InventoryItem item = itemOpt.get();
            item.setStatus(newStatus);
            inventoryItemRepository.save(item);
        }
    }
}
