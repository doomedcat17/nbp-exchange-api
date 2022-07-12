package com.doomedcat17.nbpexchangeapi.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

import java.net.URI;

public class CurrencyNotFoundException extends AbstractThrowableProblem {
    public CurrencyNotFoundException(String code) {
        super(null, "Bad request", Status.BAD_REQUEST, "Currency not found for code "+code);
    }

}
