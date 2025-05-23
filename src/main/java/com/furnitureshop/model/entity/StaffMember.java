package com.furnitureshop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff_members")
public class StaffMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column
    private LocalDate terminationDate;

    @Column(nullable = false)
    private BigDecimal hourlyRate;

    @Column
    private String emergencyContact;

    @Column
    private String emergencyPhone;

    @Column
    private String bankAccountDetails;

    @Column(nullable = false)
    private boolean active = true;
}
