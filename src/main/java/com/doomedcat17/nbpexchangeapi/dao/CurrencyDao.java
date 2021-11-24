package com.doomedcat17.nbpexchangeapi.dao;

import com.doomedcat17.nbpexchangeapi.data.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDao extends JpaRepository<Currency, String> {
}
