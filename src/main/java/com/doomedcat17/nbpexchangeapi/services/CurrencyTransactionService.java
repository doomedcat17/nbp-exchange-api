package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional
@AllArgsConstructor
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final CurrencyService currencyService;

    public void addTransaction(TransactionDto transactionDto) {
        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        //TODO exception
        Currency sellCurrency = currencyService.getByCode(transactionDto.getSellCode()).get();
        Currency buyCurrency = currencyService.getByCode(transactionDto.getBuyCode()).get();
        currencyTransaction.setSellCurrency(sellCurrency);
        currencyTransaction.setBuyCurrency(buyCurrency);
        currencyTransaction.setBoughtAmount(transactionDto.getBuyAmount());
        currencyTransaction.setSoldAmount(transactionDto.getSellAmount());
        currencyTransaction.setDate(transactionDto.getDate());
        currencyTransactionRepository.save(currencyTransaction);
    }

    public Page<CurrencyTransaction> getAllFromGivenDates(LocalDate startDate, LocalDate endDate, int pageNum) {
        return currencyTransactionRepository
                .getAllBetweenDates(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX), PageRequest.of(pageNum - 1, 50));

    }

}
