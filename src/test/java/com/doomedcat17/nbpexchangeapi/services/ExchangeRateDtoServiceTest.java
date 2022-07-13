package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.PageDto;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDto;
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
        PageDto<RateDto> rateDtoPageDto = exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);

        //then
        List<RateDto> rates = rateDtoPageDto.getResults();

        assertFalse(rates.isEmpty());
        assertTrue(rates.stream().allMatch(rateDto -> rateDto.getCode().equals(currencyCode)));

        assertEquals(4, rates.size());
        assertTrue(rates.stream().allMatch(
                rateDTO ->
                        rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-30"))
                                || (rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-25")) && rateDTO.getTargetCode().equals("AFN"))));
        assertTrue(rates.stream().noneMatch(rate -> rate.getTargetCode().equals("USD")));
    }

    @Test
    void shouldNotReturnMostRecentRatesForGivenCode() {

        //given
        String currencyCode = "XD";

        //when
        PageDto<RateDto> rateDtoPageDto = exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);

        //then
        List<RateDto> rates = rateDtoPageDto.getResults();

        assertTrue(rates.isEmpty());
    }

    @Test
    void shouldReturnRatesForGivenCodeAndDate() {

        //given
        String currencyCode = "USD";
        LocalDate date = LocalDate.of(2021, 11, 30);

        //when
        PageDto<RateDto> rateDtoPageDto = exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);

        //then
        List<RateDto> rates = rateDtoPageDto.getResults();

        assertFalse(rates.isEmpty());
        assertTrue(rates.stream().allMatch(rateDto -> rateDto.getCode().equals(currencyCode)));
        assertEquals(4, rates.size());
        assertTrue(rates.stream().noneMatch(rate -> rate.getTargetCode().equals("USD")));
    }

    @Test
    void shouldReturnRecentExchangeRateForGivenCodes() {

        //given
        String sourceCurrencyCode = "USD";
        String targetPLNCode = "PLN";
        String targetAFNCode = "AFN";

        //when
        List<RateDto> plnRates = exchangeRateDtoService.getRecentExchangeRate(sourceCurrencyCode, targetPLNCode).getResults();
        List<RateDto> afnRates = exchangeRateDtoService.getRecentExchangeRate(sourceCurrencyCode, targetAFNCode).getResults();


        //then
        assertFalse(plnRates.isEmpty());
        assertFalse(afnRates.isEmpty());

        assertTrue(plnRates.stream().allMatch(rateDto -> rateDto.getCode().equals(sourceCurrencyCode)));
        assertTrue(afnRates.stream().allMatch(rateDto -> rateDto.getCode().equals(sourceCurrencyCode)));

        assertEquals(1, plnRates.size());
        assertEquals(1, afnRates.size());

        RateDto plnRate = plnRates.get(0);
        RateDto afnRate = afnRates.get(0);
        assertEquals(targetPLNCode, plnRate.getTargetCode());
        assertEquals(targetAFNCode, afnRate.getTargetCode());

        assertEquals(LocalDate.parse("2021-11-30"), plnRate.getEffectiveDate());
        assertEquals(LocalDate.parse("2021-11-25"), afnRate.getEffectiveDate());
    }




    @Test
    void shouldReturnAllExchangeRatesForGivenCodes() {

        //given
        String sourceCurrencyCode = "USD";
        String targetPLNCode = "PLN";

        //when
        List<RateDto> plnRates = exchangeRateDtoService.getExchangeRatesForCodes(sourceCurrencyCode, targetPLNCode, null, 1).getResults();

        //then
        assertFalse(plnRates.isEmpty());
        assertTrue(plnRates.stream().allMatch(rateDto -> rateDto.getCode().equals(sourceCurrencyCode)));

        assertEquals(8, plnRates.size());
        assertTrue(plnRates.stream().allMatch(rate -> rate.getTargetCode().equals("PLN")));
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
        Optional<RateDto> foundUsdToPlnRateDTO = exchangeRateDtoService.getRecentRate(usdExchangeRate, plnExchangeRate);

        Optional<RateDto> foundUsdToAfnRateDTO = exchangeRateDtoService.getRecentRate(usdExchangeRate, afnExchangeRate);

        Optional<RateDto> foundAfnToPlnRateDTO = exchangeRateDtoService.getRecentRate(afnExchangeRate, plnExchangeRate);

        //then
        assertTrue(foundUsdToPlnRateDTO.isPresent());
        assertTrue(foundUsdToAfnRateDTO.isPresent());
        assertTrue(foundAfnToPlnRateDTO.isPresent());


        RateDto usdToPLnRateDto = foundUsdToPlnRateDTO.get();

        RateDto usdToAfnRateDto = foundUsdToAfnRateDTO.get();

        RateDto afnToPLnRateDto = foundAfnToPlnRateDTO.get();

        assertEquals("PLN", usdToPLnRateDto.getTargetCode());
        assertEquals("AFN", usdToAfnRateDto.getTargetCode());
        assertEquals("PLN", afnToPLnRateDto.getTargetCode());

        assertEquals(usdExchangeRate.getEffectiveDate(), usdToPLnRateDto.getEffectiveDate());
        assertEquals(usdToAfnRateDto.getEffectiveDate(), afnExchangeRate.getEffectiveDate());
        assertEquals(afnToPLnRateDto.getEffectiveDate(), afnExchangeRate.getEffectiveDate());
    }



}