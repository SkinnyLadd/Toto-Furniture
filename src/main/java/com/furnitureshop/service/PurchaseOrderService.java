package com.furnitureshop.service;

import com.furnitureshop.model.entity.FurnitureType;
import com.furnitureshop.model.entity.InventoryItem;
import com.furnitureshop.model.entity.PurchaseOrder;
import com.furnitureshop.model.entity.PurchaseOrderItem;
import com.furnitureshop.repository.FurnitureTypeRepository;
import com.furnitureshop.repository.InventoryItemRepository;
import com.furnitureshop.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final FurnitureTypeRepository furnitureTypeRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                               InventoryItemRepository inventoryItemRepository,
                               FurnitureTypeRepository furnitureTypeRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.furnitureTypeRepository = furnitureTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrder> findAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findPurchaseOrderByOrderNumber(String orderNumber) {
        return purchaseOrderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrder> findPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrder> findOverduePurchaseOrders() {
        return purchaseOrderRepository.findByExpectedDeliveryDateBeforeAndStatus(LocalDateTime.now(), "Sent");
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // Generate unique order number
        purchaseOrder.setOrderNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Set initial received quantity to 0 for all items
        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            item.setReceivedQuantity(0);
            item.setPurchaseOrder(purchaseOrder);
        }
        
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Transactional
    public PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Transactional
    public PurchaseOrder updatePurchaseOrderStatus(Long id, String newStatus) {
        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.setStatus(newStatus);
            return purchaseOrderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public void deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    @Transactional
    public PurchaseOrder receiveItems(Long purchaseOrderId, List<PurchaseOrderItem> receivedItems) {
        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            
            // Update received quantities
            for (PurchaseOrderItem receivedItem : receivedItems) {
                for (PurchaseOrderItem orderItem : order.getItems()) {
                    if (orderItem.getId().equals(receivedItem.getId())) {
                        int newReceivedQty = orderItem.getReceivedQuantity() + receivedItem.getReceivedQuantity();
                        orderItem.setReceivedQuantity(newReceivedQty);
                        
                        // Create inventory items for the received items
                        FurnitureType furnitureType = orderItem.getFurnitureType();
                        for (int i = 0; i < receivedItem.getReceivedQuantity(); i++) {
                            InventoryItem newItem = new InventoryItem();
                            newItem.setFurnitureType(furnitureType);
                            newItem.setSerialNumber(generateSerialNumber(furnitureType));
                            newItem.setCondition("New");
                            newItem.setLocation("Warehouse");
                            newItem.setPurchasePrice(orderItem.getUnitPrice());
                            newItem.setAcquisitionDate(LocalDateTime.now());
                            newItem.setStatus("Available");
                            inventoryItemRepository.save(newItem);
                        }
                        break;
                    }
                }
            }
            
            // Update order status based on received quantities
            boolean allReceived = true;
            boolean someReceived = false;
            
            for (PurchaseOrderItem item : order.getItems()) {
                if (item.getReceivedQuantity() < item.getQuantity()) {
                    allReceived = false;
                }
                if (item.getReceivedQuantity() > 0) {
                    someReceived = true;
                }
            }
            
            if (allReceived) {
                order.setStatus("Fully Received");
                order.setActualDeliveryDate(LocalDateTime.now());
            } else if (someReceived) {
                order.setStatus("Partially Received");
            }
            
            return purchaseOrderRepository.save(order);
        }
        return null;
    }
    
    private String generateSerialNumber(FurnitureType furnitureType) {
        // Generate a unique serial number based on furniture type and timestamp
        String prefix = furnitureType.getCategory().substring(0, 2).toUpperCase();
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        
        return prefix + "-" + timestamp + "-" + random;
    }
}
