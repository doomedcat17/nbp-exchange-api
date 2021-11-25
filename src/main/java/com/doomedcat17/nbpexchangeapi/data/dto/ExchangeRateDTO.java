package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class ExchangeRateDTO {

    private String code;

    Set<RateDTO> rates;
}
