package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.SellRequestDto;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.exceptions.MissingRequestParameterException;
import com.doomedcat17.nbpexchangeapi.services.TradeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/{sellCurrencyCode}/{buyCurrencyCode}/{buyAmount}")
    public TransactionDto trade(@PathVariable(name = "sellCurrencyCode") String sellCurrencyCode,
                                            @PathVariable(name = "buyCurrencyCode") String buyCurrencyCode,
                                @PathVariable(name = "buyAmount") String buyAmount) {
        return tradeService.buyCurrency(buyCurrencyCode.toUpperCase(), sellCurrencyCode.toUpperCase(), new BigDecimal(buyAmount));
    }

    @PostMapping
    public TransactionDto tradePOST(@RequestBody SellRequestDto sellRequestDto) {
        String missingParameter = sellRequestDto.getEmptyParameterName();
        if (!missingParameter.isBlank()) throw new MissingRequestParameterException(missingParameter);
        return tradeService.buyCurrency(sellRequestDto.getBuyCode().toUpperCase(),
                sellRequestDto.getSellCode().toUpperCase(), new BigDecimal(sellRequestDto.getBuyAmount()));
    }

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }
}
