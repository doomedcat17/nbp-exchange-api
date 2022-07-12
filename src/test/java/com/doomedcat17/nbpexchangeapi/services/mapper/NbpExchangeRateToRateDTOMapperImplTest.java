package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NbpExchangeRateToRateDTOMapperImplTest {

    private final NbpExchangeRateToRateDTOMapperImpl nbpExchangeRateToRateDTOMapper =
            new NbpExchangeRateToRateDTOMapperImpl();

    @Test
    void shouldMapJPYToUSDRateAndReverse() {
        //given
        NbpExchangeRate baseExchangeRate = new NbpExchangeRate();
        baseExchangeRate.setCurrency(new Currency("USD", "Dolar amerykański"));
        baseExchangeRate.setMidRateInPLN(new BigDecimal("4.16"));
        baseExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-25"));

        NbpExchangeRate exchangeRateToMap = new NbpExchangeRate();
        exchangeRateToMap.setCurrency(new Currency("JPY", "Jen japoński"));
        exchangeRateToMap.setMidRateInPLN(new BigDecimal("0.036062"));
        exchangeRateToMap.setEffectiveDate(LocalDate.parse("2021-11-25"));

        //when
        RateDTO rateDTO = nbpExchangeRateToRateDTOMapper.mapToRate(exchangeRateToMap, baseExchangeRate);

        RateDTO reversedRateDTO = nbpExchangeRateToRateDTOMapper.mapToRate(baseExchangeRate, exchangeRateToMap);

        //then
        assertEquals(new BigDecimal("115.36"), rateDTO.getRate());
        assertEquals(new BigDecimal("0.008669"), reversedRateDTO.getRate());
    }

    @Test
    void shouldMapJPYToAUDRateAndReverse() {
        //given
        NbpExchangeRate baseExchangeRate = new NbpExchangeRate();
        baseExchangeRate.setCurrency(new Currency("AUD", "Dolar australijski"));
        baseExchangeRate.setMidRateInPLN(new BigDecimal("3.00"));
        baseExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-25"));

        NbpExchangeRate exchangeRateToMap = new NbpExchangeRate();
        exchangeRateToMap.setCurrency(new Currency("JPY", "Jen japoński"));
        exchangeRateToMap.setMidRateInPLN(new BigDecimal("0.036062"));
        exchangeRateToMap.setEffectiveDate(LocalDate.parse("2021-11-25"));

        //when
        RateDTO rateDTO = nbpExchangeRateToRateDTOMapper.mapToRate(exchangeRateToMap, baseExchangeRate);

        RateDTO reversedRateDTO = nbpExchangeRateToRateDTOMapper.mapToRate(baseExchangeRate, exchangeRateToMap);

        //then
        assertEquals(new BigDecimal("83.19"), rateDTO.getRate());
        assertEquals(new BigDecimal("0.012021"), reversedRateDTO.getRate());
    }

    @Test
    void shouldMapJPYToAFNRateAndReverse() {
        //given
        NbpExchangeRate baseExchangeRate = new NbpExchangeRate();
        baseExchangeRate.setCurrency(new Currency("AFN", "afgani (Afganistan)"));
        baseExchangeRate.setMidRateInPLN(new BigDecimal("0.044308"));
        baseExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-25"));

        NbpExchangeRate exchangeRateToMap = new NbpExchangeRate();
        exchangeRateToMap.setCurrency(new Currency("JPY", "Jen japoński"));
        exchangeRateToMap.setMidRateInPLN(new BigDecimal("0.036062"));
        exchangeRateToMap.setEffectiveDate(LocalDate.parse("2021-11-25"));

        //when
        RateDTO rateDTO = nbpExchangeRateToRateDTOMapper.mapToRate(exchangeRateToMap, baseExchangeRate);

        RateDTO reversedRateDTO = nbpExchangeRateToRateDTOMapper.mapToRate(baseExchangeRate, exchangeRateToMap);

        //then
        assertEquals(new BigDecimal("1.228662"), rateDTO.getRate());
        assertEquals(new BigDecimal("0.813894"), reversedRateDTO.getRate());
    }



}