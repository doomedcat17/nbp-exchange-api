package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.exceptions.CurrencyNotFoundException;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.services.mapper.NbpExchangeRateToRateDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class ExchangeRatesServiceTest extends ExchangeRatesService {

    @Test
    @Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
    void shouldReturnMostRecentRatesForGivenCode() {

        //given
        String currencyCode = "USD";

        //when
        ExchangeRateDTO exchangeRateDTO = getRecentExchangeRatesForCode(currencyCode);

        //then
        assertEquals(currencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();
        assertEquals(4, rates.size());
        assertTrue(rates.stream().noneMatch(rate -> rate.getCode().equals("USD")));
    }

    @Test
    void shouldNotReturnMostRecentRatesForGivenCode() {

        //given
        String currencyCode = "XD";

        //then
        assertThrows(CurrencyNotFoundException.class, () -> getRecentExchangeRatesForCode(currencyCode));
    }

    @Test
    void shouldNotReturnRatesForGivenCodeAndDateTxt() {

        //given
        String currencyCode = "USD";
        String dateText = "2021-11-30";

        //when
        ExchangeRateDTO exchangeRateDTO = getAllExchangeRatesForCodeAndDate(currencyCode, dateText);

        //then
        assertEquals(currencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();
        assertEquals(3, rates.size());
        assertTrue(rates.stream().allMatch(rate -> rate.getEffectiveDate().equals(LocalDate.parse(dateText))));
        assertTrue(rates.stream().noneMatch(rate -> rate.getCode().equals("USD")));
    }

    @Test
    void shouldReturnRecentExchangeRateForGivenCodes() {

        //given
        String sourceCurrencyCode = "USD";
        String targetPLNCode = "PLN";
        String targetAFNCode = "AFN";

        //when
        ExchangeRateDTO plnExchangeRateDTO = getRecentExchangeRate(sourceCurrencyCode, targetPLNCode);
        ExchangeRateDTO afnExchangeRateDTO = getRecentExchangeRate(sourceCurrencyCode, targetAFNCode);

        //then
        assertEquals(sourceCurrencyCode, plnExchangeRateDTO.getCode());
        assertEquals(sourceCurrencyCode, afnExchangeRateDTO.getCode());

        List<RateDTO> plnRates = plnExchangeRateDTO.getRates();
        List<RateDTO> afnRates = afnExchangeRateDTO.getRates();
        assertEquals(1, plnRates.size());
        assertEquals(1, afnRates.size());

        RateDTO plnRate = plnRates.get(0);
        RateDTO afnRate = afnRates.get(0);
        assertEquals(plnRate.getCode(), targetPLNCode);
        assertEquals(afnRate.getCode(), targetAFNCode);

        assertEquals(plnRate.getEffectiveDate(), LocalDate.parse("2021-11-30"));
        assertEquals(afnRate.getEffectiveDate(), LocalDate.parse("2021-11-25"));
    }

    @Test
    void shouldReturnRecentExchangeRateForGivenCodesAndDate() {

        //given
        String sourceCurrencyCode = "USD";
        String targetPLNCode = "PLN";
        String dateText = "2021-11-30";

        //when
        ExchangeRateDTO plnExchangeRateDTO = getExchangeRateForCodeAndDate(sourceCurrencyCode, targetPLNCode, dateText);

        //then
        assertEquals(sourceCurrencyCode, plnExchangeRateDTO.getCode());

        List<RateDTO> plnRates = plnExchangeRateDTO.getRates();
        assertEquals(1, plnRates.size());

        RateDTO plnRate = plnRates.get(0);
        assertEquals(plnRate.getCode(), targetPLNCode);

        assertEquals(plnRate.getEffectiveDate(), LocalDate.parse("2021-11-30"));
    }


    @Test
    void shouldReturnAllExchangeRatesForGivenCodes() {

        //given
        String sourceCurrencyCode = "USD";
        String targetPLNCode = "PLN";

        //when
        ExchangeRateDTO plnExchangeRateDTO = getExchangeRatesForCodes(sourceCurrencyCode, targetPLNCode);

        //then
        assertEquals(sourceCurrencyCode, plnExchangeRateDTO.getCode());

        List<RateDTO> plnRates = plnExchangeRateDTO.getRates();
        assertEquals(8, plnRates.size());
        assertTrue(plnRates.stream().allMatch(rate -> rate.getCode().equals("PLN")));
    }


    @Test
    void shouldReturnCorrectRecentRatesForGivenExchangeRates() {

        //given
        NbpExchangeRate usdExchangeRate = new NbpExchangeRate(
                new Currency("USD", "Dolar amerykański"),
                new BigDecimal("4.09"),
                LocalDate.parse("2021-11-30"));

        NbpExchangeRate plnExchangeRate = new NbpExchangeRate(
                new Currency("PLN", "Polski złoty"),
                new BigDecimal("1.00"),
                LocalDate.parse("2021-11-30"));

        NbpExchangeRate afnExchangeRate = new NbpExchangeRate(
                new Currency("AFN", "afgani (Afganistan)"),
                new BigDecimal("0.044308"),
                LocalDate.parse("2021-11-25"));

        //when
        RateDTO usdToPlnRateDTO = getRecentRate(usdExchangeRate, plnExchangeRate);

        RateDTO usdToAfnRateDTO = getRecentRate(usdExchangeRate, afnExchangeRate);

        RateDTO afnToPlnRateDTO = getRecentRate(afnExchangeRate, plnExchangeRate);

        //then
        assertEquals("PLN", usdToPlnRateDTO.getCode());
        assertEquals("AFN", usdToAfnRateDTO.getCode());
        assertEquals("PLN", afnToPlnRateDTO.getCode());

        assertEquals(usdExchangeRate.getEffectiveDate(), usdToPlnRateDTO.getEffectiveDate());
        assertEquals(usdToAfnRateDTO.getEffectiveDate(), afnExchangeRate.getEffectiveDate());
        assertEquals(afnToPlnRateDTO.getEffectiveDate(), afnExchangeRate.getEffectiveDate());
    }

    @Autowired
    public ExchangeRatesServiceTest(NbpExchangeRateRepository nbpExchangeRateRepository, NbpExchangeRateToRateDTOMapper nbpExchangeRateToRateDTOMapper) {
        super(nbpExchangeRateRepository, nbpExchangeRateToRateDTOMapper);
    }


}