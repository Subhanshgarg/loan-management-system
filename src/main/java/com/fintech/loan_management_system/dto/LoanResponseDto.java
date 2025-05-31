package com.fintech.loan_management_system.dto;

import com.fintech.loan_management_system.entity.Loan;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LoanResponseDto {
    private Long id;
    private Loan.LoanType loanType;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Loan.LoanStatus status;
    private LocalDateTime appliedDate;
    private String remarks;
}
