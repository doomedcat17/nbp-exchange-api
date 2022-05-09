package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDao;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyTransactionDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CurrencyTransactionRepository {

    private final CurrencyTransactionDao currencyTransactionDao;

    private final CurrencyDao currencyDAO;

    public CurrencyTransactionRepository(CurrencyTransactionDao currencyTransactionDao, CurrencyDao currencyDAO) {
        this.currencyTransactionDao = currencyTransactionDao;
        this.currencyDAO = currencyDAO;
    }

    public void addTransaction(TransactionDto transactionDto) {
        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        Currency sellCurrency = currencyDAO.findById(transactionDto.getSellCode()).get();
        Currency buyCurrency = currencyDAO.findById(transactionDto.getBuyCode()).get();
        currencyTransaction.setSellCurrency(sellCurrency);
        currencyTransaction.setBuyCurrency(buyCurrency);
        currencyTransaction.setBoughtAmount(transactionDto.getBuyAmount());
        currencyTransaction.setSoldAmount(transactionDto.getSellAmount());
        currencyTransaction.setDate(transactionDto.getDate());
        currencyTransactionDao.save(currencyTransaction);
    }

    public TransactionDto getLatestTransaction(){
        CurrencyTransaction currencyTransaction = currencyTransactionDao.getTopByOrderByDateDesc();
        return TransactionDto.applyCurrencyTransaction(currencyTransaction);
    }

    public List<TransactionDto> getAllByDate(LocalDate date) {
        List<CurrencyTransaction> currencyTransactions = currencyTransactionDao
                .getAllByDateYearAndDateMonthAndDateDay(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        return currencyTransactions.stream()
                .map(TransactionDto::applyCurrencyTransaction)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getAllFromGivenDates(LocalDate startDate, LocalDate endDate) {
        List<CurrencyTransaction> currencyTransactions =
                currencyTransactionDao.getAllBetweenDates(startDate, endDate);
        return currencyTransactions.stream()
                .map(TransactionDto::applyCurrencyTransaction)
                .collect(Collectors.toList());
    }

}
