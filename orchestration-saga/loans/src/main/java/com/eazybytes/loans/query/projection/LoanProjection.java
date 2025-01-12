package com.eazybytes.loans.query.projection;

import com.eazybytes.common.event.LoanMobileNumberUpdatedEvent;
import com.eazybytes.loans.command.event.LoanCreatedEvent;
import com.eazybytes.loans.command.event.LoanDeletedEvent;
import com.eazybytes.loans.command.event.LoanUpdatedEvent;
import com.eazybytes.loans.service.ILoansService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("loan-group")
public class LoanProjection {

    private final ILoansService loansService;

    @EventHandler
    public void on(LoanCreatedEvent loanCreatedEvent) {
        loansService.createLoan(loanCreatedEvent);
    }

    @EventHandler
    public void on(LoanUpdatedEvent loanUpdatedEvent) {
        loansService.updateLoan(loanUpdatedEvent);
    }

    @EventHandler
    public void on(LoanDeletedEvent loanDeletedEvent) {
        loansService.deleteLoan(loanDeletedEvent);
    }

    @EventHandler
    public void on(LoanMobileNumberUpdatedEvent loanMobileNumberUpdatedEvent) {
        loansService.updateMobileNumber(loanMobileNumberUpdatedEvent.getMobileNumber(), loanMobileNumberUpdatedEvent.getNewMobileNumber());
    }
}
