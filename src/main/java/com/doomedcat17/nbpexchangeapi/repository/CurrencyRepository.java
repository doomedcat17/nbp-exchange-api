package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.model.Currency;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CurrencyRepository {

    private final CurrencyDao currencyDao;


    public void addCurrency(Currency currency) {
        Optional<Currency> presentCurrency = currencyDao
                .findById(currency.getCode());
        if (presentCurrency.isEmpty()) currencyDao.save(currency);
    }

    public CurrencyRepository(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }
}
