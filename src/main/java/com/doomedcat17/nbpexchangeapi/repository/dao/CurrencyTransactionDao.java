package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyTransactionDao extends JpaRepository<CurrencyTransaction, Long> {
}
