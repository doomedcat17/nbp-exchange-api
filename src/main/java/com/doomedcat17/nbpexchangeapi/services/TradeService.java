package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyTransactionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class TradeService {

    private final ExchangeRatesService exchangeRatesService;

    private final CurrencyTransactionRepository transactionRepository;

    public TransactionDto buyCurrency(String buyCurrencyCode, String sellCurrencyCode, BigDecimal sellAmount) {
        sellAmount = sellAmount.setScale(2, RoundingMode.HALF_EVEN);
        TransactionDto transaction = new TransactionDto();
        ExchangeRateDTO exchangeRateDTO = exchangeRatesService
                .getRecentExchangeRate(sellCurrencyCode, buyCurrencyCode);
        RateDTO rate = exchangeRateDTO.getRates().get(0);
        BigDecimal boughtAmount = rate.getRate().multiply(sellAmount);
        boughtAmount = boughtAmount.setScale(2, RoundingMode.HALF_EVEN);
        transaction.setBuyCode(buyCurrencyCode);
        transaction.setSellCode(sellCurrencyCode);
        transaction.setSellAmount(sellAmount);
        transaction.setBuyAmount(boughtAmount);
        transaction.setDate(new Date(System.currentTimeMillis()));
        transactionRepository.addTransaction(transaction);
        return transaction;
    }

    public TradeService(ExchangeRatesService exchangeRatesService, CurrencyTransactionRepository transactionRepository) {
        this.exchangeRatesService = exchangeRatesService;
        this.transactionRepository = transactionRepository;
    }
}