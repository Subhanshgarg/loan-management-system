package com.fintech.loan_management_system.controller;

import com.fintech.loan_management_system.dto.ApiResponse;
import com.fintech.loan_management_system.dto.CustomerRegistrationDto;
import com.fintech.loan_management_system.entity.Customer;
import com.fintech.loan_management_system.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Customer>> registerCustomer(@Valid @RequestBody CustomerRegistrationDto dto) {
        log.info("Customer registration request received for email: {}", dto.getEmail());

        Customer customer = customerService.registerCustomer(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully", customer));
    }
}
