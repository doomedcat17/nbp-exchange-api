package com.doomedcat17.nbpexchangeapi.data.dto;

import com.doomedcat17.nbpexchangeapi.data.CurrencyTransaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {

    private LocalDateTime date;
    private String buyCode;
    private BigDecimal buyAmount;
    private String sellCode;
    private BigDecimal sellAmount;

    public static TransactionDto applyCurrencyTransaction(CurrencyTransaction currencyTransaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(currencyTransaction.getDate());
        transactionDto.setBuyCode(currencyTransaction.getBuyCurrency().getCode());
        transactionDto.setSellCode(currencyTransaction.getSellCurrency().getCode());
        transactionDto.setBuyAmount(currencyTransaction.getBoughtAmount());
        transactionDto.setSellAmount(currencyTransaction.getSoldAmount());
        return transactionDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDto that = (TransactionDto) o;

        if (!date.equals(that.date)) return false;
        if (!buyCode.equals(that.buyCode)) return false;
        if (!buyAmount.equals(that.buyAmount)) return false;
        if (!sellCode.equals(that.sellCode)) return false;
        return sellAmount.equals(that.sellAmount);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + buyCode.hashCode();
        result = 31 * result + buyAmount.hashCode();
        result = 31 * result + sellCode.hashCode();
        result = 31 * result + sellAmount.hashCode();
        return result;
    }
}
