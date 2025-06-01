package com.furnitureshop.repository;

import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.model.entity.Customer;
import com.furnitureshop.model.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    List<SalesOrder> findByCustomer(Customer customer);

    List<SalesOrder> findByStatus(String status);

    List<SalesOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<SalesOrder> findByCustomerAndOrderDateBetween(Customer customer, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM SalesOrder s ORDER BY s.orderDate DESC")
    List<SalesOrder> findAllOrderByOrderDateDesc();

    @Query(value = "SELECT * FROM sales_orders ORDER BY order_date DESC LIMIT :limit", nativeQuery = true)
    List<SalesOrder> findTopNByOrderByOrderDateDesc(@Param("limit") int limit);

    @Query("SELECT SUM(s.totalAmount) FROM SalesOrder s WHERE s.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(s.totalAmount) FROM SalesOrder s")
    BigDecimal sumTotalAmount();

    @Query("SELECT COUNT(s) FROM SalesOrder s WHERE s.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT s FROM SalesOrder s WHERE s.totalAmount >= :minAmount")
    List<SalesOrder> findByTotalAmountGreaterThanEqual(@Param("minAmount") BigDecimal minAmount);

    @Query("SELECT DISTINCT s.status FROM SalesOrder s")
    List<String> findDistinctStatuses();

    @Query("SELECT FUNCTION('TO_CHAR', so.orderDate, 'Month') AS month, SUM(so.totalAmount) " +
            "FROM SalesOrder so " +
            "GROUP BY FUNCTION('TO_CHAR', so.orderDate, 'Month') " +
            "ORDER BY MIN(so.orderDate)")
    List<Object[]> findMonthlySalesData();

    @Query("SELECT DISTINCT so FROM SalesOrder so JOIN so.lineItems li WHERE li.inventoryItem = :inventoryItem")
    List<SalesOrder> findByInventoryItem(InventoryItem inventoryItem);
}
