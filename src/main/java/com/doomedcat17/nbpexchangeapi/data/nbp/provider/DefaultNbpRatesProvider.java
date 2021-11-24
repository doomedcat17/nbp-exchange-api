package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.nbp.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.NbpTableProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultNbpRatesProvider implements NbpRatesProvider {

    private final NbpTableProvider nbpTableProvider;

    private final Set<String> tableNames = Set.of("a", "b");

    @Override
    public List<NbpExchangeRate> getNbpCurrencies() throws IOException {
        List<NbpExchangeRate> nbpCurrencies = new ArrayList<>();
        for (String tableName: tableNames) {
            JsonNode jsonTableObject = nbpTableProvider.getTable(tableName);
            nbpCurrencies.addAll(mapTable(jsonTableObject));
        }
        return nbpCurrencies;
    }

    private List<NbpExchangeRate> mapTable(JsonNode jsonTableObject) {
        List<NbpExchangeRate> nbpCurrencies = new ArrayList<>();
        LocalDate tableEffectiveDate = LocalDate.parse(jsonTableObject.get("effectiveDate").asText());
        ArrayNode tableCurrencies = (ArrayNode) jsonTableObject.get("rates");
        tableCurrencies.forEach(jsonCurrency -> {
            NbpExchangeRate nbpExchangeRAte = NbpExchangeRate.applyJson(jsonCurrency);
            nbpExchangeRAte.setEffectiveDate(tableEffectiveDate);
            nbpCurrencies.add(nbpExchangeRAte);
        });
        return nbpCurrencies;
    }





    public DefaultNbpRatesProvider(NbpTableProvider nbpTableProvider) {
        this.nbpTableProvider = nbpTableProvider;
    }
}
