package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
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

}