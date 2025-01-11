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
     *
     * @param loanUpdatedEvent - LoanUpdatedEvent Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateLoan(LoanUpdatedEvent loanUpdatedEvent);

    /**
     *
     * @param loanDeletedEvent - LoanDeletedEvent Object
     * @return boolean indicating if the delete of loan details is successful or not
     */
    boolean deleteLoan(LoanDeletedEvent loanDeletedEvent);
}
