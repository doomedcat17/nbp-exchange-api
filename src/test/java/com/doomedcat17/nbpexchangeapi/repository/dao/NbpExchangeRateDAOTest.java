package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NbpExchangeRateDAOTest {

    @Autowired
    private NbpExchangeRateDAO nbpExchangeRateDAO;

    @BeforeEach
    void init() {
        nbpExchangeRateDAO.saveAll(TestDataProvider.sampleExchangeRates());
    }

    @Test
    void shouldReturnMostRecent() {
        //given
        NbpExchangeRate expectedNbpExchangeRate =
                new NbpExchangeRate(
                        new Currency("USD", "Dolar amerykański"),
                        new BigDecimal("4.09"),
                        LocalDate.parse("2021-11-30"));

        NbpExchangeRate expectedNbpExchangeRate2 =
                new NbpExchangeRate(
                        new Currency("AFN", "afgani (Afganistan)"),
                        new BigDecimal("0.044308"),
                        LocalDate.parse("2021-11-25"));

        //when
        NbpExchangeRate foundExchangeRate = nbpExchangeRateDAO.getMostRecentByCode("USD");
        NbpExchangeRate foundExchangeRate2 = nbpExchangeRateDAO.getMostRecentByCode("AFN");

        //then
        assertAll(
                () -> assertEquals(expectedNbpExchangeRate, foundExchangeRate),
                () -> assertEquals(expectedNbpExchangeRate2, foundExchangeRate2));
    }

    @Test
    void shouldReturnAllWithMatchingCode() {
        //when
        List<NbpExchangeRate> foundExchangeRates = nbpExchangeRateDAO.getAllByCurrencyCode("USD");

        //then
        assertEquals(10, foundExchangeRates.size());

    }

    @Test
    void shouldReturnMatchingExchangeRate() {
        //given
        LocalDate date = LocalDate.parse("2021-11-25");
        //when
        NbpExchangeRate foundExchangeRate = nbpExchangeRateDAO.getByCurrencyCodeAndEffectiveDate("USD", date);

        //then
        assertEquals("USD", foundExchangeRate.getCurrency().getCode());
        assertEquals(date, foundExchangeRate.getEffectiveDate());

    }

    @Test
    void shouldReturnMostRecentForEachCurrency() {
        //given
        LocalDate mostRecentDate = LocalDate.parse("2021-11-30");
        LocalDate afnMostRecentDate = LocalDate.parse("2021-11-25");

        //when
        List<NbpExchangeRate> nbpExchangeRates = nbpExchangeRateDAO.getRecent();

        //then
        assertAll(
                () -> assertTrue(nbpExchangeRates.stream()
                        .allMatch(nbpExchangeRate ->
                                nbpExchangeRate.getEffectiveDate().equals(mostRecentDate) ||
                                        (nbpExchangeRate.getCurrency().getCode().equals("AFN")
                                                && nbpExchangeRate.getEffectiveDate().equals(afnMostRecentDate)
                                        )
                        )
                ),
                () -> assertEquals(5, nbpExchangeRates.size())
        );
    }

    @Test
    void shouldRemoveOlderThanGivenDate() {
        //given
        LocalDate date = LocalDate.parse("2021-11-22");
        long sizeBefore = nbpExchangeRateDAO.count();
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateDAO.findAll();

        //when
        nbpExchangeRateDAO.deleteAllByEffectiveDateBefore(date);

        //then
        assertEquals(sizeBefore-7, nbpExchangeRateDAO.count());
    }

}