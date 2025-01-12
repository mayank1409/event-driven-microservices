package com.eazybytes.common.event;

import lombok.Data;

@Data
public class CustomerMobileNumberRollbackedEvent {

    private String customerId;
    private String mobileNumber;
    private String newMobileNumber;
    private String errorMsg;
}