package com.furnitureshop.repository;

import com.furnitureshop.model.entity.PurchaseOrder;
import com.furnitureshop.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    List<PurchaseOrder> findBySupplier(Supplier supplier);
    List<PurchaseOrder> findByStatus(String status);
    List<PurchaseOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<PurchaseOrder> findByExpectedDeliveryDateBeforeAndStatus(LocalDateTime date, String status);
}
