package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.nbp.NbpCurrency;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.NbpTableProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultNpbCurrencyProvider implements NpbCurrencyProvider{

    private final NbpTableProvider nbpTableProvider;

    private final Set<String> tableNames = Set.of("a", "b");

    @Override
    public List<NbpCurrency> getNbpCurrencies() throws IOException {
        List<NbpCurrency> nbpCurrencies = new ArrayList<>();
        for (String tableName: tableNames) {
            JsonNode jsonTableObject = nbpTableProvider.getTable(tableName);
            nbpCurrencies.addAll(mapTable(jsonTableObject));
        }
        return nbpCurrencies;
    }

    private List<NbpCurrency> mapTable(JsonNode jsonTableObject) {
        List<NbpCurrency> nbpCurrencies = new ArrayList<>();
        LocalDate tableEffectiveDate = LocalDate.parse(jsonTableObject.get("effectiveDate").asText());
        ArrayNode tableCurrencies = (ArrayNode) jsonTableObject.get("rates");
        tableCurrencies.forEach(jsonCurrency -> {
            NbpCurrency nbpCurrency = NbpCurrency.applyJson(jsonCurrency);
            nbpCurrency.setEffectiveDate(tableEffectiveDate);
            nbpCurrencies.add(nbpCurrency);
        });
        return nbpCurrencies;
    }





    public DefaultNpbCurrencyProvider(NbpTableProvider nbpTableProvider) {
        this.nbpTableProvider = nbpTableProvider;
    }
}
