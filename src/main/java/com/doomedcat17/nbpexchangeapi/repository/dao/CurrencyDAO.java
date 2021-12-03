package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDAO extends JpaRepository<Currency, String> {
}
