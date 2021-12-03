package com.doomedcat17.nbpexchangeapi.exceptions;

public class MissingRequestParameterException extends RuntimeException {
    public MissingRequestParameterException(String parameter) {
        super("Missing parameter "+parameter);
    }
}
