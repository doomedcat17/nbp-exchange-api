package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRateDtoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rates")
public class RatesController {

    private final ExchangeRateDtoService exchangeRateDtoService;

    public RatesController(ExchangeRateDtoService exchangeRateDtoService) {
        this.exchangeRateDtoService = exchangeRateDtoService;
    }

    @GetMapping("/{currencyCode}/recent")
    public ExchangeRateDTO recentRates(@PathVariable(name = "currencyCode") String currencyCode) {
        currencyCode = currencyCode.toUpperCase();
        return exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);
    }

    @GetMapping("/{currencyCode}/all")
    public ExchangeRateDTO allRates(@PathVariable(name = "currencyCode") String currencyCode,
                                                    @RequestParam(name = "effectiveDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {
        currencyCode = currencyCode.toUpperCase();
        return exchangeRateDtoService. getAllExchangeRatesForCodeAndDate(currencyCode, effectiveDate);
    }


    @GetMapping("/{sourceCurrencyCode}/{targetCurrencyCode}/recent")
    public ExchangeRateDTO recentRateForCode(@PathVariable(name = "sourceCurrencyCode") String sourceCurrencyCode,
                                        @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode) {
        sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return exchangeRateDtoService.getRecentExchangeRate(sourceCurrencyCode, targetCurrencyCode);
    }

    @GetMapping("/{sourceCurrencyCode}/{targetCurrencyCode}/{date}")
    public ExchangeRateDTO rateForCodeAndDate(@PathVariable(name = "sourceCurrencyCode") String sourceCurrencyCode,
                                        @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode,
                                                @PathVariable(name = "date") String date) {
        sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return exchangeRateDtoService.getExchangeRateForCodeAndDate(sourceCurrencyCode, targetCurrencyCode, date);
    }

    @GetMapping("/{currencyCode}/{targetCurrencyCode}/all")
    public ExchangeRateDTO allExchangeRatesForCodes(@PathVariable(name = "currencyCode") String currencyCode,
                                              @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode) {
        currencyCode = currencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return exchangeRateDtoService.getExchangeRatesForCodes(currencyCode, targetCurrencyCode);
    }
}
