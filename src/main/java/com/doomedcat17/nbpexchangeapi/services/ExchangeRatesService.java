package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.exceptions.CurrencyNotFoundException;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.services.mapper.NbpExchangeRateToRateDTOMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRatesService {

    protected final NbpExchangeRateRepository nbpExchangeRateRepository;

    private final NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper;

    public ExchangeRateDTO getRecentExchangeRatesForCode(String code) {
        List<RateDTO> rates = getRecentRatesForCurrency(code);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getAllExchangeRatesForCodeAndDate(String currencyCode, String textDate) {
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getAll(currencyCode, textDate);
        if (exchangeRates.isEmpty()) throw new CurrencyNotFoundException(currencyCode);
        List<RateDTO> rates = new ArrayList<>();
        for (NbpExchangeRate baseExchangeRate : exchangeRates) {
            List<NbpExchangeRate> exchangeRatesToMap =
                    nbpExchangeRateRepository.getByAllByEffectiveDate(baseExchangeRate.getEffectiveDate());
            rates.addAll(
                    nbpExchangeRateToRateDTOMapper.mapToRates(exchangeRatesToMap, baseExchangeRate)
            );
        }
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(currencyCode);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getRecentExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        NbpExchangeRate sourceExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(sourceCurrencyCode);
        if (sourceExchangeRate == null) throw new CurrencyNotFoundException(sourceCurrencyCode);
        NbpExchangeRate targetExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(targetCurrencyCode);
        if (targetExchangeRate == null) throw new CurrencyNotFoundException(targetCurrencyCode);

        List<RateDTO> rates = List.of(getRecentRate(sourceExchangeRate, targetExchangeRate));
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getExchangeRateForCodeAndDate(String sourceCurrencyCode, String targetCurrencyCode, String textDate) {
        LocalDate date = LocalDate.parse(textDate);

        NbpExchangeRate sourceExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(sourceCurrencyCode, date);
        if (sourceExchangeRate == null) throw new CurrencyNotFoundException(sourceCurrencyCode);
        NbpExchangeRate targetExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(targetCurrencyCode, date);
        if (targetExchangeRate == null) throw new CurrencyNotFoundException(targetCurrencyCode);

        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);

        List<RateDTO> rates = List.of(
                nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate, sourceExchangeRate));
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getExchangeRatesForCodes(String sourceCurrencyCode, String targetCurrencyCode) {
        List<NbpExchangeRate> sourceExchangeRates = nbpExchangeRateRepository.getAll(sourceCurrencyCode, "");
        if (sourceExchangeRates.isEmpty()) throw new CurrencyNotFoundException(sourceCurrencyCode);

        List<RateDTO> targetRates = new ArrayList<>();
        for (NbpExchangeRate sourceExchangeRate : sourceExchangeRates) {
            NbpExchangeRate targetExchangeRate = nbpExchangeRateRepository.getByCodeAndEffectiveDate(
                    targetCurrencyCode, sourceExchangeRate.getEffectiveDate()
            );
            if (targetExchangeRate == null) continue;
            targetRates.add(nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate, sourceExchangeRate));
        }
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        exchangeRateDTO.setRates(targetRates);
        return exchangeRateDTO;
    }

    private List<RateDTO> getRecentRatesForCurrency(String sourceCurrencyCode) {
        List<RateDTO> rateDTOS = new ArrayList<>();
        NbpExchangeRate sourceExchangeRate = nbpExchangeRateRepository.getMostRecentByCode(sourceCurrencyCode);
        if (sourceExchangeRate == null) throw new CurrencyNotFoundException(sourceCurrencyCode);
        List<NbpExchangeRate> recentExchangeRates = nbpExchangeRateRepository
                .getMostRecent();
        for (NbpExchangeRate recentExchangeRate : recentExchangeRates) {
            if (!recentExchangeRate.equals(sourceExchangeRate)) {
                rateDTOS.add(
                        getRecentRate(sourceExchangeRate, recentExchangeRate)
                );
            }
        }
        return rateDTOS;
    }


    protected RateDTO getRecentRate(NbpExchangeRate baseExchangeRate, NbpExchangeRate targetExchangeRate) {
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
