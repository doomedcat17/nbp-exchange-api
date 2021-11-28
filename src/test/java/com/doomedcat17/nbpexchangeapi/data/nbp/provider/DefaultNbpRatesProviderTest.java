package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.DefaultNbpTableProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DefaultNbpRatesProviderTest {

    @Mock
    private DefaultNbpTableProvider tableProvider;

    @InjectMocks
    private DefaultNbpRatesProvider npbCurrencyProvider;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldProvideAllNbpCurrencies() throws IOException {
        //given
        String tableAJson = TestDataProvider.jsonStringFromFile("src/test/resources/npb_table_a.json");
        Mockito.when(
                tableProvider.getTable("a")
        ).thenReturn(objectMapper.readTree(tableAJson));

        String tableBJson = TestDataProvider.jsonStringFromFile("src/test/resources/npb_table_a.json");
        Mockito.when(
                tableProvider.getTable("b")
        ).thenReturn(objectMapper.readTree(tableBJson));

        //when
        List<NbpExchangeRate> nbpCurrencies = npbCurrencyProvider.getRecentNbpExchangeRates();
        //then
        assertEquals(70, nbpCurrencies.size());
    }


}