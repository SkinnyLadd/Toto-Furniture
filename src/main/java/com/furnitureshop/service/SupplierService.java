package com.furnitureshop.service;

import com.furnitureshop.model.entity.Supplier;
import com.furnitureshop.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public List<Supplier> findAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Supplier> findActiveSuppliers() {
        return supplierRepository.findByActive(true);
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> findSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Supplier> findSuppliersByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    @Transactional
    public Supplier deactivateSupplier(Long id) {
        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.setActive(false);
            return supplierRepository.save(supplier);
        }
        return null;
    }
}
