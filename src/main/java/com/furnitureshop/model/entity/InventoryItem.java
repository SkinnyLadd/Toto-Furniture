package com.furnitureshop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "furniture_type_id", nullable = false)
    private FurnitureType furnitureType;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private LocalDateTime acquisitionDate;

    @Column
    private LocalDateTime lastRefurbishmentDate;

    @Column(nullable = false)
    private String status; // Available, Sold, Reserved, Under Refurbishment

    @Column
    private String notes;
}
