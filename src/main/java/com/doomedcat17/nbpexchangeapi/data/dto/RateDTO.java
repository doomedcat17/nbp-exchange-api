package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RateDTO {

    private String code;

    private LocalDate effectiveDate;

    private BigDecimal rate;
}
