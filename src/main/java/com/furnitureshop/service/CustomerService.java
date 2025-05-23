package com.furnitureshop.service;

import com.furnitureshop.model.entity.Customer;
import com.furnitureshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersByName(String name) {
        return customerRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumberContaining(phoneNumber);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersByEmail(String email) {
        return customerRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersWithOutstandingBalance() {
        return customerRepository.findCustomersWithOutstandingBalance();
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public void updateCustomerBalance(Long customerId, double amount) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setOutstandingBalance(customer.getOutstandingBalance().add(java.math.BigDecimal.valueOf(amount)));
            customerRepository.save(customer);
        }
    }
}
