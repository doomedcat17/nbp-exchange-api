package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyTransactionDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private CurrencyTransactionDao currencyTransactionDao;

    @Test
    void shouldReturnTransactionDto() {

        //given
        String buyCurrencyCode = "USD";
        String sellCurrencyCode = "PLN";
        BigDecimal buyAmount = new BigDecimal("40");

        //when
        Optional<TransactionDto> foundTransactionDto =
                tradeService.buyCurrency(buyCurrencyCode, sellCurrencyCode, buyAmount);

        //then
        assertTrue(foundTransactionDto.isPresent());

        TransactionDto transactionDto = foundTransactionDto.get();
        assertAll(
                ()-> assertEquals("PLN", transactionDto.getSellCode()),
                ()-> assertEquals("USD", transactionDto.getBuyCode()),
                ()-> assertEquals(new BigDecimal("163.60"), transactionDto.getSellAmount()),
                ()-> assertEquals(new BigDecimal("40.00"), transactionDto.getBuyAmount()),
                () -> assertEquals(10, currencyTransactionDao.count())
        );
    }



}