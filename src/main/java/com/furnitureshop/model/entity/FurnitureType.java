package com.furnitureshop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "furniture_types")
public class FurnitureType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column
    private String dimensions;

    @Column
    private String material;

    @Column
    private String color;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Integer minStockLevel;

    @Column(nullable = false)
    private Integer reorderQuantity;
}
