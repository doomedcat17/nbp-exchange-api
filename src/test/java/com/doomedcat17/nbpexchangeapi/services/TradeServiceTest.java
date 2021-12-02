package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyTransactionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TradeServiceTest {

    private final TradeService tradeService;

    private final CurrencyTransactionDao currencyTransactionDao;

    private final NbpExchangeRateRepository nbpExchangeRateRepository;

    @BeforeEach
    void init() {
        TestDataProvider.sampleExchangeRates()
                .forEach(nbpExchangeRateRepository::add);
    }

    @Test
    void shouldReturnTransactionDto() {

        //given
        String buyCurrencyCode = "USD";
        String sellCurrencyCode = "PLN";
        BigDecimal sellAmount = new BigDecimal("40");

        //when
        TransactionDto transactionDto =
                tradeService.buyCurrency(buyCurrencyCode, sellCurrencyCode, sellAmount);

        //then
        assertAll(
                ()-> assertEquals("PLN", transactionDto.getSellCode()),
                ()-> assertEquals("USD", transactionDto.getBuyCode()),
                ()-> assertEquals(new BigDecimal("40.00"), transactionDto.getSellAmount()),
                ()-> assertEquals(new BigDecimal("9.78"), transactionDto.getBuyAmount()),
                () -> assertEquals(1, currencyTransactionDao.count())
        );
    }




    @Autowired
    public TradeServiceTest(TradeService tradeService, CurrencyTransactionDao currencyTransactionDao, NbpExchangeRateRepository nbpExchangeRateRepository) {
        this.tradeService = tradeService;
        this.currencyTransactionDao = currencyTransactionDao;
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
    }
}