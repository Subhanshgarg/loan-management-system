package com.fintech.loan_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1000.0", message = "Loan amount must be at least 1000")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.PENDING;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @PrePersist
    protected void onCreate() {
        appliedDate = LocalDateTime.now();
        // Set default interest rates based on loan type
        if (interestRate == null) {
            setDefaultInterestRate();
        }
    }

    private void setDefaultInterestRate() {
        switch (loanType) {
            case PERSONAL:
                interestRate = new BigDecimal("12.50");
                break;
            case HOME:
                interestRate = new BigDecimal("8.75");
                break;
            case CAR:
                interestRate = new BigDecimal("10.25");
                break;
            default:
                interestRate = new BigDecimal("15.00");
        }
    }

    public enum LoanType {
        PERSONAL("Personal Loan"),
        HOME("Home Loan"),
        CAR("Car Loan");

        private final String displayName;

        LoanType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum LoanStatus {
        PENDING("Pending Review"),
        APPROVED("Approved"),
        REJECTED("Rejected");

        private final String displayName;

        LoanStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}