package com.doomedcat17.nbpexchangeapi.repository.dao;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

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
    void shouldReturnAllWithMatchingCode() {
        //when
        List<NbpExchangeRate> foundExchangeRates = nbpExchangeRateRepository.getAllByCurrencyCode("USD");

        //then
        assertEquals(10, foundExchangeRates.size());

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