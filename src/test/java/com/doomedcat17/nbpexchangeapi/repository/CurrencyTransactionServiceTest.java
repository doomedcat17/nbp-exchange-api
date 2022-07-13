package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.services.CurrencyTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}