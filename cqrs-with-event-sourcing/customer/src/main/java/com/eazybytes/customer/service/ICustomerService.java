package com.eazybytes.customer.service;

import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.dto.CustomerDto;

public interface ICustomerService {

    /**
     * @param customerCreatedEvent - Customer Object
     */
    void createCustomer(CustomerCreatedEvent customerCreatedEvent);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchCustomer(String mobileNumber);

    /**
     * @param customerUpdatedEvent - CustomerUpdatedEvent Object
     */
    void updateCustomer(CustomerUpdatedEvent customerUpdatedEvent);

    /**
     * @param customerId - Input Customer ID
     */
    void deleteCustomer(String customerId);

}
