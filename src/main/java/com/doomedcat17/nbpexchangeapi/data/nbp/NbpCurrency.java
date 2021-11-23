package com.doomedcat17.nbpexchangeapi.data.nbp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NbpCurrency {

    private String name;

    private BigDecimal midRateInPLN;

    private LocalDate effectiveDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbpCurrency that = (NbpCurrency) o;

        if (!name.equals(that.name)) return false;
        return effectiveDate.equals(that.effectiveDate);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + effectiveDate.hashCode();
        return result;
    }
}
