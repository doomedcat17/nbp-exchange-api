package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface CurrencyTransactionRepository extends JpaRepository<CurrencyTransaction, Long> {

    @Query("SELECT transaction FROM CurrencyTransaction transaction WHERE transaction.date BETWEEN :startDate AND :endDate")
    Page<CurrencyTransaction> getAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
