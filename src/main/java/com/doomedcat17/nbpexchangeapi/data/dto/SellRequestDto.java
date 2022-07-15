package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SellRequestDto {

    @NotBlank(message = "Missing sellCode property")
    private String sellCode;
    @NotBlank(message = "Missing buyAmount property")
    private String buyAmount;
    @NotBlank(message = "Missing buyCode property")
    private String buyCode;

}
