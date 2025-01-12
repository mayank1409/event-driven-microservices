package com.eazybytes.loans.service;

import com.eazybytes.loans.command.event.LoanCreatedEvent;
import com.eazybytes.loans.command.event.LoanDeletedEvent;
import com.eazybytes.loans.command.event.LoanUpdatedEvent;
import com.eazybytes.loans.dto.LoansDto;

public interface ILoansService {
    /**
     *
     * @param loanCreatedEvent - LoanCreatedEvent Object
     */
    void createLoan(LoanCreatedEvent loanCreatedEvent);

    /**
     *
     * @param mobileNumber - Input mobile Number
     *  @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);

    /**
     * @param loanUpdatedEvent - LoanUpdatedEvent Object
     */
    void updateLoan(LoanUpdatedEvent loanUpdatedEvent);

    /**
     * @param loanDeletedEvent - LoanDeletedEvent Object
     */
    void deleteLoan(LoanDeletedEvent loanDeletedEvent);

    /**
     * @param oldMobileNumber - Old mobile number of Loan
     * @param newMobileNumber - New mobile number of Loan
     */
    void updateMobileNumber(String oldMobileNumber, String newMobileNumber);
}
