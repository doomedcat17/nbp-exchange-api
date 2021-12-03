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

    public TransactionDto buyCurrency(String buyCurrencyCode, String sellCurrencyCode, BigDecimal buyAmount) {
        buyAmount = buyAmount.setScale(2, RoundingMode.HALF_EVEN);
        TransactionDto transaction = new TransactionDto();
        ExchangeRateDTO exchangeRateDTO = exchangeRatesService
                .getRecentExchangeRate(buyCurrencyCode, sellCurrencyCode);
        RateDTO rate = exchangeRateDTO.getRates().get(0);
        BigDecimal soldAmount = rate.getRate().multiply(buyAmount);
        soldAmount = soldAmount.setScale(2, RoundingMode.HALF_EVEN);
        transaction.setBuyCode(buyCurrencyCode);
        transaction.setSellCode(sellCurrencyCode);
        transaction.setSellAmount(soldAmount);
        transaction.setBuyAmount(buyAmount);
        transaction.setDate(new Date(System.currentTimeMillis()));
        transactionRepository.addTransaction(transaction);
        return transaction;
    }

    public TradeService(ExchangeRatesService exchangeRatesService, CurrencyTransactionRepository transactionRepository) {
        this.exchangeRatesService = exchangeRatesService;
        this.transactionRepository = transactionRepository;
    }
}
