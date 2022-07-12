package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.services.CurrencyTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CurrencyTransactionServiceTest {

    @Autowired
    private CurrencyTransactionRepository currencyTransactionRepository;

    @Autowired
    private CurrencyTransactionService currencyTransactionService;

    @Test
    void shouldAddTransaction() {

        //given
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(LocalDateTime.now());
        transactionDto.setSellAmount(new BigDecimal("12"));
        transactionDto.setBuyAmount(new BigDecimal("20"));
        transactionDto.setSellCode("PLN");
        transactionDto.setBuyCode("USD");

        //when
        currencyTransactionService.addTransaction(transactionDto);

        //then
        assertEquals(10, currencyTransactionRepository.count());
    }

    @Test
    void shouldReturnLatestTransaction() {

        //when
        TransactionDto foundTransaction = currencyTransactionService.getLatestTransaction();

        //then
        assertEquals("USD", foundTransaction.getBuyCode());
        assertEquals("PLN", foundTransaction.getSellCode());
        assertEquals(new Date(1638442800000L), foundTransaction.getDate());
    }

    @Test
    void shouldReturnTransactionsFromGivenDate() {

        LocalDate expectedDate = LocalDate.parse("2021-11-29");

        //when
        List<TransactionDto> foundTransactions = currencyTransactionService.getAllByDate(expectedDate);

        //then
        assertEquals(2, foundTransactions.size());
    }

    @Test
    void shouldReturnAllBetweenGivenDates() {

        //given
        LocalDate startDate = LocalDate.parse("2021-11-29");
        LocalDate endDate = LocalDate.parse("2021-12-02");

        //when
        List<TransactionDto> foundTransactions = currencyTransactionService.getAllFromGivenDates(startDate ,endDate);

        //then
        LocalDateTime dateAfterEndDate = LocalDateTime.parse("2021-12-03");
        assertTrue(foundTransactions
                .stream().allMatch(transactionDto ->
                        !transactionDto.getDate().isBefore(LocalDateTime.from(startDate))
                                && (!transactionDto.getDate().equals(endDate) || !transactionDto.getDate().isAfter(dateAfterEndDate))));
        assertEquals(5, foundTransactions.size());
    }
}