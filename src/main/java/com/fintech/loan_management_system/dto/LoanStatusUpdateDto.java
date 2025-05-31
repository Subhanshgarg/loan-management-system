package com.fintech.loan_management_system.dto;

import com.fintech.loan_management_system.entity.Loan;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusUpdateDto {

    @NotNull(message = "Status is required")
    private Loan.LoanStatus status;

    private String remarks;
}
