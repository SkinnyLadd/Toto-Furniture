package com.furnitureshop.repository;

import com.furnitureshop.model.entity.FurnitureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FurnitureTypeRepository extends JpaRepository<FurnitureType, Long> {

    List<FurnitureType> findByCategory(String category);

    List<FurnitureType> findByNameContainingIgnoreCase(String name);

    @Query("SELECT ft FROM FurnitureType ft WHERE ft.stockLevel <= ft.minStockLevel")
    List<FurnitureType> findLowStockItems();

    @Query("SELECT DISTINCT ft.category FROM FurnitureType ft")
    List<String> findDistinctCategories();

    @Query("SELECT ft FROM FurnitureType ft WHERE ft.stockLevel > 0")
    List<FurnitureType> findAvailableItems();

    @Query("SELECT ft FROM FurnitureType ft WHERE ft.stockLevel = 0")
    List<FurnitureType> findOutOfStockItems();
}
