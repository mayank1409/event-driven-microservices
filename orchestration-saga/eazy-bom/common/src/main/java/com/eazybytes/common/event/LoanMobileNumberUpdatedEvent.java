package com.eazybytes.common.event;

import lombok.Data;

@Data
public class LoanMobileNumberUpdatedEvent {

    private Long accountNumber;
    private Long loanNumber;
    private Long cardNumber;
    private String mobileNumber;
    private String newMobileNumber;
    private String customerId;
}
