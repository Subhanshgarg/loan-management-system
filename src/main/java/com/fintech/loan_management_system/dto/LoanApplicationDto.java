package com.fintech.loan_management_system.dto;

import com.fintech.loan_management_system.entity.Loan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationDto {

    @NotNull(message = "Loan type is required")
    private Loan.LoanType loanType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1000.0", message = "Loan amount must be at least 1000")
    @DecimalMax(value = "10000000.0", message = "Loan amount cannot exceed 10,000,000")
    private BigDecimal amount;

    private String remarks;
}