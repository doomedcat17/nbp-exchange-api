package com.doomedcat17.nbpexchangeapi.services.mapper;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NbpExchangeRateToRateDTOMapperImplTest {

    @Mock
    private NbpExchangeRateRepository rateRepository;

    @InjectMocks
    private NbpExchangeRateToRateDTOMapperImpl nbpExchangeRateToRateDTOMapper;

    @Test
    void shouldMapExchangeRateToUSDRate() {
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

        //then
        assertEquals(new BigDecimal("115.36"), rateDTO.getRate());




    }

}