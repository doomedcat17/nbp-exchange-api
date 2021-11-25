package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Service
public class ExchangeRatesService {

    private NbpExchangeRateRepository nbpExchangeRateRepository;

    public ExchangeRateDTO getMostRecentRates(String code) {
        Set<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getMostRecent();
        NbpExchangeRate baseExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(code);
        Set<RateDTO> rates = new HashSet<>();
        exchangeRates.forEach(nbpExchangeRate -> {
            if (!nbpExchangeRate.equals(baseExchangeRate)) {
                rates.add(mapToRate(nbpExchangeRate, baseExchangeRate));
            }
        });
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    private RateDTO mapToRate(NbpExchangeRate exchangeRateToMap, NbpExchangeRate baseExchangeRate) {
        NbpExchangeRate sourceExchangeRate = baseExchangeRate;
        if (!exchangeRateToMap.getEffectiveDate().equals(baseExchangeRate.getEffectiveDate())) {
            sourceExchangeRate = nbpExchangeRateRepository
                    .getByCodeAndEffectiveDate(
                            baseExchangeRate.getCurrency().getCode(),
                            exchangeRateToMap.getEffectiveDate()
                    );
        }
        BigDecimal rate = sourceExchangeRate.getMidRateInPLN()
                .divide(exchangeRateToMap.getMidRateInPLN(), RoundingMode.HALF_EVEN);
        RateDTO rateDTO = new RateDTO();
        rateDTO.setCode(exchangeRateToMap.getCurrency().getCode());
        rateDTO.setEffectiveDate(exchangeRateToMap.getEffectiveDate());
        rateDTO.setRate(rate);
        return rateDTO;
    }

    public ExchangeRatesService(NbpExchangeRateRepository nbpExchangeRateRepository) {
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
    }
}
