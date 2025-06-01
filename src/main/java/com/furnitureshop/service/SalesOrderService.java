package com.furnitureshop.service;

import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.model.entity.Customer;
import com.furnitureshop.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    public List<SalesOrder> findAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public List<SalesOrder> findSalesOrdersByCustomer(Customer customer) {
        return salesOrderRepository.findByCustomer(customer);
    }

    public List<SalesOrder> findSalesOrdersByStatus(String status) {
        return salesOrderRepository.findByStatus(status);
    }

    public List<SalesOrder> findSalesOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.findByOrderDateBetween(startDate, endDate);
    }

    public Double calculateTotalSalesForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = salesOrderRepository.sumTotalAmountByDateRange(startDate, endDate);
        return total != null ? total.doubleValue() : 0.0;
    }

    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        salesOrder.setOrderDate(LocalDateTime.now());
        salesOrder.setCreatedDate(LocalDateTime.now());
        salesOrder.setUpdatedDate(LocalDateTime.now());
        return salesOrderRepository.save(salesOrder);
    }

    public SalesOrder updateSalesOrder(SalesOrder salesOrder) {
        salesOrder.setUpdatedDate(LocalDateTime.now());
        return salesOrderRepository.save(salesOrder);
    }

    public Optional<SalesOrder> findSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }

    public List<SalesOrder> findRecentSalesOrders(int limit) {
        return salesOrderRepository.findTopNByOrderByOrderDateDesc(limit);
    }

    public List<SalesOrder> findPendingSalesOrders() {
        return salesOrderRepository.findByStatus("Pending");
    }

    public BigDecimal calculateTotalRevenue() {
        return salesOrderRepository.sumTotalAmount();
    }

    public Long countSalesOrdersByStatus(String status) {
        return salesOrderRepository.countByStatus(status);
    }

    public List<SalesOrder> findSalesOrdersByCustomerAndDateRange(Customer customer, LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.findByCustomerAndOrderDateBetween(customer, startDate, endDate);
    }

    public void updateSalesOrderStatus(Long orderId, String status) {
        Optional<SalesOrder> order = salesOrderRepository.findById(orderId);
        if (order.isPresent()) {
            SalesOrder salesOrder = order.get();
            salesOrder.setStatus(status);
            salesOrder.setUpdatedDate(LocalDateTime.now());
            salesOrderRepository.save(salesOrder);
        }
    }

    public Map<String, Double> getMonthlySalesData() {
        List<Object[]> results = salesOrderRepository.findMonthlySalesData();
        Map<String, Double> salesData = new LinkedHashMap<>();

        for (Object[] result : results) {
            String month = ((String) result[0]).trim(); // Trim to remove extra spaces
            Double totalSales = ((BigDecimal) result[1]).doubleValue();
            salesData.put(month, totalSales);
        }

        return salesData;
    }

    public List<SalesOrder> findSalesOrdersByInventoryItem(InventoryItem item) {
        return salesOrderRepository.findByInventoryItem(item);
    }
}
