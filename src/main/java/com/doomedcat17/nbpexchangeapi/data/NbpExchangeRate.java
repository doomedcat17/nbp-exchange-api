package com.doomedcat17.nbpexchangeapi.data;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "nbp_exchange_rates")
public class NbpExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="currency_code", nullable=false)
    private Currency currency;

    private BigDecimal midRateInPLN;

    private LocalDate effectiveDate;

    public static NbpExchangeRate applyJson(JsonNode jsonCurrency) {
        NbpExchangeRate nbpExchangeRate = new NbpExchangeRate();
        Currency currency = new Currency();
        currency.setName(jsonCurrency.get("currency").asText());
        currency.setCode(jsonCurrency.get("code").asText());
        nbpExchangeRate.setCurrency(currency);
        nbpExchangeRate.setMidRateInPLN(new BigDecimal(jsonCurrency.get("mid").asText()));
        return nbpExchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbpExchangeRate that = (NbpExchangeRate) o;

        if (!currency.equals(that.currency)) return false;
        return effectiveDate.equals(that.effectiveDate);
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + effectiveDate.hashCode();
        return result;
    }
}
