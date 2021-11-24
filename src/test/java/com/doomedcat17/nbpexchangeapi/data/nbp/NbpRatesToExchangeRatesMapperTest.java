package com.doomedcat17.nbpexchangeapi.data.nbp;

import com.doomedcat17.nbpexchangeapi.data.model.ExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.client.DefaultNbpApiClient;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.DefaultNbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.NbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.DefaultNbpTableProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NbpRatesToExchangeRatesMapperTest {

    private final NbpRatesToExchangeRatesMapper nbpRatesMapper = new NbpRatesToExchangeRatesMapper();

    @Test
    void shouldMapExchangeRates() throws IOException {
        //given
        NbpExchangeRate usdNbpExchangeRate = new NbpExchangeRate();
        usdNbpExchangeRate.setCode("USD");
        usdNbpExchangeRate.setMidRateInPLN(new BigDecimal("4.18"));
        usdNbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-24"));

        NbpExchangeRate audNbpExchangeRate = new NbpExchangeRate();
        audNbpExchangeRate.setCode("AUD");
        audNbpExchangeRate.setMidRateInPLN(new BigDecimal("3.01"));
        audNbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-11-24"));

        //when
        ExchangeRate exchangeRate1 = nbpRatesMapper.map(usdNbpExchangeRate, audNbpExchangeRate);
        ExchangeRate exchangeRate2 = nbpRatesMapper.map(audNbpExchangeRate, usdNbpExchangeRate);
        //then
        assertEquals(new BigDecimal("1.39"), exchangeRate1.getRate());
        assertEquals(new BigDecimal("0.72"), exchangeRate2.getRate());


        NbpRatesProvider nbpRatesProvider = new DefaultNbpRatesProvider(new DefaultNbpTableProvider(new DefaultNbpApiClient()));
        NbpRatesToExchangeRatesMapper nbpRatesToExchangeRatesMapper = new NbpRatesToExchangeRatesMapper();
        List<NbpExchangeRate> nbpExchangeRates = nbpRatesProvider.getNbpCurrencies();
        List<ExchangeRate> exchangeRates = nbpRatesToExchangeRatesMapper.mapList(nbpExchangeRates);
        System.out.println("meow");


    }

}