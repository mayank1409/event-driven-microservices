package com.eazybytes.accounts.command.aggregate;

import com.eazybytes.accounts.command.CreateAccountCommand;
import com.eazybytes.accounts.command.DeleteAccountCommand;
import com.eazybytes.accounts.command.UpdateAccountCommand;
import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.command.event.AccountDeletedEvent;
import com.eazybytes.accounts.command.event.AccountUpdatedEvent;
import com.eazybytes.common.command.RollbackAccountMobileNumberCommand;
import com.eazybytes.common.command.RollbackCustomerMobileNumberCommand;
import com.eazybytes.common.command.UpdateAccountMobileNumberCommand;
import com.eazybytes.common.event.AccountMobileNumberRollbackedEvent;
import com.eazybytes.common.event.AccountMobileNumberUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class AccountsAggregate {

    @AggregateIdentifier
    private Long accountNumber;
    private String mobileNumber;
    private String accountType;
    private String branchAddress;
    private boolean activeSw;
    private String errorMsg;

    public AccountsAggregate() {
    }

    @CommandHandler
    public AccountsAggregate(CreateAccountCommand createCommand) {
        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent();
        BeanUtils.copyProperties(createCommand, accountCreatedEvent);
        AggregateLifecycle.apply(accountCreatedEvent);
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        this.accountNumber = accountCreatedEvent.getAccountNumber();
        this.mobileNumber = accountCreatedEvent.getMobileNumber();
        this.accountType = accountCreatedEvent.getAccountType();
        this.branchAddress = accountCreatedEvent.getBranchAddress();
        this.activeSw = accountCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateAccountCommand updateCommand) {
        AccountUpdatedEvent accountUpdatedEvent = new AccountUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, accountUpdatedEvent);
        AggregateLifecycle.apply(accountUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        this.accountType = accountUpdatedEvent.getAccountType();
        this.branchAddress = accountUpdatedEvent.getBranchAddress();
    }

    @CommandHandler
    public void handle(DeleteAccountCommand deleteCommand) {
        AccountDeletedEvent accountDeletedEvent = new AccountDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, accountDeletedEvent);
        AggregateLifecycle.apply(accountDeletedEvent);
    }

    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        this.activeSw = accountDeletedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateAccountMobileNumberCommand updateAccntMobileNumCommand) {
        AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent = new AccountMobileNumberUpdatedEvent();
        BeanUtils.copyProperties(updateAccntMobileNumCommand, accountMobileNumberUpdatedEvent);
        AggregateLifecycle.apply(accountMobileNumberUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent) {
        this.mobileNumber = accountMobileNumberUpdatedEvent.getNewMobileNumber();
    }

    @CommandHandler
    public void handle(RollbackAccountMobileNumberCommand rollbackAccountMobileNumberCommand) {
        AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent = new AccountMobileNumberRollbackedEvent();
        BeanUtils.copyProperties(rollbackAccountMobileNumberCommand, accountMobileNumberRollbackedEvent);
        AggregateLifecycle.apply(accountMobileNumberRollbackedEvent);
    }

    @EventSourcingHandler
    public void on(AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent) {
        this.mobileNumber = accountMobileNumberRollbackedEvent.getMobileNumber();
        this.errorMsg = accountMobileNumberRollbackedEvent.getErrorMsg();
    }
}
