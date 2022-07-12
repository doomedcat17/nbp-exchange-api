package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.mapper.NbpExchangeRateMapper;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.DefaultNbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.table.DefaultNbpTableProvider;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DefaultNbpRatesProviderTest {

    @Mock
    private DefaultNbpTableProvider tableProvider;

    @Mock
    private WorkWeekStartDateProvider workWeekStartDateProvider;

    @Spy
    NbpExchangeRateMapper mapper = NbpExchangeRateMapper.INSTANCE;

    @InjectMocks
    private DefaultNbpRatesProvider npbCurrencyProvider;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldProvideAllExchangeRatesFromLastWorkWeek() throws IOException {
        //given
        Mockito.when(workWeekStartDateProvider.get(LocalDate.parse("2021-11-26")))
                .thenReturn(LocalDate.parse("2021-11-18"));

        String tablesAJson = TestDataProvider.jsonStringFromFile("src/test/resources/nbp_tables_a_2021-11-18_2021-11-26.json");

        ArrayNode aTables = (ArrayNode) objectMapper.readTree(tablesAJson);
        Mockito.when(
                tableProvider.getTableFromDates("a", LocalDate.parse("2021-11-18"), LocalDate.parse("2021-11-26"))
        ).thenReturn(aTables);

        String tablesBJson = TestDataProvider.jsonStringFromFile("src/test/resources/nbp_tables_b_2021-11-18_2021-11-26.json");

        ArrayNode bTables = (ArrayNode) objectMapper.readTree(tablesBJson);
        Mockito.when(
                tableProvider.getTableFromDates("b", LocalDate.parse("2021-11-18"), LocalDate.parse("2021-11-26"))
        ).thenReturn(bTables);

        //when
        Set<NbpExchangeRate> nbpExchangeRates = npbCurrencyProvider.getNbpExchangeRatesFromLastWeek(LocalDate.parse("2021-11-26"));
        //then
        assertEquals(
                7,
                nbpExchangeRates.stream().filter(nbpExchangeRate -> nbpExchangeRate.getCurrency().getCode().equals("PLN")).count()
        );
        assertEquals(aTables.size()*35+bTables.size()*115+7, nbpExchangeRates.size());
    }


}