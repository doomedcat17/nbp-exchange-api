package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRatesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rates")
public class RatesController {

    private final ExchangeRatesService exchangeRatesService;

    @GetMapping("/{currencyCode}/{targetCurrencyCode}")
    public ExchangeRateDTO recentRatesForCode(@PathVariable(name = "currencyCode") String currencyCode,
                                              @PathVariable(name = "targetCurrencyCode") String targetCurrencyCode,
                                              @RequestParam(name = "effectiveDate", required = false) String effectiveDate) {
        if (!effectiveDate.isBlank()) return exchangeRatesService.getRecentRatesByCodeAndDate(currencyCode, effectiveDate);
        else return exchangeRatesService.getRecentRatesByCode(currencyCode);
    }

    public RatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }
}
