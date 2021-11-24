package com.doomedcat17.nbpexchangeapi.data.nbp;

import com.doomedcat17.nbpexchangeapi.data.model.Currency;
import com.doomedcat17.nbpexchangeapi.data.model.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NbpRatesToExchangeRatesMapper {

    public List<ExchangeRate> mapList(List<NbpExchangeRate> nbpExchangeRates) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for(NbpExchangeRate nbpExchangeRate: nbpExchangeRates) {
            nbpExchangeRates.forEach(innerNbpExchangeRate ->{
                if (!innerNbpExchangeRate.equals(nbpExchangeRate)) {
                    exchangeRates.add(map(nbpExchangeRate, innerNbpExchangeRate));
                }
            });
        }
        return exchangeRates;
    }

    public ExchangeRate map(NbpExchangeRate sellRate, NbpExchangeRate buyRate) {
        ExchangeRate exchangeRate = new ExchangeRate();
        BigDecimal actualRate = sellRate.getMidRateInPLN().divide(buyRate.getMidRateInPLN());
        exchangeRate.setRate(actualRate);
        Currency sellCurrency = new Currency(sellRate.getCode(), sellRate.getName());
        Currency buyCurrency = new Currency(buyRate.getCode(), sellRate.getName());
        exchangeRate.setSell(sellCurrency);
        exchangeRate.setBuy(buyCurrency);
        LocalDate effectiveDate;
        if (sellRate.getEffectiveDate().isBefore(buyRate.getEffectiveDate()))
            effectiveDate = sellRate.getEffectiveDate();
        else effectiveDate = buyRate.getEffectiveDate();
        exchangeRate.setEffectiveDate(effectiveDate);
        return exchangeRate;
    }

    
}
