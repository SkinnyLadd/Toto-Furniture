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

    @Column(nullable = false)
    private BigDecimal price; // Current selling price

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

    @Column(nullable = false)
    private Integer stockLevel; // Current stock level

    @Override
    public String toString(){
        return name + " (" + category + ") - " + basePrice.toString() + " PKR";
    }

    // Additional getter methods for compatibility
    public Integer getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(Integer stockLevel) {
        this.stockLevel = stockLevel;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}
