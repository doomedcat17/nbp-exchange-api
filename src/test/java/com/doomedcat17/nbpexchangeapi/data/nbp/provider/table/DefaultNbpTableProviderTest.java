package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.services.nbp.client.DefaultNbpApiClient;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.table.DefaultNbpTableProvider;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.table.NbpTableProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNbpTableProviderTest {

    private final NbpTableProvider nbpTableProvider = new DefaultNbpTableProvider(
            new DefaultNbpApiClient()
    );

    @Test
    void shouldProvideNbpTable() {
        try {
            //when
            JsonNode responseJson = nbpTableProvider.getTable("a");
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
    void shouldProvideNbpTableFromDate() {
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
    void shouldProvideNbpTableFromDates() {
        //given
        LocalDate startDate = LocalDate.parse("2021-11-18");
        LocalDate endDate = LocalDate.parse("2021-11-25");
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