package com.doomedcat17.nbpexchangeapi.data.nbp;

import com.doomedcat17.nbpexchangeapi.data.model.Currency;
import com.doomedcat17.nbpexchangeapi.data.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                exchangeRates.addAll(providePLNExchangeRates(nbpExchangeRate));
            });
        }
        return exchangeRates;
    }

    private List<ExchangeRate> providePLNExchangeRates(NbpExchangeRate nbpExchangeRate) {
        List<ExchangeRate> plnExchangeRates = new ArrayList<>();
        NbpExchangeRate plnNbpExchangeRate = new NbpExchangeRate();
        plnNbpExchangeRate.setCode("PLN");
        plnNbpExchangeRate.setName("polski złoty");
        plnNbpExchangeRate.setMidRateInPLN(new BigDecimal(1));
        plnExchangeRates.add(map(nbpExchangeRate, plnNbpExchangeRate));
        plnExchangeRates.add(map(plnNbpExchangeRate, nbpExchangeRate));
        return plnExchangeRates;
    }

    public ExchangeRate map(NbpExchangeRate sellRate, NbpExchangeRate buyRate) {
        ExchangeRate exchangeRate = new ExchangeRate();
        BigDecimal actualRate = sellRate.getMidRateInPLN().divide(buyRate.getMidRateInPLN(), RoundingMode.HALF_EVEN);
        exchangeRate.setRate(actualRate);
        Currency sellCurrency = new Currency(sellRate.getCode(), sellRate.getName());
        Currency buyCurrency = new Currency(buyRate.getCode(), buyRate.getName());
        exchangeRate.setSell(sellCurrency);
        exchangeRate.setBuy(buyCurrency);
        exchangeRate.setEffectiveDate(
                getEffectiveDate(buyRate.getEffectiveDate(), sellRate.getEffectiveDate())
        );
        return exchangeRate;
    }

    private LocalDate getEffectiveDate(LocalDate buyExchangeRateDate, LocalDate sellExchangeRateDate) {
        if (sellExchangeRateDate != null) {
            if (buyExchangeRateDate != null) {
                if (sellExchangeRateDate.isBefore(buyExchangeRateDate))
                    return sellExchangeRateDate;
                else return buyExchangeRateDate;
            } return sellExchangeRateDate;
        } else if (buyExchangeRateDate != null) return buyExchangeRateDate;
        else return LocalDate.now();
    }

    
}
