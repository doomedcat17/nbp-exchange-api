package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {

    private String sellCode;

    private BigDecimal sellAmount;

    private String buyCode;

    private BigDecimal buyAmount;
}
