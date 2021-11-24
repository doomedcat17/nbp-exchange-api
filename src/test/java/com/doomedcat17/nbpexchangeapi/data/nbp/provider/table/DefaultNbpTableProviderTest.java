package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.data.nbp.client.DefaultNbpApiClient;
import org.json.JSONObject;
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
            JSONObject responseJson = nbpTableProvider.getTable("a");
            //then
            assertEquals("A", responseJson.getString("table"));
            assertTrue(responseJson.getJSONArray("rates").length() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}