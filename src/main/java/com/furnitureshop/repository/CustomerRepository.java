package com.furnitureshop.repository;

import com.furnitureshop.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByFullNameContainingIgnoreCase(String name);
    List<Customer> findByPhoneNumberContaining(String phoneNumber);
    List<Customer> findByEmailContainingIgnoreCase(String email);
    
    @Query("SELECT c FROM Customer c WHERE c.outstandingBalance > 0 ORDER BY c.outstandingBalance DESC")
    List<Customer> findCustomersWithOutstandingBalance();
}
