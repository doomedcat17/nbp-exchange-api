package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRatesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rates")
public class RatesController {

    private final ExchangeRatesService exchangeRatesService;

    public RatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("/{currencyCode}/recent")
    public ResponseEntity<ExchangeRateDTO> recentRates(@PathVariable(name = "currencyCode") String currencyCode) {
        currencyCode = currencyCode.toUpperCase();
        return ResponseEntity.of(exchangeRatesService.getRecentExchangeRatesForCode(currencyCode));
    }

    @GetMapping("/{currencyCode}/all")
    public ResponseEntity<ExchangeRateDTO> allRates(@PathVariable(name = "currencyCode") String currencyCode,
                                                    @RequestParam(name = "effectiveDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {
        currencyCode = currencyCode.toUpperCase();
        return ResponseEntity.of(exchangeRatesService. getAllExchangeRatesForCodeAndDate(currencyCode, effectiveDate));
    }


    @GetMapping("/{sourceCurrencyCode}/{targetCurrencyCode}/recent")
    public ResponseEntity<ExchangeRateDTO> recentRateForCode(@PathVariable(name = "sourceCurrencyCode") String sourceCurrencyCode,
                                        @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode) {
        sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return ResponseEntity.of(exchangeRatesService.getRecentExchangeRate(sourceCurrencyCode, targetCurrencyCode));
    }

    @GetMapping("/{sourceCurrencyCode}/{targetCurrencyCode}/{date}")
    public ResponseEntity<ExchangeRateDTO> rateForCodeAndDate(@PathVariable(name = "sourceCurrencyCode") String sourceCurrencyCode,
                                        @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode,
                                                @PathVariable(name = "date") String date) {
        sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return ResponseEntity.of(exchangeRatesService.getExchangeRateForCodeAndDate(sourceCurrencyCode, targetCurrencyCode, date));
    }

    @GetMapping("/{currencyCode}/{targetCurrencyCode}/all")
    public ResponseEntity<ExchangeRateDTO> allExchangeRatesForCodes(@PathVariable(name = "currencyCode") String currencyCode,
                                              @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode) {
        currencyCode = currencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();
        return ResponseEntity.of(exchangeRatesService.getExchangeRatesForCodes(currencyCode, targetCurrencyCode));
    }
}
