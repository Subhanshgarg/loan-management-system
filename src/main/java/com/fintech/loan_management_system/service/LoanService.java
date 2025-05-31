package com.fintech.loan_management_system.service;

import com.fintech.loan_management_system.dto.LoanApplicationDto;
import com.fintech.loan_management_system.dto.LoanResponseDto;
import com.fintech.loan_management_system.dto.LoanStatusUpdateDto;
import com.fintech.loan_management_system.entity.Customer;
import com.fintech.loan_management_system.entity.Loan;
import com.fintech.loan_management_system.entity.User;
import com.fintech.loan_management_system.exception.ResourceNotFoundException;
import com.fintech.loan_management_system.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerService customerService;

    /**
     * Customer applies for a loan.
     */
    @Transactional
    public LoanResponseDto applyForLoan(LoanApplicationDto dto, User user) {
        log.info("Processing loan application for user: {}", user.getUsername());

        Customer customer = customerService.findByUserId(user.getId());

        Loan loan = new Loan();
        loan.setLoanType(dto.getLoanType());
        loan.setAmount(dto.getAmount());
        loan.setRemarks(dto.getRemarks());
        loan.setCustomer(customer);
        loan.setStatus(Loan.LoanStatus.PENDING);

        loan = loanRepository.save(loan);

        log.info("Loan application submitted successfully with ID: {}", loan.getId());

        return mapToDto(loan);
    }

    /**
     * Admin updates loan status.
     */
    @Transactional
    public LoanResponseDto updateLoanStatus(Long loanId, LoanStatusUpdateDto dto, User admin) {
        log.info("Updating loan status for loan ID: {} by admin: {}", loanId, admin.getUsername());

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        loan.setStatus(dto.getStatus());
        loan.setRemarks(dto.getRemarks());
        loan.setProcessedDate(LocalDateTime.now());
        loan.setProcessedBy(admin);

        loan = loanRepository.save(loan);

        log.info("Loan status updated to '{}' for ID: {}", dto.getStatus(), loanId);

        return mapToDto(loan);
    }

    /**
     * Get all loans (for admin view).
     */
    @Transactional(readOnly = true)
    public List<LoanResponseDto> getAllLoans() {
        return loanRepository.findAllOrderByAppliedDateDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get loans of a specific customer.
     */
    @Transactional(readOnly = true)
    public List<LoanResponseDto> getCustomerLoans(User user) {
        Customer customer = customerService.findByUserId(user.getId());

        return loanRepository.findByCustomerIdOrderByAppliedDateDesc(customer.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Get full loan object by ID (internal use).
     */
    @Transactional(readOnly = true)
    public LoanResponseDto getLoanById(Long loanId) {
        Loan l=  loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));
        return mapToDto(l);
    }

    /**
     * Maps Loan entity to LoanResponseDto.
     */
    private LoanResponseDto mapToDto(Loan loan) {
        return LoanResponseDto.builder()
                .id(loan.getId())
                .loanType(loan.getLoanType())
                .amount(loan.getAmount())
                .interestRate(loan.getInterestRate())
                .status(loan.getStatus())
                .appliedDate(loan.getAppliedDate())
                .remarks(loan.getRemarks())
                .build();
    }
}
