package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.services.TradeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }
}
