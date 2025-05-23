package com.furnitureshop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String city;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column
    private String paymentTerms;

    @Column
    private String notes;

    @Column(nullable = false)
    private boolean active = true;
}
