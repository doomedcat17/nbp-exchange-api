package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.nbp.NbpCurrency;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.NbpTableProvider;
import org.json.JSONArray;
import org.json.JSONObject;

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
            JSONObject jsonTableObject = nbpTableProvider.getTable(tableName);
            nbpCurrencies.addAll(mapTable(jsonTableObject));
        }
        return nbpCurrencies;
    }

    private List<NbpCurrency> mapTable(JSONObject jsonTableObject) {
        List<NbpCurrency> nbpCurrencies = new ArrayList<>();
        LocalDate tableEffectiveDate = LocalDate.parse(jsonTableObject.getString("effectiveDate"));
        JSONArray tableCurrencies = jsonTableObject.getJSONArray("rates");
        tableCurrencies.forEach(object -> {
            JSONObject jsonCurrencyObject = (JSONObject) object;
            NbpCurrency nbpCurrency = NbpCurrency.applyJson(jsonCurrencyObject);
            nbpCurrency.setEffectiveDate(tableEffectiveDate);
            nbpCurrencies.add(nbpCurrency);
        });
        return nbpCurrencies;
    }





    public DefaultNpbCurrencyProvider(NbpTableProvider nbpTableProvider) {
        this.nbpTableProvider = nbpTableProvider;
    }
}
