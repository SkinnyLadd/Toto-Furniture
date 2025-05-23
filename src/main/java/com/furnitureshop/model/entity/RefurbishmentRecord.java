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
@Table(name = "refurbishment_records")
public class RefurbishmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column
    private LocalDateTime completionDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_staff_id")
    private StaffMember assignedStaff;

    @Column(nullable = false)
    private String status; // Scheduled, In Progress, Completed, Cancelled

    @Column
    private String notes;
}
