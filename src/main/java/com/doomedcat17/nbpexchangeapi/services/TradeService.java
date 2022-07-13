package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.PageDto;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDto;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.mapper.CurrencyTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final ExchangeRateDtoService exchangeRateService;
    private final CurrencyTransactionService transactionRepository;
    private final CurrencyTransactionMapper mapper;


    public TransactionDto buyCurrency(String buyCurrencyCode, String sellCurrencyCode, BigDecimal buyAmount) {
        buyAmount = buyAmount.setScale(2, RoundingMode.HALF_EVEN);
        TransactionDto transaction = new TransactionDto();
        RateDto rate = exchangeRateService
                .getRecentExchangeRate(buyCurrencyCode, sellCurrencyCode).getResults().get(0);
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

    public PageDto<TransactionDto> getTransactionsFromGivenDates(LocalDate startDate, LocalDate endDate, int pageNumber) {
        PageDto<TransactionDto> pageDto = new PageDto<>();
        List<TransactionDto> transactionDtos = new ArrayList<>();
        pageDto.setResults(transactionDtos);
        if (Objects.isNull(startDate)) startDate = LocalDate.MIN;
        if (Objects.isNull(endDate)) endDate = LocalDate.MAX;
        Page<CurrencyTransaction> currencyTransactions = transactionRepository.getAllFromGivenDates(startDate, endDate, pageNumber);
        pageDto.setPage(pageNumber);
        pageDto.setTotalPages(currencyTransactions.getTotalPages());
        currencyTransactions.getContent().forEach(currencyTransaction -> transactionDtos.add(mapper.toDto(currencyTransaction)));
        return pageDto;
    }
}
