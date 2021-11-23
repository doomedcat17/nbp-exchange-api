package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.data.nbp.client.NbpApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;


public class DefaultNbpTableProvider implements NbpTableProvider {

    private final String NBP_TABLES_RESOURCE_PATH = "http://api.nbp.pl/api/exchangerates/tables/";

    private final NbpApiClient nbpApiClient;

    public JSONObject getTable(String tableName) throws IOException {
        String responseBody = nbpApiClient.requestResource(NBP_TABLES_RESOURCE_PATH);
        return retriveTableFromBody(responseBody);
    }

    //table object inside an array
    private JSONObject retriveTableFromBody(String responseBody) {
        JSONArray jsonArray = new JSONArray(responseBody);
        return (JSONObject) jsonArray.get(0);
    }

    public DefaultNbpTableProvider(NbpApiClient nbpApiClient) {
        this.nbpApiClient = nbpApiClient;
    }
}
