package com.doomedcat17.nbpexchangeapi.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class CurrencyNotFoundException extends AbstractThrowableProblem {
    public CurrencyNotFoundException(String code) {
        super(null, "Bad request", Status.BAD_REQUEST, "Currency not found for code "+code);
    }

}
