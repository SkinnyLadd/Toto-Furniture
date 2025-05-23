package com.furnitureshop.repository;

import com.furnitureshop.model.entity.Customer;
import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    List<SalesOrder> findByCustomer(Customer customer);
    List<SalesOrder> findByCreatedBy(User createdBy);
    List<SalesOrder> findByStatus(String status);
    List<SalesOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(s.finalAmount) FROM SalesOrder s WHERE s.orderDate BETWEEN :start AND :end AND s.status = 'Delivered'")
    Double calculateTotalSalesForPeriod(LocalDateTime start, LocalDateTime end);
}
