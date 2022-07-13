package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.PageDto;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDto;
import com.doomedcat17.nbpexchangeapi.mapper.NbpExchangeRateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateDtoService {

    private final ExchangeRateService exchangeRateService;
    private final NbpExchangeRateMapper mapper = NbpExchangeRateMapper.INSTANCE;

    @Cacheable(cacheNames = "rateDtos", key = "#code")
    public PageDto<RateDto> getRecentExchangeRatesForCode(String code) {
        PageDto<RateDto> pageDto = new PageDto<>();
        pageDto.setPage(1);
        pageDto.setTotalPages(1);
        List<RateDto> rates = getRecentRatesForCurrency(code);
        pageDto.setResults(rates);
        pageDto.setTotalPages(1);
        return pageDto;
    }

    public PageDto<RateDto> getAllExchangeRatesForCodeAndDate(String currencyCode, LocalDate effectiveDate, int pageNum) {
        PageDto<RateDto> pageDto = new PageDto();
        List<NbpExchangeRate> exchangeRates;
        if (Objects.isNull(effectiveDate)) {
            Page<NbpExchangeRate> nbpExchangeRatePage = exchangeRateService.getAllByCurrencyCode(currencyCode, pageNum);
            pageDto.setPage(pageNum);
            pageDto.setTotalPages(nbpExchangeRatePage.getTotalPages());
            exchangeRates = nbpExchangeRatePage.getContent();
        } else {
            pageDto.setPage(1);
            pageDto.setTotalPages(1);
            Optional<NbpExchangeRate> foundExchangeRate = exchangeRateService
                    .getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
            exchangeRates = foundExchangeRate.map(List::of).orElseGet(List::of);
        }


        if (exchangeRates.isEmpty()) return pageDto;
        List<RateDto> rates = new ArrayList<>();
        for (NbpExchangeRate baseExchangeRate : exchangeRates) {
            List<NbpExchangeRate> exchangeRatesToMap =
                    exchangeRateService.getAllByEffectiveDate(baseExchangeRate.getEffectiveDate());
            exchangeRatesToMap.stream().filter(nbpExchangeRate -> !nbpExchangeRate.getCurrency().getCode().equals(currencyCode))
                    .forEach(nbpExchangeRate -> rates.add(mapper.toRateDto(nbpExchangeRate, baseExchangeRate)));
        }
        pageDto.setResults(rates);
        return pageDto;
    }

    @Cacheable(cacheNames = "rateExchangeDto", key = "#sourceCurrencyCode.concat(#targetCurrencyCode)")
    public PageDto<RateDto> getRecentExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        PageDto<RateDto> pageDto = new PageDto();
        pageDto.setPage(1);
        pageDto.setTotalPages(0);
        List<RateDto> rateDtos = new ArrayList<>();
        pageDto.setResults(rateDtos);
        Optional<NbpExchangeRate> sourceExchangeRate =
                exchangeRateService.getMostRecentByCurrencyCode(sourceCurrencyCode);
        if (sourceExchangeRate.isEmpty()) return pageDto;
        Optional<NbpExchangeRate> targetExchangeRate =
                exchangeRateService.getMostRecentByCurrencyCode(targetCurrencyCode);
        if (targetExchangeRate.isEmpty()) return pageDto;
        getRecentRate(sourceExchangeRate.get(), targetExchangeRate.get()).ifPresent(
                rateDtos::add);
        pageDto.setTotalPages(1);
        return pageDto;
    }

    public PageDto<RateDto> getExchangeRatesForCodes(String sourceCurrencyCode, String targetCurrencyCode, LocalDate effectiveDate, int pageNum) {
        PageDto<RateDto> pageDto = new PageDto<>();
        pageDto.setPage(pageNum);
        List<RateDto> targetRates = new ArrayList<>();
        pageDto.setResults(targetRates);
        if (Objects.nonNull(effectiveDate)) {
            NbpExchangeRate sourceExchangeRate = exchangeRateService.getByCurrencyCodeAndEffectiveDate(sourceCurrencyCode, effectiveDate).get();
            NbpExchangeRate targetExchangeRate = exchangeRateService.getByCurrencyCodeAndEffectiveDate(targetCurrencyCode, effectiveDate).get();
            targetRates.add(mapper.toRateDto(targetExchangeRate, sourceExchangeRate));
        } else {
            Page<NbpExchangeRate> sourceExchangeRates = exchangeRateService.getAllByCurrencyCode(sourceCurrencyCode, pageNum);
            pageDto.setTotalPages(sourceExchangeRates.getTotalPages());
            if (sourceExchangeRates.isEmpty()) return pageDto;
            for (NbpExchangeRate sourceExchangeRate : sourceExchangeRates) {
                Optional<NbpExchangeRate> targetExchangeRate = exchangeRateService.getByCurrencyCodeAndEffectiveDate(
                        targetCurrencyCode, sourceExchangeRate.getEffectiveDate()
                );
                if (targetExchangeRate.isEmpty()) continue;
                targetRates.add(mapper.toRateDto(targetExchangeRate.get(), sourceExchangeRate));
            }
        }
        pageDto.setResults(targetRates);
        return pageDto;
    }

    private List<RateDto> getRecentRatesForCurrency(String sourceCurrencyCode) {
        List<RateDto> rateDtos = new ArrayList<>();
        Optional<NbpExchangeRate> foundSourceExchangeRate = exchangeRateService.getMostRecentByCurrencyCode(sourceCurrencyCode);
        if (foundSourceExchangeRate.isEmpty()) return List.of();
        List<NbpExchangeRate> recentExchangeRates = exchangeRateService
                .getMostRecent();

        NbpExchangeRate sourceExchangeRate = foundSourceExchangeRate.get();
        for (NbpExchangeRate recentExchangeRate : recentExchangeRates) {
            if (!recentExchangeRate.equals(sourceExchangeRate)) {
                Optional<RateDto> foundRecentRate = getRecentRate(sourceExchangeRate, recentExchangeRate);
                foundRecentRate.ifPresent(rateDtos::add);
            }
        }
        return rateDtos;
    }


    protected Optional<RateDto> getRecentRate(NbpExchangeRate baseExchangeRate, NbpExchangeRate targetExchangeRate) {
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
