package com.furnitureshop.service;

import com.furnitureshop.model.entity.Customer;
import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.OrderLineItem;
import com.furnitureshop.model.entity.SalesOrder;
import com.furnitureshop.repository.InventoryItemRepository;
import com.furnitureshop.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final CustomerService customerService;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository, 
                            InventoryItemRepository inventoryItemRepository,
                            CustomerService customerService) {
        this.salesOrderRepository = salesOrderRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SalesOrder> findSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<SalesOrder> findSalesOrderByOrderNumber(String orderNumber) {
        return salesOrderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findSalesOrdersByCustomer(Customer customer) {
        return salesOrderRepository.findByCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findSalesOrdersByStatus(String status) {
        return salesOrderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findSalesOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return salesOrderRepository.findByOrderDateBetween(start, end);
    }

    @Transactional
    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        // Generate unique order number
        salesOrder.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Update inventory status for each line item
        for (OrderLineItem lineItem : salesOrder.getLineItems()) {
            InventoryItem item = lineItem.getInventoryItem();
            item.setStatus("Sold");
            inventoryItemRepository.save(item);
            
            // Set the sales order reference in the line item
            lineItem.setSalesOrder(salesOrder);
        }
        
        // Update customer's outstanding balance if applicable
        if (salesOrder.getStatus().equals("Delivered") && 
            (salesOrder.getPayments() == null || salesOrder.getPayments().isEmpty())) {
            customerService.updateCustomerBalance(salesOrder.getCustomer().getId(), 
                                                salesOrder.getFinalAmount().doubleValue());
        }
        
        return salesOrderRepository.save(salesOrder);
    }

    @Transactional
    public SalesOrder updateSalesOrder(SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }

    @Transactional
    public SalesOrder updateSalesOrderStatus(Long id, String newStatus) {
        Optional<SalesOrder> orderOpt = salesOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            SalesOrder order = orderOpt.get();
            order.setStatus(newStatus);
            return salesOrderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Double calculateTotalSalesForPeriod(LocalDateTime start, LocalDateTime end) {
        return salesOrderRepository.calculateTotalSalesForPeriod(start, end);
    }
}
