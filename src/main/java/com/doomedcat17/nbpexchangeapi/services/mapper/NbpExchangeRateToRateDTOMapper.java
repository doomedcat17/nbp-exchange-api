package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;

import java.util.Set;

public interface NbpExchangeRateToRateDTOMapper {

    Set<RateDTO> mapToRates(Set<NbpExchangeRate> exchangesRateToMap, NbpExchangeRate baseExchangeRate);

    RateDTO mapToRate(NbpExchangeRate exchangeRateToMap, NbpExchangeRate baseExchangeRate);
}
