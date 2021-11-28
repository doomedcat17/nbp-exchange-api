package com.doomedcat17.nbpexchangeapi.services;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesServiceTest {

    @Mock
    private NbpExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRatesService exchangeRatesService;

    @Test
    void shouldReturnExchangeRateDto() {
        //given
        NbpExchangeRate nbpExchangeRate = new NbpExchangeRate();
        nbpExchangeRate.setCurrency(new Currency("USD", "Dolar amerykański"));
        nbpExchangeRate.setMidRateInPLN(new BigDecimal("4.16"));
        nbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-25"));

        String code = "USD";

        when(exchangeRateRepository
                .getMostRecentByCode(code))
                .thenReturn(nbpExchangeRate);
        when(exchangeRateRepository
                .getMostRecent())
                .thenReturn(testData());

        //when
        ExchangeRateDTO exchangeRateDTO = exchangeRatesService.getMostRecentRates(code);
        //then
        assertEquals(code, exchangeRateDTO.getCode());
        assertEquals(3, exchangeRateDTO.getRates().size());
        Set<RateDTO> rates = exchangeRateDTO.getRates();
        RateDTO audRate = rates.stream()
                .filter(rateDTO -> rateDTO.getCode().equals("AUD"))
                .findFirst().orElse(null);
        RateDTO jpyRate = rates.stream()
                .filter(rateDTO -> rateDTO.getCode().equals("JPY"))
                .findFirst().orElse(null);
        RateDTO gbpRate = rates.stream()
                .filter(rateDTO -> rateDTO.getCode().equals("GBP"))
                .findFirst().orElse(null);
        assertAll("Should all match",
                () -> assertEquals(new BigDecimal("115.37"), jpyRate.getRate()),
                () -> assertEquals(new BigDecimal("1.39"), audRate.getRate()),
                () -> assertEquals(new BigDecimal("0.75"), gbpRate.getRate())
        );

    }



    private Set<NbpExchangeRate> testData() {
        NbpExchangeRate nbpExchangeRate = new NbpExchangeRate();
        nbpExchangeRate.setCurrency(new Currency("USD", "Dolar amerykański"));
        nbpExchangeRate.setMidRateInPLN(new BigDecimal("4.17"));
        nbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-25"));

        NbpExchangeRate nbpExchangeRate2 = new NbpExchangeRate();
        nbpExchangeRate2.setCurrency(new Currency("AUD", "Dolar australijski"));
        nbpExchangeRate2.setMidRateInPLN(new BigDecimal("3.00"));
        nbpExchangeRate2.setEffectiveDate(LocalDate.parse("2021-11-25"));


        NbpExchangeRate nbpExchangeRate3 = new NbpExchangeRate();
        nbpExchangeRate3.setCurrency(new Currency("JPY", "Jen japoński"));
        nbpExchangeRate3.setMidRateInPLN(new BigDecimal("0.036"));
        nbpExchangeRate3.setEffectiveDate(LocalDate.parse("2021-11-25"));


        NbpExchangeRate nbpExchangeRate4 = new NbpExchangeRate();
        nbpExchangeRate4.setCurrency(new Currency("GBP", "Funt brytyjski"));
        nbpExchangeRate4.setMidRateInPLN(new BigDecimal("5.56"));
        nbpExchangeRate4.setEffectiveDate(LocalDate.parse("2021-11-25"));
        return Set.of(nbpExchangeRate, nbpExchangeRate2, nbpExchangeRate3, nbpExchangeRate4);
    }

}