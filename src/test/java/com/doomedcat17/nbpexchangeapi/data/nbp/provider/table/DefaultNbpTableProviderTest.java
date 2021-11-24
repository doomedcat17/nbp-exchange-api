package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.data.nbp.client.DefaultNbpApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNbpTableProviderTest {

    private NbpTableProvider nbpTableProvider = new DefaultNbpTableProvider(
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

}