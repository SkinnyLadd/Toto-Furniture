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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod; // Cash, Credit Card, Bank Transfer

    @Column
    private String referenceNumber;

    @Column(nullable = false)
    private String status; // Completed, Pending, Failed, Refunded

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    @Column
    private String notes;
}
