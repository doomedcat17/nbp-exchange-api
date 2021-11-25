package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NbpExchangeRateDAOTest {

    @Autowired
    private NbpExchangeRateDAO nbpExchangeRateDAO;

    @BeforeEach
    void init() {
        supplyNbpExchangeRateDAOWithDummyData();
    }
    @Test
    void shouldReturnLatest() {
        //given
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setName("Dolar amerykański");

        NbpExchangeRate expectedNbpExchangeRate = new NbpExchangeRate();
        expectedNbpExchangeRate.setCurrency(currency);
        expectedNbpExchangeRate.setMidRateInPLN(new BigDecimal("4.09"));
        expectedNbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-09-02"));

        //when
        NbpExchangeRate foundExchangeRate = nbpExchangeRateDAO.getLatestByCurrencyCode("USD");

        //then
        assertEquals(expectedNbpExchangeRate, foundExchangeRate);
    }

    @Test
    void shouldReturnAllWithMatchingCode() {
        //when
        List<NbpExchangeRate> foundExchangeRates = nbpExchangeRateDAO.getAllByCurrencyCode("USD");

        //then
        assertEquals(2, foundExchangeRates.size());

    }

    @Test
    void shouldReturnMatchingExchangeRate() {
        //given
        LocalDate date = LocalDate.parse("2021-07-02");
        //when
        NbpExchangeRate foundExchangeRate = nbpExchangeRateDAO.getByCurrencyCodeAndEffectiveDate("USD", date);

        //then
        assertEquals("USD", foundExchangeRate.getCurrency().getCode());
        assertEquals(date, foundExchangeRate.getEffectiveDate());

    }

    private void supplyNbpExchangeRateDAOWithDummyData() {
        Currency usdCurrency = new Currency();
        usdCurrency.setCode("USD");
        usdCurrency.setName("Dolar amerykański");

        Currency audCurrency = new Currency();
        audCurrency.setCode("AUD");
        audCurrency.setName("Dolar australijski");

        NbpExchangeRate nbpExchangeRate = new NbpExchangeRate();
        nbpExchangeRate.setCurrency(usdCurrency);
        nbpExchangeRate.setMidRateInPLN(new BigDecimal("4.20"));
        nbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-07-02"));

        NbpExchangeRate nbpExchangeRate2 = new NbpExchangeRate();
        nbpExchangeRate2.setCurrency(usdCurrency);
        nbpExchangeRate2.setMidRateInPLN(new BigDecimal("4.09"));
        nbpExchangeRate2.setEffectiveDate(LocalDate.parse("2021-09-02"));

        NbpExchangeRate nbpExchangeRate3 = new NbpExchangeRate();
        nbpExchangeRate3.setCurrency(audCurrency);
        nbpExchangeRate3.setMidRateInPLN(new BigDecimal("4.09"));
        nbpExchangeRate3.setEffectiveDate(LocalDate.parse("2021-09-02"));

        nbpExchangeRateDAO.save(nbpExchangeRate);
        nbpExchangeRateDAO.save(nbpExchangeRate2);
        nbpExchangeRateDAO.save(nbpExchangeRate3);

    }

}