package com.eazybytes.loans.service.impl;

import com.eazybytes.loans.command.event.LoanCreatedEvent;
import com.eazybytes.loans.command.event.LoanDeletedEvent;
import com.eazybytes.loans.command.event.LoanUpdatedEvent;
import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.exception.LoanAlreadyExistsException;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    /**
     * @param loanCreatedEvent - LoanCreatedEvent Object
     */
    @Override
    public void createLoan(LoanCreatedEvent loanCreatedEvent) {
        Optional<Loans> optionalLoan = loansRepository.findByMobileNumberAndActiveSw(loanCreatedEvent.getMobileNumber(), LoansConstants.ACTIVE_SW);
        if (optionalLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + loanCreatedEvent.getMobileNumber());
        }
        Loans loan = createNewLoan(loanCreatedEvent.getLoanNumber(), loanCreatedEvent.getMobileNumber());
        loansRepository.save(loan);
    }

    /**
     * @param loanNumber - Loan Number of the Customer
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(long loanNumber, String mobileNumber) {
        Loans newLoan = new Loans();
        newLoan.setLoanNumber(loanNumber);
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setActiveSw(LoansConstants.ACTIVE_SW);
        return newLoan;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loan = loansRepository.findByMobileNumberAndActiveSw(mobileNumber, LoansConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
        return LoansMapper.mapToLoansDto(loan, new LoansDto());
    }

    /**
     * @param loanUpdatedEvent - LoanUpdatedEvent Object
     * @return boolean indicating if the update of loan details is successful or not
     */
    @Override
    public boolean updateLoan(LoanUpdatedEvent loanUpdatedEvent) {
        Loans loan = loansRepository.findByMobileNumberAndActiveSw(loanUpdatedEvent.getMobileNumber(), LoansConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "LoanNumber", loanUpdatedEvent.getLoanNumber().toString()));
        LoansMapper.mapEventToLoan(loanUpdatedEvent, loan);
        loansRepository.save(loan);
        return true;
    }

    /**
     * @param loanDeletedEvent - LoanDeletedEvent Object
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(LoanDeletedEvent loanDeletedEvent) {
        Loans loan = loansRepository.findById(loanDeletedEvent.getLoanNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanDeletedEvent.getLoanNumber().toString()));
        loan.setActiveSw(LoansConstants.IN_ACTIVE_SW);
        loansRepository.save(loan);
        return true;
    }
}
