package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.command.event.AccountDeletedEvent;
import com.eazybytes.accounts.command.event.AccountUpdatedEvent;
import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.exception.AccountAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl  implements IAccountsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private AccountsRepository accountsRepository;

    /**
     * @param accountCreatedEvent - AccountCreatedEvent Object
     */
    @Override
    public void createAccount(AccountCreatedEvent accountCreatedEvent) {
        Optional<Accounts> optionalAccounts= accountsRepository.findByMobileNumberAndActiveSw(accountCreatedEvent.getMobileNumber(), AccountsConstants.ACTIVE_SW);
        if (optionalAccounts.isPresent()){
            throw new AccountAlreadyExistsException("Account already registered with given mobileNumber "+ accountCreatedEvent.getMobileNumber());
        }
        Accounts accounts = createNewAccount(accountCreatedEvent.getAccountNumber(), accountCreatedEvent.getMobileNumber());
        accountsRepository.save(accounts);
    }

    /**
     * @param mobileNumber - String
     * @return the new account details
     */
    private Accounts createNewAccount(long accountNumber, String mobileNumber) {
        Accounts newAccount = new Accounts();
        newAccount.setMobileNumber(mobileNumber);
        newAccount.setAccountNumber(accountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setActiveSw(AccountsConstants.ACTIVE_SW);
        return newAccount;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public AccountsDto fetchAccount(String mobileNumber) {
        Accounts account = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber, AccountsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
        );
        return AccountsMapper.mapToAccountsDto(account, new AccountsDto());
    }

    /**
     * @param accountUpdatedEvent - AccountsDto Object
     */
    @Override
    public void updateAccount(AccountUpdatedEvent accountUpdatedEvent) {
        Accounts account = accountsRepository.findByMobileNumberAndActiveSw(accountUpdatedEvent.getMobileNumber(), AccountsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber", accountUpdatedEvent.getMobileNumber()));
        AccountsMapper.mapEventToAccount(accountUpdatedEvent, account);
        accountsRepository.save(account);
        LOGGER.info("account updated successfully");
    }

    /**
     * @param accountDeletedEvent - AccountDeletedEvent
     */
    @Override
    public void deleteAccount(AccountDeletedEvent accountDeletedEvent) {
        Accounts account = accountsRepository.findById(accountDeletedEvent.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountDeletedEvent.getAccountNumber().toString()));
        account.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
        accountsRepository.save(account);
    }
}
