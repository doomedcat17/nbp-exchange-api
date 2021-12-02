package com.doomedcat17.nbpexchangeapi.services.nbp.provider.table;

import com.doomedcat17.nbpexchangeapi.services.nbp.client.NbpApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;


@Component
public class DefaultNbpTableProvider implements NbpTableProvider {

    private final String NBP_TABLES_RESOURCE_PATH = "exchangerates/tables/";

    private final NbpApiClient nbpApiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public JsonNode getRecentTable(String tableName) throws IOException {
        String responseBody = nbpApiClient.requestResource(NBP_TABLES_RESOURCE_PATH+tableName);
        return retriveTableFromBody(responseBody);
    }

    public JsonNode getTableFromDate(String tableName, LocalDate localDate) throws IOException {
        String responseBody = nbpApiClient.requestResource(NBP_TABLES_RESOURCE_PATH+tableName+"/"+localDate.toString());
        return retriveTableFromBody(responseBody);
    }

    @Override
    public ArrayNode getTableFromDates(String tableName, LocalDate startDate, LocalDate endDate) throws IOException {
        String responseBody = nbpApiClient.requestResource(
                NBP_TABLES_RESOURCE_PATH+tableName+"/"+startDate.toString()+"/"+endDate);
        return (ArrayNode) objectMapper.readTree(responseBody);
    }


    //table object is inside an array
    private JsonNode retriveTableFromBody(String responseBody) throws JsonProcessingException {
        ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(responseBody);
        return jsonArray.get(0);
    }

    public DefaultNbpTableProvider(NbpApiClient nbpApiClient) {
        this.nbpApiClient = nbpApiClient;
    }
}
