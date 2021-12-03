package com.doomedcat17.nbpexchangeapi.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SellRequestDto {

    private String sellCode;

    private String buyAmount;
    
    private String buyCode;
    

    @JsonIgnore
    public String getEmptyParameterName() {
        if (sellCode == null || sellCode.isEmpty()) return "sellCode";
        if (buyCode == null || buyCode.isEmpty()) return "buyCode";
        if (buyAmount == null || buyAmount.isEmpty()) return "buyAmount";
        return "";
    }



    public String getSellCode() {
        return sellCode;
    }

    public void setSellCode(String sellCode) {
        this.sellCode = sellCode;
    }

    public String getBuyCode() {
        return buyCode;
    }

    public void setBuyCode(String buyCode) {
        this.buyCode = buyCode;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public SellRequestDto() {
    }
}
