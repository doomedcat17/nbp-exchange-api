package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ExchangeRateDtoServiceTest {


    @Autowired
    private ExchangeRateDtoService exchangeRateDtoService;

    @Test
    void shouldReturnMostRecentRatesForGivenCode() {

        //given
        String currencyCode = "USD";

        //when
        ExchangeRateDTO exchangeRateDTO = exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);

        //then
        assertFalse(exchangeRateDTO.getRates().isEmpty());
        assertEquals(currencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();

        assertEquals(4, rates.size());
        assertTrue(rates.stream().allMatch(
                rateDTO ->
                        rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-30"))
                        || (rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-25")) && rateDTO.getCode().equals("AFN"))));
        assertTrue(rates.stream().noneMatch(rate -> rate.getCode().equals("USD")));
    }

    @Test
    void shouldNotReturnMostRecentRatesForGivenCode() {

        //given
        String currencyCode = "XD";

        //when
        ExchangeRateDTO exchangeRateDTO = exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);

        //then
        assertTrue(exchangeRateDTO.getRates().isEmpty());
    }

    @Test
    void shouldReturnRatesForGivenCodeAndDate() {

        //given
        String currencyCode = "USD";
        LocalDate date = LocalDate.of(2021, 11, 30);

        //when
        ExchangeRateDTO exchangeRateDTO = exchangeRateDtoService.getAllExchangeRatesForCodeAndDate(currencyCode, date);

        //then

        assertFalse(exchangeRateDTO.getRates().isEmpty());
        List<RateDTO> rates = exchangeRateDTO.getRates();
        assertEquals(3, rates.size());
        assertTrue(rates.stream().allMatch(rate -> rate.getEffectiveDate().equals(date)));
        assertTrue(rates.stream().noneMatch(rate -> rate.getCode().equals("USD")));
    }

    @Test
    void shouldReturnRecentExchangeRateForGivenCodes() {

        //given
        String sourceCurrencyCode = "USD";
        String targetPLNCode = "PLN";
        String targetAFNCode = "AFN";

        //when
        ExchangeRateDTO PlnExchangeRateDTO = exchangeRateDtoService.getRecentExchangeRate(sourceCurrencyCode, targetPLNCode);
        ExchangeRateDTO AfnExchangeRateDTO = exchangeRateDtoService.getRecentExchangeRate(sourceCurrencyCode, targetAFNCode);

        //then
        assertFalse(PlnExchangeRateDTO.getRates().isEmpty());
        assertFalse(AfnExchangeRateDTO.getRates().isEmpty());

        assertEquals(sourceCurrencyCode, PlnExchangeRateDTO.getCode());
        assertEquals(sourceCurrencyCode, AfnExchangeRateDTO.getCode());

        List<RateDTO> plnRates = PlnExchangeRateDTO.getRates();
        List<RateDTO> afnRates = AfnExchangeRateDTO.getRates();
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
        ExchangeRateDTO plnExchangeRateDTO = exchangeRateDtoService.getExchangeRateForCodeAndDate(sourceCurrencyCode, targetPLNCode, dateText);

        //then
        assertFalse(plnExchangeRateDTO.getRates().isEmpty());
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
        ExchangeRateDTO plnExchangeRateDTO = exchangeRateDtoService.getExchangeRatesForCodes(sourceCurrencyCode, targetPLNCode);

        //then
        assertFalse(plnExchangeRateDTO.getRates().isEmpty());
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
        Optional<RateDTO> foundUsdToPlnRateDTO = exchangeRateDtoService.getRecentRate(usdExchangeRate, plnExchangeRate);

        Optional<RateDTO> foundUsdToAfnRateDTO = exchangeRateDtoService.getRecentRate(usdExchangeRate, afnExchangeRate);

        Optional<RateDTO> foundAfnToPlnRateDTO = exchangeRateDtoService.getRecentRate(afnExchangeRate, plnExchangeRate);

        //then
        assertTrue(foundUsdToPlnRateDTO.isPresent());
        assertTrue(foundUsdToAfnRateDTO.isPresent());
        assertTrue(foundAfnToPlnRateDTO.isPresent());


        RateDTO usdToPlnRateDTO = foundUsdToPlnRateDTO.get();

        RateDTO usdToAfnRateDTO = foundUsdToAfnRateDTO.get();

        RateDTO afnToPlnRateDTO = foundAfnToPlnRateDTO.get();

        assertEquals("PLN", usdToPlnRateDTO.getCode());
        assertEquals("AFN", usdToAfnRateDTO.getCode());
        assertEquals("PLN", afnToPlnRateDTO.getCode());

        assertEquals(usdExchangeRate.getEffectiveDate(), usdToPlnRateDTO.getEffectiveDate());
        assertEquals(usdToAfnRateDTO.getEffectiveDate(), afnExchangeRate.getEffectiveDate());
        assertEquals(afnToPlnRateDTO.getEffectiveDate(), afnExchangeRate.getEffectiveDate());
    }



}