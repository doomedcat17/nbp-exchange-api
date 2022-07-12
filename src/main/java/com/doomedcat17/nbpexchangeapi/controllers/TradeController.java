package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.SellRequestDto;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.exceptions.MissingRequestParameterException;
import com.doomedcat17.nbpexchangeapi.services.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<List<TransactionDto>> getAllTransactionsFromDate(@PathVariable(name = "date") String date) {
        List<TransactionDto> transactions = tradeService.getTransactionsFromGivenDate(date);
        if (transactions.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/history/{startDate}/{endDate}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsFromDates(@PathVariable(name = "startDate") String startDate,
                                                            @PathVariable(name = "endDate") String endDate) {
        List<TransactionDto> transactions = tradeService.getTransactionsFromGivenDates(startDate, endDate);
        if (transactions.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping
    public TransactionDto tradePOST(@RequestBody SellRequestDto sellRequestDto) {
        return tradeService.buyCurrency(sellRequestDto.getBuyCode().toUpperCase(),
                sellRequestDto.getSellCode().toUpperCase(), new BigDecimal(sellRequestDto.getBuyAmount()));
    }
}
