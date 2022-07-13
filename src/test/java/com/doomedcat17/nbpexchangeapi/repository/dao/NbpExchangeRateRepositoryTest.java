package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class NbpExchangeRateRepositoryTest {

    @Autowired
    private NbpExchangeRateRepository nbpExchangeRateRepository;

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
        NbpExchangeRate foundExchangeRate = nbpExchangeRateRepository
                .getMostRecentByCode("USD", PageRequest.of(0, 1)).get(0);
        NbpExchangeRate foundExchangeRate2 = nbpExchangeRateRepository
                .getMostRecentByCode("AFN", PageRequest.of(0, 1)).get(0);

        //then
        assertAll(
                () -> assertEquals(expectedNbpExchangeRate, foundExchangeRate),
                () -> assertEquals(expectedNbpExchangeRate2, foundExchangeRate2));
    }

    @Test
    void shouldReturnAllWithMatchingCode() {
        //when
        Page<NbpExchangeRate> foundExchangeRates = nbpExchangeRateRepository.getAllByCurrencyCode("USD", PageRequest.of(0, 50));

        //then
        assertEquals(10, foundExchangeRates.getContent().size());

    }

    @Test
    void shouldReturnMatchingExchangeRate() {
        //given
        LocalDate date = LocalDate.parse("2021-11-25");
        //when
        Optional<NbpExchangeRate> foundExchangeRate = nbpExchangeRateRepository.getByCurrencyCodeAndEffectiveDate("USD", date);

        //then
        assertTrue(foundExchangeRate.isPresent());
        assertEquals("USD", foundExchangeRate.get().getCurrency().getCode());
        assertEquals(date, foundExchangeRate.get().getEffectiveDate());

    }

    @Test
    void shouldReturnMostRecentForEachCurrency() {
        //given
        LocalDate mostRecentDate = LocalDate.parse("2021-11-30");
        LocalDate afnMostRecentDate = LocalDate.parse("2021-11-25");

        //when
        List<NbpExchangeRate> nbpExchangeRates = nbpExchangeRateRepository.getRecent();

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
        long sizeBefore = nbpExchangeRateRepository.count();

        //when
        nbpExchangeRateRepository.deleteAllByEffectiveDateBefore(date);

        //then
        assertEquals(sizeBefore - 7, nbpExchangeRateRepository.count());
    }

    @Test
    void shouldGetAllByEffectiveDate() {
        //given
        LocalDate date = LocalDate.parse("2021-11-25");

        //when
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getAllByEffectiveDate(date);

        //then
        assertEquals(5, exchangeRates.size());
        assertTrue(exchangeRates.stream().allMatch(nbpExchangeRate -> nbpExchangeRate.getEffectiveDate().equals(date)));
    }

}