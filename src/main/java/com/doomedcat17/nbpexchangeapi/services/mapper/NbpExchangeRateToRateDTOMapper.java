package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDto;

import java.util.List;

public interface NbpExchangeRateToRateDTOMapper {

    List<RateDto> mapToRates(List<NbpExchangeRate> exchangesRateToMap, NbpExchangeRate baseExchangeRate);

    RateDto mapToRate(NbpExchangeRate exchangeRateToMap, NbpExchangeRate baseExchangeRate);
}
