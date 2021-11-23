package com.doomedcat17.nbpexchangeapi.data.nbp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NbpCurrency {

    private String name;

    private BigDecimal midRateInPLN;

    private LocalDate effectiveDate;
}
