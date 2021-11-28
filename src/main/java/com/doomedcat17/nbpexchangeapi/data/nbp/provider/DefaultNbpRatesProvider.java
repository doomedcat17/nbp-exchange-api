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
            JsonNode table = nbpTableProvider.getTableFromDate(tableName, date);
            exchangeRates.addAll(mapTable(table));
        }
        return exchangeRates;
    }


    //work days only!!
    @Override
    public List<NbpExchangeRate> getNbpExchangeRatesFromLastWeek(LocalDate now) throws IOException {
        LocalDate startDate = getStartDate(now);
        List<NbpExchangeRate> exchangeRates = new ArrayList<>();
        for (String tableName: tableNames) {
            ArrayNode tables = nbpTableProvider.getTableFromDates(tableName, startDate, now);
            exchangeRates.addAll(mapTables(tables));
        }
        return exchangeRates;
    }

    private LocalDate getStartDate(LocalDate now) {
        LocalDate startDate = now;
        int remainingDays = 7;
        do {
            if (!startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !startDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                remainingDays--;
            }
            startDate = startDate.minusDays(1);
        } while (remainingDays != 0);
        return startDate;
    }

    private List<NbpExchangeRate> mapTables(ArrayNode jsonTablesArray) {
        List<NbpExchangeRate> nbpExchangeRates = new ArrayList<>();
        jsonTablesArray.elements()
                .forEachRemaining(tableNode -> nbpExchangeRates.addAll(mapTable(tableNode)));
        return nbpExchangeRates;
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
