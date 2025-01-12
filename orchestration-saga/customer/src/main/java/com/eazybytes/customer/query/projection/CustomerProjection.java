package com.eazybytes.customer.query.projection;

import com.eazybytes.common.event.CustomerMobileNumberRollbackedEvent;
import com.eazybytes.common.event.CustomerMobileNumberUpdatedEvent;
import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class CustomerProjection {

    private final ICustomerService customerService;

    @EventHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        customerService.createCustomer(customerCreatedEvent);
    }

    @EventHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        customerService.updateCustomer(customerUpdatedEvent);
    }

    @EventHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        customerService.deleteCustomer(customerDeletedEvent.getCustomerId());
    }

    @EventHandler
    public void on(CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent) {
        customerService.updateMobileNumber(customerMobileNumberUpdatedEvent.getMobileNumber(), customerMobileNumberUpdatedEvent.getNewMobileNumber());
    }

    @EventHandler
    public void on(CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent) {
        customerService.updateMobileNumber(customerMobileNumberRollbackedEvent.getNewMobileNumber(), customerMobileNumberRollbackedEvent.getMobileNumber());
    }
}
