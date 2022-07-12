package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private CurrencyTransactionRepository currencyTransactionRepository;

    @Test
    void shouldReturnTransactionDto() {

        //given
        String buyCurrencyCode = "USD";
        String sellCurrencyCode = "PLN";
        BigDecimal buyAmount = new BigDecimal("40");

        //when
        TransactionDto transactionDto =
                tradeService.buyCurrency(buyCurrencyCode, sellCurrencyCode, buyAmount);

        //then
        assertAll(
                ()-> assertEquals("PLN", transactionDto.getSellCode()),
                ()-> assertEquals("USD", transactionDto.getBuyCode()),
                ()-> assertEquals(new BigDecimal("163.60"), transactionDto.getSellAmount()),
                ()-> assertEquals(new BigDecimal("40.00"), transactionDto.getBuyAmount()),
                () -> assertEquals(10, currencyTransactionRepository.count())
        );
    }



}