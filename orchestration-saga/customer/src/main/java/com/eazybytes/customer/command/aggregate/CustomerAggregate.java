package com.eazybytes.customer.command.aggregate;

import com.eazybytes.common.command.RollbackCustomerMobileNumberCommand;
import com.eazybytes.common.command.UpdateCustomerMobileNumberCommand;
import com.eazybytes.common.event.CustomerMobileNumberRollbackedEvent;
import com.eazybytes.common.event.CustomerMobileNumberUpdatedEvent;
import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
    private String errorMsg;

    public CustomerAggregate() {}

    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);
        AggregateLifecycle.apply(customerCreatedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        this.customerId = customerCreatedEvent.getCustomerId();
        this.name = customerCreatedEvent.getName();
        this.email= customerCreatedEvent.getEmail();
        this.mobileNumber = customerCreatedEvent.getMobileNumber();
        this.activeSw = customerCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateCustomerCommand updateCustomerCommand) {
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent();
        BeanUtils.copyProperties(updateCustomerCommand, customerUpdatedEvent);
        AggregateLifecycle.apply(customerUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        this.name = customerUpdatedEvent.getName();
        this.email= customerUpdatedEvent.getEmail();
    }

    @CommandHandler
    public void handle(DeleteCustomerCommand deleteCustomerCommand) {
        CustomerDeletedEvent customerDeletedEvent = new CustomerDeletedEvent();
        BeanUtils.copyProperties(deleteCustomerCommand, customerDeletedEvent);
        AggregateLifecycle.apply(customerDeletedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        this.activeSw = customerDeletedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(UpdateCustomerMobileNumberCommand updateCustomerMobileNumberCommand) {
        CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent = new CustomerMobileNumberUpdatedEvent();
        BeanUtils.copyProperties(updateCustomerMobileNumberCommand, customerMobileNumberUpdatedEvent);
        AggregateLifecycle.apply(customerMobileNumberUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent) {
        this.mobileNumber = customerMobileNumberUpdatedEvent.getNewMobileNumber();
    }

    @CommandHandler
    public void handle(RollbackCustomerMobileNumberCommand rollbackCustomerMobileNumberCommand) {
        CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent = new CustomerMobileNumberRollbackedEvent();
        BeanUtils.copyProperties(rollbackCustomerMobileNumberCommand, customerMobileNumberRollbackedEvent);
        AggregateLifecycle.apply(customerMobileNumberRollbackedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent) {
        this.mobileNumber = customerMobileNumberRollbackedEvent.getMobileNumber();
        this.errorMsg = customerMobileNumberRollbackedEvent.getErrorMsg();
    }
}
