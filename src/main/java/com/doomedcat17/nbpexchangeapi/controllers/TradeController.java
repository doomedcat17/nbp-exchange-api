package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.PageDto;
import com.doomedcat17.nbpexchangeapi.data.dto.SellRequestDto;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.services.TradeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/{buyCurrencyCode}/{sellCurrencyCode}/{buyAmount}")
    public TransactionDto trade(@PathVariable(name = "sellCurrencyCode") String sellCurrencyCode,
                                @PathVariable(name = "buyCurrencyCode") String buyCurrencyCode,
                                @PathVariable(name = "buyAmount") String buyAmount) {
        return tradeService.buyCurrency(buyCurrencyCode.toUpperCase(), sellCurrencyCode.toUpperCase(), new BigDecimal(buyAmount));
    }

    @PostMapping
    public TransactionDto tradePost(@RequestBody SellRequestDto sellRequestDto) {
        return tradeService.buyCurrency(sellRequestDto.getBuyCode().toUpperCase(),
                sellRequestDto.getSellCode().toUpperCase(), new BigDecimal(sellRequestDto.getBuyAmount()));
    }

    @GetMapping()
    public PageDto<TransactionDto> getAllTransactionsFromDates(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                               @RequestParam(name = "page", defaultValue = "1") int pageNum) {
        if (pageNum <= 0) pageNum = 1;
        return tradeService.getTransactionsFromGivenDates(startDate, endDate, pageNum);
    }
}
