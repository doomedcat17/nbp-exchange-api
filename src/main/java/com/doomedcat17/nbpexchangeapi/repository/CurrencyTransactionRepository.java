package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDAO;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyTransactionDao;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyTransactionRepository {

    private final CurrencyTransactionDao currencyTransactionDao;

    private final CurrencyDAO currencyDAO;

    public void addTransaction(TransactionDto transactionDto) {
        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        Currency sellCurrency = currencyDAO.getById(transactionDto.getSellCode());
        Currency buyCurrency = currencyDAO.getById(transactionDto.getBuyCode());
        currencyTransaction.setSellCurrency(sellCurrency);
        currencyTransaction.setBuyCurrency(buyCurrency);
        currencyTransaction.setBoughtAmount(transactionDto.getBuyAmount());
        currencyTransaction.setSoldAmount(transactionDto.getSellAmount());
        currencyTransaction.setDate(transactionDto.getDate());
        currencyTransactionDao.save(currencyTransaction);
    }


    public CurrencyTransactionRepository(CurrencyTransactionDao currencyTransactionDao, CurrencyDAO currencyDAO) {
        this.currencyTransactionDao = currencyTransactionDao;
        this.currencyDAO = currencyDAO;
    }
}
