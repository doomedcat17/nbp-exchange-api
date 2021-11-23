package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.data.nbp.client.DefaultNbpApiClient;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNbpTableProviderTest {

    private NbpTableProvider nbpTableProvider = new DefaultNbpTableProvider(
            new DefaultNbpApiClient()
    );

    @Test
    void shouldProvideNbpTable() throws IOException {
        //when
        JSONObject jsonObject = nbpTableProvider.getTable("a");
        //then
        System.out.println("meow");
    }

}