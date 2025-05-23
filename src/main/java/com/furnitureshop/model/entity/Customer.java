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
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private BigDecimal creditLimit;

    @Column
    private BigDecimal outstandingBalance;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column
    private String notes;
}
