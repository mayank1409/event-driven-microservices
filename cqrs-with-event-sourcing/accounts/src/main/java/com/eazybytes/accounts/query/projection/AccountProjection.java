package com.eazybytes.accounts.query.projection;

import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.command.event.AccountDeletedEvent;
import com.eazybytes.accounts.command.event.AccountUpdatedEvent;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("account-group")
public class AccountProjection {

    private final IAccountsService iAccountsService;

    @EventHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        iAccountsService.createAccount(accountCreatedEvent);
    }

    @EventHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        iAccountsService.updateAccount(accountUpdatedEvent);
    }

    @EventHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        iAccountsService.deleteAccount(accountDeletedEvent);
    }

}