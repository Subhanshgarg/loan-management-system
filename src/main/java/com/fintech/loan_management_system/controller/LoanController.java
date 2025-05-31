package com.fintech.loan_management_system.controller;

import com.fintech.loan_management_system.dto.ApiResponse;
import com.fintech.loan_management_system.dto.LoanApplicationDto;
import com.fintech.loan_management_system.dto.LoanResponseDto;
import com.fintech.loan_management_system.dto.LoanStatusUpdateDto;
import com.fintech.loan_management_system.entity.User;
import com.fintech.loan_management_system.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<LoanResponseDto>> applyForLoan(
            @Valid @RequestBody LoanApplicationDto dto,
            @AuthenticationPrincipal User user) {

        log.info("Loan application received from user: {}", user.getUsername());
        LoanResponseDto responseDto = loanService.applyForLoan(dto, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Loan application submitted successfully", responseDto));
    }

    @GetMapping("/my-loans")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<LoanResponseDto>>> getMyLoans(@AuthenticationPrincipal User user) {
        log.info("Fetching loans for user: {}", user.getUsername());
        List<LoanResponseDto> loans = loanService.getCustomerLoans(user);

        return ResponseEntity.ok(ApiResponse.success("Loans retrieved successfully", loans));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanResponseDto>>> getAllLoans() {
        log.info("Admin requested all loan applications");
        List<LoanResponseDto> loans = loanService.getAllLoans();

        return ResponseEntity.ok(ApiResponse.success("All loans retrieved successfully", loans));
    }

    @PutMapping("/{loanId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanResponseDto>> updateLoanStatus(
            @PathVariable Long loanId,
            @Valid @RequestBody LoanStatusUpdateDto dto,
            @AuthenticationPrincipal User admin) {

        log.info("Updating loan status for ID: {} by admin: {}", loanId, admin.getUsername());
        LoanResponseDto updatedLoan = loanService.updateLoanStatus(loanId, dto, admin);

        return ResponseEntity.ok(ApiResponse.success("Loan status updated successfully", updatedLoan));
    }

    @GetMapping("/{loanId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanResponseDto>> getLoanById(@PathVariable Long loanId) {
        log.info("Fetching loan details for ID: {}", loanId);
        LoanResponseDto loan = loanService.getLoanById(loanId);

        return ResponseEntity.ok(ApiResponse.success("Loan details retrieved successfully", loan));
    }
}
