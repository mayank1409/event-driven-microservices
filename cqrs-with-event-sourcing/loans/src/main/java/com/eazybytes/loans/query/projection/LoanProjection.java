package com.eazybytes.loans.query.projection;

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

    private final ILoansService iLoansService;

    @EventHandler
    public void on(LoanCreatedEvent loanCreatedEvent) {
        iLoansService.createLoan(loanCreatedEvent);
    }

    @EventHandler
    public void on(LoanUpdatedEvent loanUpdatedEvent) {
        iLoansService.updateLoan(loanUpdatedEvent);
    }

    @EventHandler
    public void on(LoanDeletedEvent loanDeletedEvent) {
        iLoansService.deleteLoan(loanDeletedEvent);
    }
}
