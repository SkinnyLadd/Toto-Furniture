package com.furnitureshop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payroll_records")
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffMember staffMember;

    @Column(nullable = false)
    private LocalDate payPeriodStart;

    @Column(nullable = false)
    private LocalDate payPeriodEnd;

    @Column(nullable = false)
    private BigDecimal hoursWorked;

    @Column(nullable = false)
    private BigDecimal hourlyRate;

    @Column(nullable = false)
    private BigDecimal grossAmount;

    @Column(nullable = false)
    private BigDecimal taxDeductions;

    @Column(nullable = false)
    private BigDecimal otherDeductions;

    @Column(nullable = false)
    private BigDecimal netAmount;

    @Column(nullable = false)
    private LocalDateTime processedDate;

    @Column(nullable = false)
    private String status; // Pending, Processed, Paid

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column
    private String notes;
}
