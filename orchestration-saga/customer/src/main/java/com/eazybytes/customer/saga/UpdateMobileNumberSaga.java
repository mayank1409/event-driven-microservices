package com.eazybytes.customer.saga;

import com.eazybytes.common.command.*;
import com.eazybytes.common.event.*;
import com.eazybytes.customer.constants.CustomerConstants;
import com.eazybytes.customer.dto.ResponseDto;
import com.eazybytes.customer.query.FindCustomerQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class UpdateMobileNumberSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent) {
        log.info("Saga Event 1 [Start] : Received CustomerMobileNumberUpdatedEvent for customerId: {}", customerMobileNumberUpdatedEvent.getCustomerId());
        UpdateAccountMobileNumberCommand updateAccountMobileNumberCommand = UpdateAccountMobileNumberCommand.builder()
                .accountNumber(customerMobileNumberUpdatedEvent.getAccountNumber())
                .cardNumber(customerMobileNumberUpdatedEvent.getCardNumber())
                .loanNumber(customerMobileNumberUpdatedEvent.getLoanNumber())
                .customerId(customerMobileNumberUpdatedEvent.getCustomerId())
                .mobileNumber(customerMobileNumberUpdatedEvent.getMobileNumber())
                .newMobileNumber(customerMobileNumberUpdatedEvent.getNewMobileNumber()).build();

        commandGateway.send(updateAccountMobileNumberCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                RollbackCustomerMobileNumberCommand rollbackCusMobNumCommand = RollbackCustomerMobileNumberCommand.builder()
                        .customerId(customerMobileNumberUpdatedEvent.getCustomerId())
                        .mobileNumber(customerMobileNumberUpdatedEvent.getMobileNumber())
                        .newMobileNumber(customerMobileNumberUpdatedEvent.getNewMobileNumber())
                        .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                commandGateway.sendAndWait(rollbackCusMobNumCommand);
            }
        });
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent) {
        log.info("Saga Event 2 : Received AccountMobileNumberUpdatedEvent for accountNumber: {}", accountMobileNumberUpdatedEvent.getAccountNumber());
        UpdateCardMobileNumberCommand updateCardMobileNumberCommand = UpdateCardMobileNumberCommand.builder()
                .accountNumber(accountMobileNumberUpdatedEvent.getAccountNumber())
                .cardNumber(accountMobileNumberUpdatedEvent.getCardNumber())
                .loanNumber(accountMobileNumberUpdatedEvent.getLoanNumber())
                .customerId(accountMobileNumberUpdatedEvent.getCustomerId())
                .mobileNumber(accountMobileNumberUpdatedEvent.getMobileNumber())
                .newMobileNumber(accountMobileNumberUpdatedEvent.getNewMobileNumber()).build();

        commandGateway.send(updateCardMobileNumberCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                RollbackAccountMobileNumberCommand rollbackAccntMobNumCommand = RollbackAccountMobileNumberCommand.builder()
                        .accountNumber(accountMobileNumberUpdatedEvent.getAccountNumber())
                        .customerId(accountMobileNumberUpdatedEvent.getCustomerId())
                        .mobileNumber(accountMobileNumberUpdatedEvent.getMobileNumber())
                        .newMobileNumber(accountMobileNumberUpdatedEvent.getNewMobileNumber())
                        .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                commandGateway.sendAndWait(rollbackAccntMobNumCommand);
            }
        });
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileNumberUpdatedEvent cardMobileNumberUpdatedEvent) {
        log.info("Saga Event 3 : Received CardMobileNumberUpdatedEvent for cardNumber: {}", cardMobileNumberUpdatedEvent.getCardNumber());
        UpdateLoanMobileNumberCommand updateLoanMobileNumberCommand = UpdateLoanMobileNumberCommand.builder()
                .accountNumber(cardMobileNumberUpdatedEvent.getAccountNumber())
                .cardNumber(cardMobileNumberUpdatedEvent.getCardNumber())
                .loanNumber(cardMobileNumberUpdatedEvent.getLoanNumber())
                .customerId(cardMobileNumberUpdatedEvent.getCustomerId())
                .mobileNumber(cardMobileNumberUpdatedEvent.getMobileNumber())
                .newMobileNumber(cardMobileNumberUpdatedEvent.getNewMobileNumber()).build();

        commandGateway.send(updateLoanMobileNumberCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                RollbackCardMobileNumberCommand rollbackCardMobNumCommand = RollbackCardMobileNumberCommand.builder()
                        .cardNumber(cardMobileNumberUpdatedEvent.getCardNumber())
                        .accountNumber(cardMobileNumberUpdatedEvent.getAccountNumber())
                        .customerId(cardMobileNumberUpdatedEvent.getCustomerId())
                        .mobileNumber(cardMobileNumberUpdatedEvent.getMobileNumber())
                        .newMobileNumber(cardMobileNumberUpdatedEvent.getNewMobileNumber())
                        .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                commandGateway.sendAndWait(rollbackCardMobNumCommand);
            }
        });
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(LoanMobileNumberUpdatedEvent loanMobileNumberUpdatedEvent) {
        log.info("Saga Event 4 [END] : Received LoanMobileNumberUpdatedEvent for loanNumber: {}", loanMobileNumberUpdatedEvent.getLoanNumber());
        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true, new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MOBILE_UPD_SUCCESS_MESSAGE));
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileNumberRollbackedEvent cardMobileNumberRollbackedEvent) {
        log.info("Saga Compensation Event : Received CardMobileNumberRollbackedEvent for cardNumber: {}", cardMobileNumberRollbackedEvent.getCardNumber());
        RollbackAccountMobileNumberCommand rollbackAccountMobileNumberCommand = RollbackAccountMobileNumberCommand.builder()
                .accountNumber(cardMobileNumberRollbackedEvent.getAccountNumber())
                .customerId(cardMobileNumberRollbackedEvent.getCustomerId())
                .mobileNumber(cardMobileNumberRollbackedEvent.getMobileNumber())
                .newMobileNumber(cardMobileNumberRollbackedEvent.getNewMobileNumber())
                .errorMsg(cardMobileNumberRollbackedEvent.getErrorMsg()).build();
        commandGateway.send(rollbackAccountMobileNumberCommand);
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent) {
        log.info("Saga Compensation Event : Received AccountMobileNumberRollbackedEvent for accountNumber: {}", accountMobileNumberRollbackedEvent.getAccountNumber());
        RollbackCustomerMobileNumberCommand rollbackCustomerMobileNumberCommand = RollbackCustomerMobileNumberCommand.builder()
                .customerId(accountMobileNumberRollbackedEvent.getCustomerId())
                .mobileNumber(accountMobileNumberRollbackedEvent.getMobileNumber())
                .newMobileNumber(accountMobileNumberRollbackedEvent.getNewMobileNumber())
                .errorMsg(accountMobileNumberRollbackedEvent.getErrorMsg()).build();
        commandGateway.send(rollbackCustomerMobileNumberCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent) {
        log.info("Saga Compensation Event [END] : Received CustomerMobileNumberRollbackedEvent for customerId: {}", customerMobileNumberRollbackedEvent.getCustomerId());
        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true, new ResponseDto(CustomerConstants.STATUS_500, CustomerConstants.MOBILE_UPD_FAILURE_MESSAGE));
    }
}
