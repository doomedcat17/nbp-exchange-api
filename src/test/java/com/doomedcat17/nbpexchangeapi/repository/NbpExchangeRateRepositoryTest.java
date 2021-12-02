package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NbpExchangeRateRepositoryTest {

    @Autowired
    private NbpExchangeRateRepository nbpExchangeRateRepository;

    @Test
    void shouldAddAll() {
        //when
        try {
            TestDataProvider.sampleExchangeRates()
                    .forEach(nbpExchangeRate -> nbpExchangeRateRepository.add(nbpExchangeRate));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //then
        assertEquals(36, nbpExchangeRateRepository.getSize());

    }

    @Test
    void shouldIgnoreDuplicate() {
        //given
        NbpExchangeRate duplicateRate = new NbpExchangeRate(
                new Currency("JPY", "Jen japoński"),
                new BigDecimal("0.36"),
                LocalDate.parse("2021-11-30"));

        TestDataProvider.sampleExchangeRates()
                .forEach(nbpExchangeRate -> nbpExchangeRateRepository.add(nbpExchangeRate));

        //when
        nbpExchangeRateRepository.add(duplicateRate);

        //then
        assertEquals(36, nbpExchangeRateRepository.getSize());

    }

    @Test
    void shouldReturnMostRecent() {
        //given
        try {
            TestDataProvider.sampleExchangeRates()
                    .forEach(nbpExchangeRate -> nbpExchangeRateRepository.add(nbpExchangeRate));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //when
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getNbpExchangeRates("", "");

        //then
        assertEquals(5, exchangeRates.size());
        long mostRecentCounter = exchangeRates.stream()
                .filter(nbpExchangeRate ->
                        nbpExchangeRate.getEffectiveDate()
                                .equals(LocalDate.parse("2021-11-30")))
                        .count();
        assertEquals(4, mostRecentCounter);
        assertTrue(
                exchangeRates.stream().anyMatch(nbpExchangeRate ->
                        nbpExchangeRate.getEffectiveDate().equals(LocalDate.parse("2021-11-25"))
        ));
    }

    @Test
    void shouldReturnMostRecentForGivenCode() {
        //given
        try {
            TestDataProvider.sampleExchangeRates()
                    .forEach(nbpExchangeRate -> nbpExchangeRateRepository.add(nbpExchangeRate));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //when
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getNbpExchangeRates("USD", "");

        //then
        assertEquals(1, exchangeRates.size());
        NbpExchangeRate exchangeRate = exchangeRates.get(0);
        assertAll(
                () -> assertEquals("USD", exchangeRate.getCurrency().getCode()),
                () -> assertEquals(LocalDate.parse("2021-11-30"), exchangeRate.getEffectiveDate())
        );

    }

    @Test
    void shouldReturnMostRecentForGivenCodeAndDate() {
        //given
        try {
            TestDataProvider.sampleExchangeRates()
                    .forEach(nbpExchangeRate -> nbpExchangeRateRepository.add(nbpExchangeRate));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //when
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getNbpExchangeRates("USD", "2021-11-25");

        //then
        assertEquals(1, exchangeRates.size());
        NbpExchangeRate exchangeRate = exchangeRates.get(0);
        assertAll(
                () -> assertEquals("USD", exchangeRate.getCurrency().getCode()),
                () -> assertEquals(LocalDate.parse("2021-11-25"), exchangeRate.getEffectiveDate())
        );

    }

    @Test
    void shouldReturnForGivenDate() {
        //given
        try {
            TestDataProvider.sampleExchangeRates()
                    .forEach(nbpExchangeRate -> nbpExchangeRateRepository.add(nbpExchangeRate));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //when
        List<NbpExchangeRate> exchangeRates = nbpExchangeRateRepository.getNbpExchangeRates("", "2021-11-25");

        //then
        assertEquals(5, exchangeRates.size());
        assertTrue(
                exchangeRates.stream().allMatch(nbpExchangeRate ->
                        nbpExchangeRate.getEffectiveDate().equals(LocalDate.parse("2021-11-25"))
                ));

    }
}