package com.furnitureshop.repository;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findBySerialNumber(String serialNumber);
    List<InventoryItem> findByFurnitureTypeAndStatus(FurnitureType furnitureType, String status);
    List<InventoryItem> findByStatus(String status);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.furnitureType.id = :furnitureTypeId AND i.status = 'Available'")
    List<InventoryItem> findAvailableItemsByFurnitureTypeId(Long furnitureTypeId);
    
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.furnitureType.id = :furnitureTypeId AND i.status = 'Available'")
    Long countAvailableItemsByFurnitureTypeId(Long furnitureTypeId);
}
