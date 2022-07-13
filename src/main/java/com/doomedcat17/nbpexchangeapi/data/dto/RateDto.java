package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RateDto {

    private LocalDate effectiveDate;
    private String code;
    private String targetCode;
    private BigDecimal rate;
}
