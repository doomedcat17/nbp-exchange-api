package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rates")
public class RatesController {

    private ExchangeRatesService exchangeRatesService;

    @GetMapping("/{currencyCode}")
    public ExchangeRateDTO recentRatesForCode(@PathVariable(name = "currencyCode") String currencyCode) {
        return exchangeRatesService.getRecentRatesByCode(currencyCode);
    }

    public RatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }
}
