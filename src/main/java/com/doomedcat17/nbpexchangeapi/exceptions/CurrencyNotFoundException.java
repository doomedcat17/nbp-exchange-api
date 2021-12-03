package com.doomedcat17.nbpexchangeapi.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String code) {
        super("Currency not found for code "+code);
    }
}
