package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    public Optional<Currency> getByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    public void save(Currency currency) {
        currencyRepository.save(currency);
    }


}
