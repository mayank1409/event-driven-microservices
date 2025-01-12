package com.eazybytes.profile.query.projection;

import com.eazybytes.common.event.AccountDataChangedEvent;
import com.eazybytes.common.event.CardDataChangedEvent;
import com.eazybytes.common.event.CustomerDataChangedEvent;
import com.eazybytes.common.event.LoanDataChangedEvent;
import com.eazybytes.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class ProfileProjection {

    private final IProfileService IProfileService;

    @EventHandler
    public void on(CustomerDataChangedEvent customerDataChangedEvent) {
        IProfileService.handleCustomerDataChangedEvent(customerDataChangedEvent);
    }

    @EventHandler
    public void on(AccountDataChangedEvent accountDataChangedEvent) {
        IProfileService.handleAccountDataChangedEvent(accountDataChangedEvent);
    }

    @EventHandler
    public void on(CardDataChangedEvent cardDataChangedEvent) {
        IProfileService.handleCardDataChangedEvent(cardDataChangedEvent);
    }

    @EventHandler
    public void on(LoanDataChangedEvent loanDataChangedEvent) {
        IProfileService.handleLoanDataChangedEvent(loanDataChangedEvent);
    }
}
