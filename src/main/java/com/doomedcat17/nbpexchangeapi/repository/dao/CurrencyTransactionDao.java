package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CurrencyTransactionDao extends JpaRepository<CurrencyTransaction, Long> {

    CurrencyTransaction getTopByOrderByDate();

    @Query("SELECT transaction FROM CurrencyTransaction transaction WHERE YEAR(transaction.date) = :year AND MONTH(transaction.date) = :month AND DAY(transaction.date) = :day")
    List<CurrencyTransaction> getAllByDateYearAndDateMonthAndDateDay(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);

    @Query("SELECT transaction FROM CurrencyTransaction transaction WHERE transaction.date BETWEEN :startDate AND :endDate")
    List<CurrencyTransaction> getAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
