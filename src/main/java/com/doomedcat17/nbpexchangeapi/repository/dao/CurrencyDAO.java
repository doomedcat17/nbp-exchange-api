package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyDAO extends CrudRepository<Currency, String> {
}
