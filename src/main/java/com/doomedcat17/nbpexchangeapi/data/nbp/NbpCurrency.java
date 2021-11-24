package com.doomedcat17.nbpexchangeapi.data.nbp;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NbpCurrency {

    private String name;

    private String code;

    private BigDecimal midRateInPLN;

    private LocalDate effectiveDate;

    public static NbpCurrency applyJson(JSONObject jsonObject) {
        NbpCurrency nbpCurrency = new NbpCurrency();
        nbpCurrency.setName(jsonObject.getString("currency"));
        nbpCurrency.setCode(jsonObject.getString("code"));
        nbpCurrency.setMidRateInPLN(jsonObject.getBigDecimal("mid"));
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
