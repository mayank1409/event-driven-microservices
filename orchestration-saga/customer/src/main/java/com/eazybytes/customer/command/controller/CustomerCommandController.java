package com.eazybytes.customer.command.controller;

import com.eazybytes.common.command.UpdateCustomerMobileNumberCommand;
import com.eazybytes.common.dto.MobileNumberUpdateDto;
import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.constants.CustomerConstants;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.dto.ResponseDto;
import com.eazybytes.customer.query.FindCustomerQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CreateCustomerCommand createCustomerCommand = CreateCustomerCommand.builder()
                .customerId(UUID.randomUUID().toString()).email(customerDto.getEmail())
                .name(customerDto.getName()).mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(createCustomerCommand);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomerDetails(@Valid @RequestBody CustomerDto customerDto) {
        UpdateCustomerCommand updateCustomerCommand = UpdateCustomerCommand.builder()
                .customerId(customerDto.getCustomerId()).email(customerDto.getEmail())
                .name(customerDto.getName()).mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(updateCustomerCommand);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam("customerId") @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
            message = "CustomerId is invalid") String customerId) {
        DeleteCustomerCommand deleteCustomerCommand = DeleteCustomerCommand.builder()
                .customerId(customerId).activeSw(CustomerConstants.IN_ACTIVE_SW).build();
        commandGateway.sendAndWait(deleteCustomerCommand);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

    @PatchMapping("/mobile-number")
    public ResponseEntity<ResponseDto> updateMobileNumber(@Valid @RequestBody MobileNumberUpdateDto mobileNumberUpdateDto) {
        UpdateCustomerMobileNumberCommand updateCusMobNumCommand = UpdateCustomerMobileNumberCommand.builder()
                .customerId(mobileNumberUpdateDto.getCustomerId())
                .accountNumber(mobileNumberUpdateDto.getAccountNumber())
                .loanNumber(mobileNumberUpdateDto.getLoanNumber())
                .cardNumber(mobileNumberUpdateDto.getCardNumber())
                .mobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber())
                .newMobileNumber(mobileNumberUpdateDto.getNewMobileNumber()).build();

        try (SubscriptionQueryResult<ResponseDto, ResponseDto> queryResult = queryGateway.subscriptionQuery(
                new FindCustomerQuery(mobileNumberUpdateDto.getNewMobileNumber()),
                ResponseTypes.instanceOf(ResponseDto.class),
                ResponseTypes.instanceOf(ResponseDto.class))) {

            commandGateway.send(updateCusMobNumCommand, (commandMessage, commandResultMessage) -> {
                if (commandResultMessage.isExceptional()) {
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), commandResultMessage.exceptionResult().getMessage()));
                }
            });

            return ResponseEntity.status(HttpStatus.OK)
                    .body(queryResult.updates().blockFirst());
        }
    }
}
