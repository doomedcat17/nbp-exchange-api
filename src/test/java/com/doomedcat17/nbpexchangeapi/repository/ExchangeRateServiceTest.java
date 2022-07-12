package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Test
    void shouldAddAll() {
        //when
        try {
            TestDataProvider.sampleExchangeRates()
                    .forEach(nbpExchangeRate -> exchangeRateService.add(nbpExchangeRate));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //then
        assertEquals(36, exchangeRateService.getSize());

    }

    @Test
    void shouldIgnoreDuplicate() {
        //given
        NbpExchangeRate duplicateRate = new NbpExchangeRate(
                new Currency("JPY", "Jen japoński"),
                new BigDecimal("0.36"),
                LocalDate.parse("2021-11-30"));

        TestDataProvider.sampleExchangeRates()
                .forEach(nbpExchangeRate -> exchangeRateService.add(nbpExchangeRate));

        //when
        exchangeRateService.add(duplicateRate);

        //then
        assertEquals(36, exchangeRateService.getSize());

    }

}