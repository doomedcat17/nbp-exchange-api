package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface NbpTableProvider {

    JsonNode getTable(String tableName) throws IOException;
}
