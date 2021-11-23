package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class DefaultNbpTableProvider implements NbpTableProvider {

    private final String NBP_TABLE_URL = "http://api.nbp.pl/api/exchangerates/tables/";

    public JSONObject getTable(String tableName) throws IOException {
        URL nbpUrl = new URL(NBP_TABLE_URL+tableName);
        HttpURLConnection connection = (HttpURLConnection) nbpUrl.openConnection();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (InputStream requestInputStream = connection.getInputStream()) {
                BufferedReader responseBodyReader =
                        new BufferedReader(
                                new InputStreamReader(requestInputStream, StandardCharsets.UTF_8)
                        );
                String responseBody = readBody(responseBodyReader);
                return retriveTableFromBody(responseBody);
            }
        } else throw new IOException();
    }

    private String readBody(BufferedReader responseBodyReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = responseBodyReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    //table object inside an array
    private JSONObject retriveTableFromBody(String responseBody) {
        JSONArray jsonArray = new JSONArray(responseBody);
        return (JSONObject) jsonArray.get(0);
    }


}
