package com.furnitureshop.repository;

import com.furnitureshop.model.entity.FurnitureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FurnitureTypeRepository extends JpaRepository<FurnitureType, Long> {
    List<FurnitureType> findByCategory(String category);
    
    @Query("SELECT ft FROM FurnitureType ft WHERE ft.id IN " +
           "(SELECT i.furnitureType.id FROM InventoryItem i GROUP BY i.furnitureType.id " +
           "HAVING COUNT(i) <= ft.minStockLevel)")
    List<FurnitureType> findLowStockItems();
}
