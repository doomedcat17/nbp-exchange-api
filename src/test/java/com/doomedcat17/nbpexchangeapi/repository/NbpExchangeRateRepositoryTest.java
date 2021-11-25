package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NbpExchangeRateRepositoryTest {

    @Mock
    private NbpExchangeRateDAO nbpExchangeRateDAO;

    @InjectMocks
    private NbpExchangeRateRepository nbpExchangeRateRepository;

    @Test
    void shouldNotAddDuplicate() {
        //given
        Currency usdCurrency = new Currency();
        usdCurrency.setCode("USD");
        usdCurrency.setName("Dolar amerykański");

        NbpExchangeRate nbpExchangeRate = new NbpExchangeRate();
        nbpExchangeRate.setCurrency(usdCurrency);
        nbpExchangeRate.setMidRateInPLN(new BigDecimal("4.20"));
        nbpExchangeRate.setEffectiveDate(LocalDate.parse("2021-07-02"));

        when(nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate("USD", LocalDate.parse("2021-07-02")))
                .thenReturn(new NbpExchangeRate());

        //when
        nbpExchangeRateRepository.addExchangeRate(nbpExchangeRate);

        //then
        verify(nbpExchangeRateDAO, times(0)).save(any());

    }

}