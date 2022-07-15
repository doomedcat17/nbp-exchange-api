package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    @Cacheable(cacheNames = "currencies", key = "#code")
    public Optional<Currency> getByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @CacheEvict(cacheNames = "currencies", key = "#currency.code")
    public void save(Currency currency) {
        currencyRepository.save(currency);
    }


}
