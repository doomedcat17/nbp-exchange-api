package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeService {

    private final ExchangeRateDtoService exchangeRateDtoService;

    private final CurrencyTransactionService transactionRepository;

    public TradeService(ExchangeRateDtoService exchangeRateDtoService, CurrencyTransactionService transactionRepository) {
        this.exchangeRateDtoService = exchangeRateDtoService;
        this.transactionRepository = transactionRepository;
    }

    public TransactionDto buyCurrency(String buyCurrencyCode, String sellCurrencyCode, BigDecimal buyAmount) {
        buyAmount = buyAmount.setScale(2, RoundingMode.HALF_EVEN);
        TransactionDto transaction = new TransactionDto();
        ExchangeRateDTO foundExchangeRateDTO = exchangeRateDtoService
                .getRecentExchangeRate(buyCurrencyCode, sellCurrencyCode);
        RateDTO rate = foundExchangeRateDTO
                .getRates().get(0);
        BigDecimal soldAmount = rate.getRate().multiply(buyAmount);
        soldAmount = soldAmount.setScale(2, RoundingMode.HALF_EVEN);
        transaction.setBuyCode(buyCurrencyCode);
        transaction.setSellCode(sellCurrencyCode);
        transaction.setSellAmount(soldAmount);
        transaction.setBuyAmount(buyAmount);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.addTransaction(transaction);
        return transaction;
    }

    public List<TransactionDto> getTransactionsFromGivenDate(String date) {
        LocalDate transactionDate = LocalDate.parse(date);
        return transactionRepository.getAllByDate(transactionDate);
    }

    public List<TransactionDto> getTransactionsFromGivenDates(String startDate, String endDate) {
        LocalDate transactionStartDate = LocalDate.parse(startDate);
        LocalDate transactionEndDate= LocalDate.parse(endDate);
        return transactionRepository.getAllFromGivenDates(transactionStartDate, transactionEndDate);
    }
}
