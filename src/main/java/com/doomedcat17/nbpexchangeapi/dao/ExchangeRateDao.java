package com.doomedcat17.nbpexchangeapi.dao;

import com.doomedcat17.nbpexchangeapi.data.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateDao extends JpaRepository<ExchangeRate, Long> {
}
