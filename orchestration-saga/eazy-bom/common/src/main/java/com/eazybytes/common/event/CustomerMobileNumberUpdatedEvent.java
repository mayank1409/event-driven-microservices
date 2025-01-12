package com.eazybytes.common.event;

import lombok.Data;

@Data
public class CustomerMobileNumberUpdatedEvent {

    private String customerId;
    private Long accountNumber;
    private Long loanNumber;
    private Long cardNumber;
    private String mobileNumber;
    private String newMobileNumber;
}
