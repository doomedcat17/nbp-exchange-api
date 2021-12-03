package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRatesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rates")
public class RatesController {

    private final ExchangeRatesService exchangeRatesService;

    @GetMapping("/{currencyCode}/recent")
    public ExchangeRateDTO recentRates(@PathVariable(name = "currencyCode") String currencyCode) {
        currencyCode = currencyCode.toUpperCase();
        return exchangeRatesService.getRecentExchangeRatesForCode(currencyCode);
    }

    @GetMapping("/{currencyCode}/all")
    public ExchangeRateDTO allRates(@PathVariable(name = "currencyCode") String currencyCode,
                                    @RequestParam(name = "effectiveDate", required = false, defaultValue = "") String effectiveDate) {
        currencyCode = currencyCode.toUpperCase();
        return exchangeRatesService. getAllExchangeRatesForCodeAndDate(currencyCode, effectiveDate);
    }


    @GetMapping("/{sourceCurrencyCode}/{targetCurrencyCode}/recent")
    public ExchangeRateDTO recentRateForCode(@PathVariable(name = "sourceCurrencyCode") String sourceCurrencyCode,
                                        @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode) {
        sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return exchangeRatesService.getRecentExchangeRate(sourceCurrencyCode, targetCurrencyCode);
    }

    @GetMapping("/{sourceCurrencyCode}/{targetCurrencyCode}/{date}")
    public ExchangeRateDTO rateForCodeAndDate(@PathVariable(name = "sourceCurrencyCode") String sourceCurrencyCode,
                                        @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode,
                                                @PathVariable(name = "date") String date) {
        sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return exchangeRatesService.getExchangeRateForCodeAndDate(sourceCurrencyCode, targetCurrencyCode, date);
    }

    @GetMapping("/{currencyCode}/{targetCurrencyCode}/all")
    public ExchangeRateDTO allExchangeRatesForCodes(@PathVariable(name = "currencyCode") String currencyCode,
                                              @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode) {
        currencyCode = currencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return exchangeRatesService.getExchangeRatesForCodes(currencyCode, targetCurrencyCode);
    }

    public RatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }
}
