package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.NbpTableProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultNbpRatesProvider implements NbpRatesProvider {

    private final NbpTableProvider nbpTableProvider;

    private final Set<String> tableNames = Set.of("a", "b");

    @Override
    public List<NbpExchangeRate> getNbpExchangeRatesFromDate(LocalDate date) throws IOException {
        List<NbpExchangeRate> exchangeRates = new ArrayList<>();
        for (String tableName: tableNames) {
            JsonNode jsonTableObject = nbpTableProvider.getTableFromDate(tableName, date);
            exchangeRates.addAll(mapTable(jsonTableObject));
        }
        return exchangeRates;
    }

    @Override
    public List<NbpExchangeRate> getRecentNbpExchangeRates() throws IOException {
        return getNbpExchangeRatesFromDate(LocalDate.now());
    }

    //work days only!!
    @Override
    public List<NbpExchangeRate> getNbpExchangeRatesFromLastWeek(LocalDate now) throws IOException {
        LocalDate date = now;
        int remainingDays = 7;
        List<NbpExchangeRate> exchangeRates = new ArrayList<>();
        do {
            if (!date.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                exchangeRates.addAll(
                        getNbpExchangeRatesFromDate(date)
                );
                remainingDays--;
            }
            date = now.minusDays(1);
        } while (remainingDays != 0);
        return exchangeRates;

    }

    private List<NbpExchangeRate> mapTable(JsonNode jsonTableObject) {
        List<NbpExchangeRate> exchangeRates = new ArrayList<>();
        LocalDate tableEffectiveDate = LocalDate.parse(jsonTableObject.get("effectiveDate").asText());
        ArrayNode tableCurrencies = (ArrayNode) jsonTableObject.get("rates");
        tableCurrencies.forEach(jsonCurrency -> {
            NbpExchangeRate nbpExchangeRAte = NbpExchangeRate.applyJson(jsonCurrency);
            nbpExchangeRAte.setEffectiveDate(tableEffectiveDate);
            exchangeRates.add(nbpExchangeRAte);
        });
        return exchangeRates;
    }





    public DefaultNbpRatesProvider(NbpTableProvider nbpTableProvider) {
        this.nbpTableProvider = nbpTableProvider;
    }
}
