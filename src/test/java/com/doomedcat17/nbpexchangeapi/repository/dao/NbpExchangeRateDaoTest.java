package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class NbpExchangeRateDaoTest {

    @Autowired
    private NbpExchangeRateDao nbpExchangeRateDAO;

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
        NbpExchangeRate foundExchangeRate = nbpExchangeRateDAO
                .getMostRecentByCode("USD", PageRequest.of(0, 1)).get(0);
        NbpExchangeRate foundExchangeRate2 = nbpExchangeRateDAO
                .getMostRecentByCode("AFN", PageRequest.of(0, 1)).get(0);

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

        //when
        nbpExchangeRateDAO.deleteAllByEffectiveDateBefore(date);

        //then
        assertEquals(sizeBefore - 7, nbpExchangeRateDAO.count());
    }

    @Test
    void shouldGetAllByEffectiveDate() {
        //given
        LocalDate date = LocalDate.parse("2021-11-25");

        //when
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateDAO.getAllByEffectiveDate(date);

        //then
        assertEquals(5, exchangeRates.size());
        assertTrue(exchangeRates.stream().allMatch(nbpExchangeRate -> nbpExchangeRate.getEffectiveDate().equals(date)));
    }

}