package com.doomedcat17.nbpexchangeapi;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestDataProvider {

    public static String jsonStringFromFile(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public static List<NbpExchangeRate> sampleExchangeRates() {
        Currency usdCurrency =
                new Currency("USD", "Dolar amerykański");
        Currency audCurrency =
                new Currency("AUD", "Dolar australijski");
        Currency jpyCurrency =
                new Currency("JPY", "Jen japoński");
        Currency afnCurrency =
                new Currency("AFN", "afgani (Afganistan)");
        Currency plnCurrency =
                new Currency("PLN", "Polski złoty");
        List<NbpExchangeRate> exchangeRates = new ArrayList<>();
        LocalDate date = LocalDate.parse("2021-11-30");
        while (date.isAfter(LocalDate.parse("2021-11-18"))) {
            if (!date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                exchangeRates.addAll(
                        List.of(
                                new NbpExchangeRate(jpyCurrency, new BigDecimal("0.36"), date),
                                new NbpExchangeRate(usdCurrency, new BigDecimal("4.09"), date),
                                new NbpExchangeRate(audCurrency, new BigDecimal("3.00"), date),
                                new NbpExchangeRate(plnCurrency, new BigDecimal("1.00"), date))
                );
            }
            date = date.minusDays(1);
        }

        exchangeRates.addAll(
                List.of(
                        new NbpExchangeRate(usdCurrency, new BigDecimal("4.20"), LocalDate.parse("2021-07-02")),
                        new NbpExchangeRate(usdCurrency, new BigDecimal("4.09"), LocalDate.parse("2021-09-02")),
                        new NbpExchangeRate(afnCurrency, new BigDecimal("0.044308"), LocalDate.parse("2021-11-25")),
                        new NbpExchangeRate(audCurrency, new BigDecimal("3.00"), LocalDate.parse("2021-09-02"))
                ));
        return exchangeRates;
    }
}
