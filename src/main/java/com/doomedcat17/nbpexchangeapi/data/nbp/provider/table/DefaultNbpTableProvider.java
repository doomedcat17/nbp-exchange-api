package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.data.nbp.client.NbpApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.time.LocalDate;


public class DefaultNbpTableProvider implements NbpTableProvider {

    private final String NBP_TABLES_RESOURCE_PATH = "exchangerates/tables/";

    private final NbpApiClient nbpApiClient;

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getTable(String tableName) throws IOException {
        String responseBody = nbpApiClient.requestResource(NBP_TABLES_RESOURCE_PATH+tableName);
        return retrieveTableFromBody(responseBody);
    }

    @Override
    public JsonNode getTableByDate(String tableName, LocalDate date) {
        return null;
    }

    //table object inside an array
    private JsonNode retrieveTableFromBody(String responseBody) throws JsonProcessingException {
        ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(responseBody);
        return jsonArray.get(0);
    }

    public DefaultNbpTableProvider(NbpApiClient nbpApiClient) {
        this.nbpApiClient = nbpApiClient;
    }
}
