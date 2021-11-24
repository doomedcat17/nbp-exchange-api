package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.model.ExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.ExchangeRateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExchangeRatesRepository {

    private final ExchangeRateDao exchangeRateDao;

    public void saveExchangeRate(ExchangeRate exchangeRate) {
        ExchangeRate presentExchangeRate = exchangeRateDao.getByBuyAndSellAndEffectiveDate(
                exchangeRate.getBuy(),
                exchangeRate.getSell(),
                exchangeRate.getEffectiveDate()
        );
        if (presentExchangeRate != null) {
            presentExchangeRate.setRate(exchangeRate.getRate());
        } else exchangeRateDao.save(exchangeRate);
    }

    public void saveExchangeRates(List<ExchangeRate> exchangeRates) {
        exchangeRates.forEach(this::saveExchangeRate);
    }




    public ExchangeRatesRepository(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }
}
