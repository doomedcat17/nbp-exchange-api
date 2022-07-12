package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;

import java.util.List;

public interface NbpExchangeRateToRateDTOMapper {

    List<RateDTO> mapToRates(List<NbpExchangeRate> exchangesRateToMap, NbpExchangeRate baseExchangeRate);

    RateDTO mapToRate(NbpExchangeRate exchangeRateToMap, NbpExchangeRate baseExchangeRate);
}
