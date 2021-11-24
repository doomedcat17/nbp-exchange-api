package com.doomedcat17.nbpexchangeapi.data.nbp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NbpCurrency {

    private String name;

    private String code;

    private BigDecimal midRateInPLN;

    private LocalDate effectiveDate;

    public static NbpCurrency applyJson(JsonNode jsonCurrency) {
        NbpCurrency nbpCurrency = new NbpCurrency();
        nbpCurrency.setName(jsonCurrency.get("currency").asText());
        nbpCurrency.setCode(jsonCurrency.get("code").asText());
        nbpCurrency.setMidRateInPLN(new BigDecimal(jsonCurrency.get("mid").asText()));
        return nbpCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbpCurrency that = (NbpCurrency) o;

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
