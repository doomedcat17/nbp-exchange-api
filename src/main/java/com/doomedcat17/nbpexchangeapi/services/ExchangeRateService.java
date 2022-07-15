package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateService {

    private final NbpExchangeRateRepository nbpExchangeRateRepository;

    private final CurrencyService currencyService;

    @Caching(evict = {
            @CacheEvict(cacheNames = {"allRecentRates", "ratesSize", "rateExchangeDto"}, allEntries = true),
            @CacheEvict(cacheNames = {"recentRates", "rateDtos"}, key = "#nbpExchangeRate.currency.code")
    })
    public void add(NbpExchangeRate nbpExchangeRate) {
        Optional<NbpExchangeRate> presentExchangeRate =
                getByCurrencyCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (presentExchangeRate.isEmpty()) {
            Optional<Currency> foundCurrency = currencyService.getByCode(nbpExchangeRate.getCurrency().getCode());
            if (foundCurrency.isPresent()) nbpExchangeRate.setCurrency(foundCurrency.get());
            else currencyService.save(nbpExchangeRate.getCurrency());
            nbpExchangeRateRepository.save(nbpExchangeRate);
        }
    }

    public List<NbpExchangeRate> getAllByCurrencyCode(String currencyCode) {
        return nbpExchangeRateRepository.getAllByCurrencyCode(currencyCode);
    }

    public void removeAllOlderThanGivenDate(LocalDate date) {
        nbpExchangeRateRepository.deleteAllByEffectiveDateBefore(date);
    }

    @Cacheable(cacheNames = "ratesSize")
    public long getSize() {
        return nbpExchangeRateRepository.count();
    }

    public long getSizeByCodeAndEffectiveDate(String currencyCode, LocalDate effectiveDate) {
        return nbpExchangeRateRepository.countAllByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
    }

    public long getSizeByEffectiveDate(LocalDate effectiveDate) {
        return nbpExchangeRateRepository.countAllByEffectiveDate(effectiveDate);
    }

    @Cacheable(cacheNames = "allRecentRates")
    public List<NbpExchangeRate> getMostRecent() {
        return nbpExchangeRateRepository.getRecent();
    }

    public Optional<NbpExchangeRate> getByCurrencyCodeAndEffectiveDate(String currencyCode, LocalDate effectiveDate) {
        return nbpExchangeRateRepository
                .getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
    }

    public List<NbpExchangeRate> getAllByEffectiveDate(LocalDate effectiveDate) {
        return nbpExchangeRateRepository.getAllByEffectiveDate(effectiveDate);
    }
    @Cacheable(cacheNames = "recentRates", key = "#currencyCode")
    public Optional<NbpExchangeRate> getMostRecentByCurrencyCode(String currencyCode) {
        List<NbpExchangeRate> foundExchangeRates = nbpExchangeRateRepository.getMostRecentByCode(currencyCode, PageRequest.of(0, 1));
        if (!foundExchangeRates.isEmpty()) return Optional.of(foundExchangeRates.get(0));
        else return Optional.empty();
    }
}
