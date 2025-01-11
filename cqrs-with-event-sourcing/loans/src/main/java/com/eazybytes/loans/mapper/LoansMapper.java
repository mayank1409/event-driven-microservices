package com.eazybytes.loans.mapper;

import com.eazybytes.loans.command.event.LoanUpdatedEvent;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;

public class LoansMapper {

    public static LoansDto mapToLoansDto(Loans loans, LoansDto loansDto) {
        loansDto.setLoanNumber(loans.getLoanNumber());
        loansDto.setLoanType(loans.getLoanType());
        loansDto.setMobileNumber(loans.getMobileNumber());
        loansDto.setTotalLoan(loans.getTotalLoan());
        loansDto.setAmountPaid(loans.getAmountPaid());
        loansDto.setOutstandingAmount(loans.getOutstandingAmount());
        loansDto.setActiveSw(loans.isActiveSw());
        return loansDto;
    }

    public static void mapEventToLoan(LoanUpdatedEvent loanUpdatedEvent, Loans loan) {
        loan.setLoanType(loanUpdatedEvent.getLoanType());
        loan.setTotalLoan(loanUpdatedEvent.getTotalLoan());
        loan.setAmountPaid(loanUpdatedEvent.getAmountPaid());
        loan.setOutstandingAmount(loanUpdatedEvent.getOutstandingAmount());
    }
}
