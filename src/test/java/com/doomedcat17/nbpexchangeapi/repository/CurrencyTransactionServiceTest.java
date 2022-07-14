package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.services.CurrencyTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void shouldGetAllTransactions() {


        //when
        Page<CurrencyTransaction> currencyTransactionPage =
                currencyTransactionRepository.getAllBetweenDates(LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC),
                        LocalDateTime.ofEpochSecond(Integer.MAX_VALUE, 0, ZoneOffset.UTC), PageRequest.of(0, 50));

        //then
        assertEquals(9, currencyTransactionPage.getTotalElements());
    }

}