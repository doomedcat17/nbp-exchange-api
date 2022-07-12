package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.SellRequestDto;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.services.TradeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/history/{date}")
    public List<TransactionDto> getAllTransactionsFromDate(@PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TransactionDto> transactions = tradeService.getTransactionsFromGivenDate(date);
        if (transactions.isEmpty()) return List.of();
        return transactions;
    }

    @GetMapping("/history/{startDate}/{endDate}")
    public List<TransactionDto> getAllTransactionsFromDates(@PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionDto> transactions = tradeService.getTransactionsFromGivenDates(startDate, endDate);
        if (transactions.isEmpty()) return List.of();
        return transactions;
    }

    @PostMapping
    public TransactionDto tradePost(@RequestBody SellRequestDto sellRequestDto) {
        return tradeService.buyCurrency(sellRequestDto.getBuyCode().toUpperCase(),
                sellRequestDto.getSellCode().toUpperCase(), new BigDecimal(sellRequestDto.getBuyAmount()));
    }
}
