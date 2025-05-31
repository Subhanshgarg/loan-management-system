package com.fintech.loan_management_system.repository;

import com.fintech.loan_management_system.entity.Loan;
import com.fintech.loan_management_system.entity.Loan.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(Long customerId);
    List<Loan> findByStatus(LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.customer.id = :customerId ORDER BY l.appliedDate DESC")
    List<Loan> findByCustomerIdOrderByAppliedDateDesc(@Param("customerId") Long customerId);

    @Query("SELECT l FROM Loan l ORDER BY l.appliedDate DESC")
    List<Loan> findAllOrderByAppliedDateDesc();

    long countByStatus(LoanStatus status);
}
