package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransactionDto {

    private Date date;

    private String buyCode;

    private BigDecimal buyAmount;

    private String sellCode;

    private BigDecimal sellAmount;

}
