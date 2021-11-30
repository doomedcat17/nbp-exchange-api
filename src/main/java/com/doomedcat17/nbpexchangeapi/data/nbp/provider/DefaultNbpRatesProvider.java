package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.table.NbpTableProvider;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DefaultNbpRatesProvider implements NbpRatesProvider {

    private final NbpTableProvider nbpTableProvider;

    private final WorkWeekStartDateProvider workWeekStartDateProvider;

    private final Set<String> tableNames = Set.of("a", "b");

    //work days only!!
    @Override
    public List<NbpExchangeRate> getNbpExchangeRatesFromLastWeek(LocalDate now){
        LocalDate startDate = workWeekStartDateProvider.get(now);
        List<NbpExchangeRate> exchangeRates = new ArrayList<>();
        try {
            for (String tableName : tableNames) {
                ArrayNode tables = nbpTableProvider.getTableFromDates(tableName, startDate, now);
                exchangeRates.addAll(mapTables(tables));
            }
        } catch (IOException ignored) {
        }
        return exchangeRates;
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

    public DefaultNbpRatesProvider(NbpTableProvider nbpTableProvider, WorkWeekStartDateProvider workWeekStartDateProvider) {
        this.nbpTableProvider = nbpTableProvider;
        this.workWeekStartDateProvider = workWeekStartDateProvider;
    }
}
