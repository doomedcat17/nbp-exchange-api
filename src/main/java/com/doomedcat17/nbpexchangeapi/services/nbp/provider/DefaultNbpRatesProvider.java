package com.doomedcat17.nbpexchangeapi.services.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.table.NbpTableProvider;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DefaultNbpRatesProvider implements NbpRatesProvider {

    private final NbpTableProvider nbpTableProvider;

    private final WorkWeekStartDateProvider workWeekStartDateProvider;

    private final Set<String> tableNames = Set.of("a", "b");

    //work days only!!
    @Override
    public Set<NbpExchangeRate> getNbpExchangeRatesFromLastWeek(LocalDate now){
        LocalDate startDate = workWeekStartDateProvider.get(now);
        Set<NbpExchangeRate> exchangeRates = new HashSet<>();
        try {
            for (String tableName : tableNames) {
                ArrayNode tables = nbpTableProvider.getTableFromDates(tableName, startDate, now);
                exchangeRates.addAll(mapTables(tables));
            }
        } catch (IOException ignored) {
        }
        return exchangeRates;
    }

    @Override
    public Set<NbpExchangeRate> getRecent() {
        Set<NbpExchangeRate> exchangeRates = new HashSet<>();
        try {
            for (String tableName : tableNames) {
                JsonNode table = nbpTableProvider.getRecentTable(tableName);
                exchangeRates.addAll(mapTable(table));
            }
        } catch (IOException ignored) {
        }
        return exchangeRates;
    }

    private Set<NbpExchangeRate> mapTables(ArrayNode jsonTablesArray) {
        Set<NbpExchangeRate> nbpExchangeRates = new HashSet<>();
        jsonTablesArray.elements()
                .forEachRemaining(tableNode -> nbpExchangeRates.addAll(mapTable(tableNode)));
        return nbpExchangeRates;
    }

    //adds NbpExchangeRate for PLN too
    private Set<NbpExchangeRate> mapTable(JsonNode jsonTableObject) {
        Set<NbpExchangeRate> exchangeRates = new HashSet<>();
        LocalDate tableEffectiveDate = LocalDate.parse(jsonTableObject.get("effectiveDate").asText());
        ArrayNode tableCurrencies = (ArrayNode) jsonTableObject.get("rates");
        tableCurrencies.forEach(jsonCurrency -> {
            NbpExchangeRate nbpExchangeRAte = NbpExchangeRate.applyJson(jsonCurrency);
            nbpExchangeRAte.setEffectiveDate(tableEffectiveDate);
            exchangeRates.add(nbpExchangeRAte);
        });
        exchangeRates.add(
                new NbpExchangeRate(
                        new Currency("PLN", "polski złoty"),
                        new BigDecimal("1.00"),
                        tableEffectiveDate
                )
        );
        return exchangeRates;
    }

    public DefaultNbpRatesProvider(NbpTableProvider nbpTableProvider, WorkWeekStartDateProvider workWeekStartDateProvider) {
        this.nbpTableProvider = nbpTableProvider;
        this.workWeekStartDateProvider = workWeekStartDateProvider;
    }
}
