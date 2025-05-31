package com.fintech.loan_management_system.service;

import com.fintech.loan_management_system.dto.CustomerRegistrationDto;
import com.fintech.loan_management_system.entity.Customer;
import com.fintech.loan_management_system.entity.User;
import com.fintech.loan_management_system.exception.ResourceAlreadyExistsException;
import com.fintech.loan_management_system.repository.CustomerRepository;
import com.fintech.loan_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Customer registerCustomer(CustomerRegistrationDto dto) {
        log.info("Registering new customer with email: {}", dto.getEmail());

        // Check if email already exists
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Customer with email " + dto.getEmail() + " already exists");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username " + dto.getUsername() + " already exists");
        }

        // Create User entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(User.Role.CUSTOMER);
        user = userRepository.save(user);

        // Create Customer entity
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setUser(user);

        customer = customerRepository.save(customer);
        log.info("Customer registered successfully with ID: {}", customer.getId());

        return customer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
    }
}
