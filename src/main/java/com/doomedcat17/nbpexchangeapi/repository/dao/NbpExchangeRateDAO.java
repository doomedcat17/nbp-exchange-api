package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface NbpExchangeRateDAO extends JpaRepository<NbpExchangeRate, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM nbp_exchange_rates WHERE currency_code = :code AND effective_date = :effectiveDate")
    NbpExchangeRate getByCurrencyCodeAndEffectiveDate(@Param("code") String code, @Param("effectiveDate") LocalDate effectiveDate);

    @Query(nativeQuery = true, value = "SELECT * FROM nbp_exchange_rates WHERE currency_code = :code ORDER BY effective_date DESC LIMIT 1")
    NbpExchangeRate getMostRecentByCode(@Param("code") String code);

    @Query(nativeQuery = true, value = "SELECT * FROM nbp_exchange_rates WHERE currency_code = :code ORDER BY effective_date DESC")
    List<NbpExchangeRate> getAllByCurrencyCode(@Param("code") String code);

    @Query(nativeQuery = true, value = "SELECT * FROM nbp_exchange_rates rates WHERE rates.effective_date = " +
            "(SELECT MAX(rates2.effective_date) FROM nbp_exchange_rates rates2 WHERE rates.currency_code = rates2.currency_code)")
    Set<NbpExchangeRate> getRecent();

}
