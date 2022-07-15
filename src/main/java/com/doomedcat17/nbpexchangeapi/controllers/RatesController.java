package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.PageDto;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDto;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRateDtoService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/rates")
@AllArgsConstructor
public class RatesController {

    private final ExchangeRateDtoService exchangeRateDtoService;

    @GetMapping("/recent")
    public PageDto<RateDto> recentRates(@RequestParam(name = "currency") String currencyCode,
                                        @RequestParam(name = "targetCurrency", required = false) String targetCurrencyCode) {
        currencyCode = currencyCode.toUpperCase();
        if (Objects.nonNull(targetCurrencyCode) && !targetCurrencyCode.isBlank()) {
            return exchangeRateDtoService.getRecentExchangeRate(currencyCode, targetCurrencyCode.toUpperCase());
        } else return exchangeRateDtoService.getRecentExchangeRatesForCode(currencyCode);
    }

    @GetMapping()
    public PageDto<RateDto> allRates(@RequestParam(name = "currency") String currencyCode,
                                     @RequestParam(name = "targetCurrency", required = false) String targetCurrencyCode,
                                     @RequestParam(name = "effectiveDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate,
                                     @RequestParam(name = "page", defaultValue = "1") int pageNum) {
        currencyCode = currencyCode.toUpperCase();
        if (pageNum <= 0) pageNum = 1;
        if (Objects.nonNull(targetCurrencyCode) && !targetCurrencyCode.isBlank()) {
            return exchangeRateDtoService.getExchangeRatesForCodes(currencyCode, targetCurrencyCode, effectiveDate, pageNum);
        } else return exchangeRateDtoService.getAllExchangeRatesForCodeAndDate(currencyCode, effectiveDate, pageNum);
    }
}
