package com.doomedcat17.nbpexchangeapi.data.nbp.provider.table;

import org.json.JSONObject;

import java.io.IOException;

public interface NbpTableProvider {

    JSONObject getTable(String tableName) throws IOException;
}
