package com.doomedcat17.nbpexchangeapi.data.nbp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NbpExchangeRate {

    private String name;

    private String code;

    private BigDecimal midRateInPLN;

    private LocalDate effectiveDate;

    public static NbpExchangeRate applyJson(JsonNode jsonCurrency) {
        NbpExchangeRate nbpExchangeRAte = new NbpExchangeRate();
        nbpExchangeRAte.setName(jsonCurrency.get("currency").asText());
        nbpExchangeRAte.setCode(jsonCurrency.get("code").asText());
        nbpExchangeRAte.setMidRateInPLN(new BigDecimal(jsonCurrency.get("mid").asText()));
        return nbpExchangeRAte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbpExchangeRate that = (NbpExchangeRate) o;

        if (!name.equals(that.name)) return false;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
