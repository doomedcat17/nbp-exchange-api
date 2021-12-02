package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class NbpExchangeRateToRateDTOMapperImpl implements NbpExchangeRateToRateDTOMapper {

    @Override
    public List<RateDTO> mapToRates(List<NbpExchangeRate> exchangesRateToMap, NbpExchangeRate baseExchangeRate) {
        List<RateDTO> rates = new ArrayList<>();
        exchangesRateToMap.forEach(nbpExchangeRate -> {
            if (!nbpExchangeRate.equals(baseExchangeRate)) {
                rates.add(mapToRate(nbpExchangeRate, baseExchangeRate));
            }
        });
        return rates;
    }

    @Override
    public RateDTO mapToRate(NbpExchangeRate exchangeRateToMap, NbpExchangeRate baseExchangeRate) {
        if (!exchangeRateToMap.getEffectiveDate().equals(baseExchangeRate.getEffectiveDate()))
            throw new IllegalArgumentException();
        BigDecimal rate = baseExchangeRate.getMidRateInPLN()
                .divide(exchangeRateToMap.getMidRateInPLN(), RoundingMode.HALF_EVEN);
        RateDTO rateDTO = new RateDTO();
        rateDTO.setCode(exchangeRateToMap.getCurrency().getCode());
        rateDTO.setEffectiveDate(exchangeRateToMap.getEffectiveDate());
        rateDTO.setRate(rate);
        return rateDTO;
    }

}
