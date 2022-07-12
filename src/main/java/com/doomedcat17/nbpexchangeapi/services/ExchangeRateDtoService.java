package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.mapper.NbpExchangeRateMapper;
import com.doomedcat17.nbpexchangeapi.services.mapper.NbpExchangeRateToRateDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExchangeRateDtoService {

    private final ExchangeRateService exchangeRateService;

    private final NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper;

    private final NbpExchangeRateMapper mapper = NbpExchangeRateMapper.INSTANCE;


    public ExchangeRateDTO getRecentExchangeRatesForCode(String code) {
        List<RateDTO> rates = getRecentRatesForCurrency(code);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(code);
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getAllExchangeRatesForCodeAndDate(String currencyCode, LocalDate effectiveDate) {
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(currencyCode);
        List<NbpExchangeRate> exchangeRates;
        if (Objects.isNull(effectiveDate)) {
            exchangeRates = exchangeRateService.getAllByCurrencyCode(currencyCode);
        } else {
            Optional<NbpExchangeRate> foundExchangeRate = exchangeRateService
                    .getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
            exchangeRates = foundExchangeRate.map(List::of).orElseGet(List::of);
        }


        if (exchangeRates.isEmpty()) return exchangeRateDTO;
        List<RateDTO> rates = new ArrayList<>();
        for (NbpExchangeRate baseExchangeRate : exchangeRates) {
            List<NbpExchangeRate> exchangeRatesToMap =
                    exchangeRateService.getAllByEffectiveDate(baseExchangeRate.getEffectiveDate());
            rates.addAll(
                    nbpExchangeRateToRateDTOMapper.mapToRates(exchangeRatesToMap, baseExchangeRate)
            );
        }
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getRecentExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        if (sourceCurrencyCode.equals(targetCurrencyCode)) return exchangeRateDTO;
        Optional<NbpExchangeRate> sourceExchangeRate =
                exchangeRateService.getMostRecentByCurrencyCode(sourceCurrencyCode);
        if (sourceExchangeRate.isEmpty()) return exchangeRateDTO;
        Optional<NbpExchangeRate> targetExchangeRate =
                exchangeRateService.getMostRecentByCurrencyCode(targetCurrencyCode);
        if (targetExchangeRate.isEmpty()) return exchangeRateDTO;
        getRecentRate(sourceExchangeRate.get(), targetExchangeRate.get()).ifPresent(
                        rateDTO -> exchangeRateDTO.setRates(List.of(rateDTO)));
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getExchangeRateForCodeAndDate(String sourceCurrencyCode, String targetCurrencyCode, String textDate) {
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        if (sourceCurrencyCode.equals(targetCurrencyCode)) return exchangeRateDTO;
        LocalDate date = LocalDate.parse(textDate);

        Optional<NbpExchangeRate> sourceExchangeRate =
                exchangeRateService.getByCurrencyCodeAndEffectiveDate(sourceCurrencyCode, date);
        Optional<NbpExchangeRate> targetExchangeRate =
                exchangeRateService.getByCurrencyCodeAndEffectiveDate(targetCurrencyCode, date);
        if (sourceExchangeRate.isEmpty() || targetExchangeRate.isEmpty()) return exchangeRateDTO;

        List<RateDTO> rates = List.of(
                nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate.get(), sourceExchangeRate.get()));
        exchangeRateDTO.setRates(rates);
        return exchangeRateDTO;
    }

    public ExchangeRateDTO getExchangeRatesForCodes(String sourceCurrencyCode, String targetCurrencyCode) {
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setCode(sourceCurrencyCode);
        if (sourceCurrencyCode.equals(targetCurrencyCode)) return exchangeRateDTO;
        List<NbpExchangeRate> sourceExchangeRates = exchangeRateService.getAllByCurrencyCode(sourceCurrencyCode);
        if (sourceExchangeRates.isEmpty()) return exchangeRateDTO;

        List<RateDTO> targetRates = new ArrayList<>();
        for (NbpExchangeRate sourceExchangeRate : sourceExchangeRates) {
            Optional<NbpExchangeRate> targetExchangeRate = exchangeRateService.getByCurrencyCodeAndEffectiveDate(
                    targetCurrencyCode, sourceExchangeRate.getEffectiveDate()
            );
            if (targetExchangeRate.isEmpty()) continue;
            targetRates.add(nbpExchangeRateToRateDTOMapper.mapToRate(targetExchangeRate.get(), sourceExchangeRate));
        }
        if (targetRates.isEmpty()) return exchangeRateDTO;
        exchangeRateDTO.setRates(targetRates);
        return exchangeRateDTO;
    }

    private List<RateDTO> getRecentRatesForCurrency(String sourceCurrencyCode) {
        List<RateDTO> rateDTOS = new ArrayList<>();
        Optional<NbpExchangeRate> foundSourceExchangeRate = exchangeRateService.getMostRecentByCurrencyCode(sourceCurrencyCode);
        if (foundSourceExchangeRate.isEmpty()) return List.of();
        List<NbpExchangeRate> recentExchangeRates = exchangeRateService
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
            Optional<NbpExchangeRate> foundSourceExchangeRate = exchangeRateService.getByCurrencyCodeAndEffectiveDate(
                    baseExchangeRate.getCurrency().getCode(),
                    targetExchangeRate.getEffectiveDate()
            );
            if (foundSourceExchangeRate.isEmpty()) {
                Optional<NbpExchangeRate> foundTargetExchangeRate = exchangeRateService
                        .getByCurrencyCodeAndEffectiveDate(
                                targetExchangeRate.getCurrency().getCode(),
                                baseExchangeRate.getEffectiveDate());
                if (foundTargetExchangeRate.isPresent()) targetExchangeRate = foundTargetExchangeRate.get();
                else return Optional.empty();
            } else baseExchangeRate = foundSourceExchangeRate.get();
        }
        return Optional.of(mapper.toRateDto(targetExchangeRate, baseExchangeRate));

    }

}
