package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CurrencyTransactionRepository extends JpaRepository<CurrencyTransaction, Long> {

    CurrencyTransaction getTopByOrderByDateDesc();

    @Query("SELECT transaction FROM CurrencyTransaction transaction WHERE YEAR(transaction.date) = :year AND MONTH(transaction.date) = :month AND DAY(transaction.date) = :day")
    List<CurrencyTransaction> getAllByDateYearAndDateMonthAndDateDay(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);

    @Query("SELECT transaction FROM CurrencyTransaction transaction WHERE transaction.date BETWEEN :startDate AND :endDate")
    List<CurrencyTransaction> getAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
