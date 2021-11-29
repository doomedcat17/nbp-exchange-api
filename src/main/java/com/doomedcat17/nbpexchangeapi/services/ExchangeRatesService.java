package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.services.mapper.NbpExchangeRateToRateDTOMapper;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class ExchangeRatesService {

    private final NbpExchangeRateRepository nbpExchangeRateRepository;

    private final NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper;

    public ExchangeRateDTO getRecentRatesByCode(String code) {
        Set<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getMostRecent();
        NbpExchangeRate baseExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(code);
        Set<RateDTO> rates = nbpExchangeRateToRateDTOMapper.mapToRates(exchangeRates, baseExchangeRate);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRatesService(NbpExchangeRateRepository nbpExchangeRateRepository, NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper) {
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
        this.nbpExchangeRateToRateDTOMapper = nbpExchangeRateToRateDTOMapper;
    }
}
