package com.doomedcat17.nbpexchangeapi.services.nbp.provider.table;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.time.LocalDate;

public interface NbpTableProvider {

    JsonNode getTable(String tableName) throws IOException;

    JsonNode getTableFromDate(String tableName, LocalDate localDate) throws IOException;

    ArrayNode getTableFromDates(String tableName, LocalDate startDate, LocalDate endDate) throws IOException;
}
