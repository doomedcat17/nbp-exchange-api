package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.model.Currency;
import com.doomedcat17.nbpexchangeapi.data.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateDao extends JpaRepository<ExchangeRate, Long> {

    ExchangeRate getByBuyAndSellAndEffectiveDate(Currency buy, Currency sell, LocalDate effectiveDate);
}
