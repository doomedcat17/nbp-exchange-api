package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NbpExchangeRateRepository extends JpaRepository<NbpExchangeRate, Long> {

    @Query("SELECT rates FROM NbpExchangeRate rates WHERE rates.currency.code = :code AND rates.effectiveDate= :effectiveDate")
    Optional<NbpExchangeRate> getByCurrencyCodeAndEffectiveDate(@Param("code") String code, @Param("effectiveDate") LocalDate effectiveDate);


    @Query("SELECT COUNT(rates) FROM NbpExchangeRate rates WHERE rates.currency.code = :code AND rates.effectiveDate= :effectiveDate")
    long countAllByCurrencyCodeAndEffectiveDate(@Param("code") String currencyCode, @Param("effectiveDate") LocalDate effectiveDate);

    long countAllByEffectiveDate(LocalDate effectiveDate);

    List<NbpExchangeRate> getAllByEffectiveDate(LocalDate date);

    @Query("SELECT rates FROM NbpExchangeRate rates WHERE rates.currency.code = :code ORDER BY rates.effectiveDate DESC")
    List<NbpExchangeRate> getMostRecentByCode(@Param("code") String code, Pageable pageable);

    @Query("SELECT rates FROM NbpExchangeRate rates WHERE rates.currency.code = :code ORDER BY rates.effectiveDate DESC")
    List<NbpExchangeRate> getAllByCurrencyCode(@Param("code") String code);

    @Query("SELECT rates FROM NbpExchangeRate rates WHERE rates.effectiveDate = " +
            "(SELECT MAX(rates2.effectiveDate) FROM NbpExchangeRate rates2 WHERE rates.currency.code = rates2.currency.code)")
    List<NbpExchangeRate> getRecent();

    void deleteAllByEffectiveDateBefore(LocalDate date);

}
