package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.DefaultNbpTableProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
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
    void shouldProvideAllExchangeRatesFromLastWorkWeek() throws IOException {
        //given
        String tablesAJson = TestDataProvider.jsonStringFromFile("src/test/resources/nbp_tables_a_2021-11-17_2021-11-26.json");
        Mockito.when(
                tableProvider.getTableFromDates("a", LocalDate.parse("2021-11-17"), LocalDate.parse("2021-11-26"))
        ).thenReturn((ArrayNode) objectMapper.readTree(tablesAJson));

        String tablesBJson = TestDataProvider.jsonStringFromFile("src/test/resources/nbp_tables_b_2021-11-17_2021-11-26.json");
        Mockito.when(
                tableProvider.getTableFromDates("b", LocalDate.parse("2021-11-17"), LocalDate.parse("2021-11-26"))
        ).thenReturn((ArrayNode) objectMapper.readTree(tablesBJson));

        //when
        List<NbpExchangeRate> nbpExchangeRates = npbCurrencyProvider.getNbpExchangeRatesFromLastWeek(LocalDate.parse("2021-11-26"));
        //then
        assertEquals(510, nbpExchangeRates.size());
    }


}