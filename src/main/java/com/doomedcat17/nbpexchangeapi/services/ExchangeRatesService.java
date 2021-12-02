package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.services.mapper.NbpExchangeRateToRateDTOMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRatesService {

    private final NbpExchangeRateRepository nbpExchangeRateRepository;

    private final NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper;

    public ExchangeRateDTO getRecentExchangeRatesForCode(String code) {
        List<RateDTO> rates = getRecentRatesForCurrency(code);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getAllExchangeRatesForCodeAndDate(String code, String textDate) {
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getAll(code, textDate);
        List<RateDTO> rates = new ArrayList<>();
        for (NbpExchangeRate baseExchangeRate : exchangeRates) {
            List<NbpExchangeRate> exchangeRatesToMap =
                    nbpExchangeRateRepository.getByAllByEffectiveDate(baseExchangeRate.getEffectiveDate());
            rates.addAll(
                    nbpExchangeRateToRateDTOMapper.mapToRates(exchangeRatesToMap, baseExchangeRate)
            );
        }
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getRecentExchangeRate(String sourceCurrency, String targetCurrency) {
        NbpExchangeRate sourceExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(sourceCurrency);
        NbpExchangeRate targetExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(targetCurrency);
        List<RateDTO> rates = List.of(getRecentRate(sourceExchangeRate, targetExchangeRate));
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrency);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getExchangeRateForCodeAndDate(String sourceCurrency, String targetCurrency, String textDate) {
        LocalDate date = LocalDate.parse(textDate);
        NbpExchangeRate sourceExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(sourceCurrency, date);
        NbpExchangeRate targetExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(targetCurrency, date);

        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrency);

        if (sourceExchangeRate == null || targetExchangeRate == null) {
            exchangeRateDTO.setRates(List.of());
            return exchangeRateDTO;
        }
        List<RateDTO> rates = List.of(
                nbpExchangeRateToRateDTOMapper.mapToRate(sourceExchangeRate, targetExchangeRate));
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getExchangeRatesForCodes(String sourceCurrency, String targetCurrency) {
        List<NbpExchangeRate> sourceExchangeRates = nbpExchangeRateRepository.getAll(sourceCurrency, "");
        List<RateDTO> rates = new ArrayList<>();
        for (NbpExchangeRate sourceExchangeRate : sourceExchangeRates) {
            NbpExchangeRate targetExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(
                    targetCurrency, sourceExchangeRate.getEffectiveDate()
            );
            if (targetExchangeRate == null) continue;
            rates.add(nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate, sourceExchangeRate));
        }
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrency);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    private List<RateDTO> getRecentRatesForCurrency(String currencyCode) {
        List<RateDTO> rateDTOS = new ArrayList<>();
        NbpExchangeRate baseExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(currencyCode);
        List<NbpExchangeRate> recentExchangeRates = nbpExchangeRateRepository
                .getMostRecent();
        for (NbpExchangeRate recentExchangeRate : recentExchangeRates) {
            if (!recentExchangeRate.equals(baseExchangeRate)) {
                rateDTOS.add(
                        getRecentRate(baseExchangeRate, recentExchangeRate)
                );
            }
        }
        return rateDTOS;
    }


    private RateDTO getRecentRate(NbpExchangeRate baseExchangeRate, NbpExchangeRate targetExchangeRate) {
        NbpExchangeRate sourceExchangeRate = baseExchangeRate;
        if (!sourceExchangeRate.getEffectiveDate().equals(targetExchangeRate.getEffectiveDate())) {
            sourceExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(
                    baseExchangeRate.getCurrency().getCode(),
                    targetExchangeRate.getEffectiveDate()
            );
            if (sourceExchangeRate == null) {
                sourceExchangeRate = baseExchangeRate;
                targetExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(
                        targetExchangeRate.getCurrency().getCode(),
                        baseExchangeRate.getEffectiveDate()
                );
            }
        }
        return nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate, sourceExchangeRate);

    }

    public ExchangeRatesService(NbpExchangeRateRepository nbpExchangeRateRepository, NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper) {
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
        this.nbpExchangeRateToRateDTOMapper = nbpExchangeRateToRateDTOMapper;
    }
}
