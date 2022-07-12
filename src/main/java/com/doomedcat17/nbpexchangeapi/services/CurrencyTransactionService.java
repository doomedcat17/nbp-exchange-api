package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.mapper.CurrencyTransactionMapper;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyRepository;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final CurrencyRepository currencyRepository;

    private final CurrencyTransactionMapper mapper;

    public void addTransaction(TransactionDto transactionDto) {
        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        Currency sellCurrency = currencyRepository.findById(transactionDto.getSellCode()).get();
        Currency buyCurrency = currencyRepository.findById(transactionDto.getBuyCode()).get();
        currencyTransaction.setSellCurrency(sellCurrency);
        currencyTransaction.setBuyCurrency(buyCurrency);
        currencyTransaction.setBoughtAmount(transactionDto.getBuyAmount());
        currencyTransaction.setSoldAmount(transactionDto.getSellAmount());
        currencyTransaction.setDate(transactionDto.getDate());
        currencyTransactionRepository.save(currencyTransaction);
    }

    public TransactionDto getLatestTransaction(){
        CurrencyTransaction currencyTransaction = currencyTransactionRepository.getTopByOrderByDateDesc();
        return mapper.toDto(currencyTransaction);
    }

    public List<TransactionDto> getAllByDate(LocalDate date) {
        List<CurrencyTransaction> currencyTransactions = currencyTransactionRepository
                .getAllByDateYearAndDateMonthAndDateDay(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        return currencyTransactions.stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<TransactionDto> getAllFromGivenDates(LocalDate startDate, LocalDate endDate) {
        List<CurrencyTransaction> currencyTransactions =
                currencyTransactionRepository.getAllBetweenDates(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));
        return currencyTransactions.stream()
                .map(mapper::toDto)
                .toList();
    }

}
