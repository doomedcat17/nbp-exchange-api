package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.services.nbp.client.DefaultNbpApiClient;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.table.DefaultNbpTableProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DefaultNbpTableProviderTest {

    @Mock
    private DefaultNbpApiClient nbpApiClient;

    @InjectMocks
    private DefaultNbpTableProvider nbpTableProvider;

    @Test
    void shouldProvideNbpTable() throws IOException {

        //given
        Mockito.when(nbpApiClient.requestResource("exchangerates/tables/a"))
                .thenReturn(
                        TestDataProvider
                                .jsonStringFromFile("src/test/resources/npb_table_a.json")
                );


        try {
            //when
            JsonNode responseJson = nbpTableProvider.getRecentTable("a");

            //then
            assertEquals("A", responseJson.get("table").asText());
            ArrayNode jsonArray = (ArrayNode) responseJson.get("rates");
            assertFalse(jsonArray.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void shouldProvideNbpTableFromDate() throws IOException {

        //given
        Mockito.when(nbpApiClient.requestResource("exchangerates/tables/a/2021-11-25"))
                .thenReturn(
                        TestDataProvider
                                .jsonStringFromFile("src/test/resources/npb_table_a.json")
                );

        try {
            //when
            JsonNode responseJson = nbpTableProvider.getTableFromDate("a", LocalDate.parse("2021-11-25"));

            //then
            assertEquals("A", responseJson.get("table").asText());
            assertEquals("2021-11-25", responseJson.get("effectiveDate").asText());
            ArrayNode jsonArray = (ArrayNode) responseJson.get("rates");
            assertFalse(jsonArray.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void shouldProvideNbpTablesFromDates() throws IOException {

        //given
        Mockito.when(nbpApiClient.requestResource("exchangerates/tables/a/2021-11-18/2021-11-26"))
                .thenReturn(
                        TestDataProvider
                                .jsonStringFromFile("src/test/resources/nbp_tables_a_2021-11-18_2021-11-26.json")
                );

        LocalDate startDate = LocalDate.parse("2021-11-18");
        LocalDate endDate = LocalDate.parse("2021-11-26");
        try {
            //when
            ArrayNode responseJson = nbpTableProvider.getTableFromDates("a", startDate, endDate);

            //then
            responseJson.forEach(table -> {
                assertEquals("A", table.get("table").asText());
                LocalDate tableDate = LocalDate.parse(table.get("effectiveDate").asText());
                assertAll("Checking if dates are in given range",
                        () -> assertTrue(startDate.isEqual(tableDate) || startDate.isBefore(tableDate)),
                        () -> assertTrue(endDate.isEqual(tableDate) || endDate.isAfter(tableDate)));
                ArrayNode jsonArray = (ArrayNode) table.get("rates");
                assertFalse(jsonArray.isEmpty());
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}