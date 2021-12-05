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
import java.util.Optional;

@Service
public class ExchangeRatesService {

    protected final NbpExchangeRateRepository nbpExchangeRateRepository;

    protected final NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper;

    public Optional<ExchangeRateDTO> getRecentExchangeRatesForCode(String code) {
        List<RateDTO> rates = getRecentRatesForCurrency(code);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        if (rates.isEmpty()) return Optional.empty();
        return Optional.of(exchangeRateDTO);
    }

    public Optional<ExchangeRateDTO> getAllExchangeRatesForCodeAndDate(String currencyCode, String textDate) {
        List<NbpExchangeRate> exchangeRates;
        if (textDate.isBlank()) {
            exchangeRates = nbpExchangeRateRepository.getAllByCurrencyCode(currencyCode);
        } else {
            LocalDate effectiveDate = LocalDate.parse(textDate);
            Optional<NbpExchangeRate> foundExchangeRate = nbpExchangeRateRepository
                    .getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
            exchangeRates = foundExchangeRate.map(List::of).orElseGet(List::of);
        }


        if (exchangeRates.isEmpty()) return Optional.empty();
        List<RateDTO> rates = new ArrayList<>();
        for (NbpExchangeRate baseExchangeRate : exchangeRates) {
            List<NbpExchangeRate> exchangeRatesToMap =
                    nbpExchangeRateRepository.getAllByEffectiveDate(baseExchangeRate.getEffectiveDate());
            rates.addAll(
                    nbpExchangeRateToRateDTOMapper.mapToRates(exchangeRatesToMap, baseExchangeRate)
            );
        }
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(currencyCode);
        exchangeRateDTO.setRates(rates);
        if(rates.isEmpty()) return Optional.empty();
        return Optional.of(exchangeRateDTO);
    }

    public Optional<ExchangeRateDTO> getRecentExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        Optional<NbpExchangeRate> sourceExchangeRate =
                nbpExchangeRateRepository.getMostRecentByCurrencyCode(sourceCurrencyCode);
        Optional<NbpExchangeRate> targetExchangeRate =
                nbpExchangeRateRepository.getMostRecentByCurrencyCode(targetCurrencyCode);
        if (sourceExchangeRate.isEmpty() || targetExchangeRate.isEmpty())
            return Optional.empty();

        Optional<RateDTO> foundRecentRate =
                getRecentRate(sourceExchangeRate.get(), targetExchangeRate.get());
        if (foundRecentRate.isEmpty()) return Optional.empty();
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        exchangeRateDTO.setRates(List.of(foundRecentRate.get()));
        return Optional.of(exchangeRateDTO);
    }

    public Optional<ExchangeRateDTO> getExchangeRateForCodeAndDate(String sourceCurrencyCode, String targetCurrencyCode, String textDate) {
        LocalDate date = LocalDate.parse(textDate);

        Optional<NbpExchangeRate> sourceExchangeRate =
                nbpExchangeRateRepository.getByCurrencyCodeAndEffectiveDate(sourceCurrencyCode, date);
        Optional<NbpExchangeRate> targetExchangeRate =
                nbpExchangeRateRepository.getByCurrencyCodeAndEffectiveDate(targetCurrencyCode, date);
        if (sourceExchangeRate.isEmpty() || targetExchangeRate.isEmpty()) return Optional.empty();

        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);

        List<RateDTO> rates = List.of(
                nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate.get(), sourceExchangeRate.get()));
        exchangeRateDTO.setRates(rates);
        return Optional.of(exchangeRateDTO);
    }

    public Optional<ExchangeRateDTO> getExchangeRatesForCodes(String sourceCurrencyCode, String targetCurrencyCode) {
        List<NbpExchangeRate> sourceExchangeRates = nbpExchangeRateRepository.getAllByCurrencyCode(sourceCurrencyCode);
        if (sourceExchangeRates.isEmpty()) return Optional.empty();

        List<RateDTO> targetRates = new ArrayList<>();
        for (NbpExchangeRate sourceExchangeRate : sourceExchangeRates) {
            Optional<NbpExchangeRate> targetExchangeRate = nbpExchangeRateRepository.getByCurrencyCodeAndEffectiveDate(
                    targetCurrencyCode, sourceExchangeRate.getEffectiveDate()
            );
            if (targetExchangeRate.isEmpty()) continue;
            targetRates.add(nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate.get(), sourceExchangeRate));
        }
        if (targetRates.isEmpty()) return Optional.empty();
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        exchangeRateDTO.setRates(targetRates);
        return Optional.of(exchangeRateDTO);
    }

    private List<RateDTO> getRecentRatesForCurrency(String sourceCurrencyCode) {
        List<RateDTO> rateDTOS = new ArrayList<>();
        Optional<NbpExchangeRate> foundSourceExchangeRate = nbpExchangeRateRepository.getMostRecentByCurrencyCode(sourceCurrencyCode);
        if (foundSourceExchangeRate.isEmpty()) return List.of();
        List<NbpExchangeRate> recentExchangeRates = nbpExchangeRateRepository
                .getMostRecent();

        NbpExchangeRate sourceExchangeRate = foundSourceExchangeRate.get();
        for (NbpExchangeRate recentExchangeRate : recentExchangeRates) {
            if (!recentExchangeRate.equals(sourceExchangeRate)) {
                Optional<RateDTO> foundRecentRate = getRecentRate(sourceExchangeRate, recentExchangeRate);
                foundRecentRate.ifPresent(rateDTOS::add);
            }
        }
        return rateDTOS;
    }


    protected Optional<RateDTO> getRecentRate(NbpExchangeRate baseExchangeRate, NbpExchangeRate targetExchangeRate) {
        if (!baseExchangeRate.getEffectiveDate().equals(targetExchangeRate.getEffectiveDate())) {
            Optional<NbpExchangeRate> foundSourceExchangeRate = nbpExchangeRateRepository.getByCurrencyCodeAndEffectiveDate(
                    baseExchangeRate.getCurrency().getCode(),
                    targetExchangeRate.getEffectiveDate()
            );
            if (foundSourceExchangeRate.isEmpty()) {
                Optional<NbpExchangeRate> foundTargetExchangeRate = nbpExchangeRateRepository
                        .getByCurrencyCodeAndEffectiveDate(
                                targetExchangeRate.getCurrency().getCode(),
                                baseExchangeRate.getEffectiveDate());
                if (foundTargetExchangeRate.isPresent()) targetExchangeRate = foundTargetExchangeRate.get();
                else return Optional.empty();
            } else baseExchangeRate = foundSourceExchangeRate.get();
        }
        return Optional.of(nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate, baseExchangeRate));

    }

    public ExchangeRatesService(NbpExchangeRateRepository nbpExchangeRateRepository, NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper) {
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
        this.nbpExchangeRateToRateDTOMapper = nbpExchangeRateToRateDTOMapper;
    }
}
