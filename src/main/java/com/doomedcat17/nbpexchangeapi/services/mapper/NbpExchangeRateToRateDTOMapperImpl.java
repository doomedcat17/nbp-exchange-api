package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Component
public class NbpExchangeRateToRateDTOMapperImpl implements NbpExchangeRateToRateDTOMapper {

    private final NbpExchangeRateRepository nbpExchangeRateRepository;

    @Override
    public Set<RateDTO> mapToRates(Set<NbpExchangeRate> exchangesRateToMap, NbpExchangeRate baseExchangeRate) {
        Set<RateDTO> rates = new HashSet<>();
        exchangesRateToMap.forEach(nbpExchangeRate -> {
            if (!nbpExchangeRate.equals(baseExchangeRate)) {
                rates.add(mapToRate(nbpExchangeRate, baseExchangeRate));
            }
        });
        return rates;
    }

    @Override
    public RateDTO mapToRate(NbpExchangeRate exchangeRateToMap, NbpExchangeRate baseExchangeRate) {
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

    public NbpExchangeRateToRateDTOMapperImpl(NbpExchangeRateRepository nbpExchangeRateRepository) {
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
    }
}
