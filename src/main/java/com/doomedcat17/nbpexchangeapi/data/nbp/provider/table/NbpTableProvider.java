package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDate;

public interface NbpTableProvider {

    JsonNode getTable(String tableName) throws IOException;

    JsonNode getTableByDate(String tableName, LocalDate date);
}
