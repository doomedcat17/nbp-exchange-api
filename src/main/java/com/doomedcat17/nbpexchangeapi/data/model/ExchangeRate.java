package com.doomedcat17.nbpexchangeapi.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "exchange_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sell_currency", nullable = false)
    private Currency sell;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "buy_currency", nullable = false)
    private Currency buy;

    private BigDecimal rate;

    private LocalDate effectiveDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRate that = (ExchangeRate) o;

        if (!sell.equals(that.sell)) return false;
        if (!buy.equals(that.buy)) return false;
        return effectiveDate.equals(that.effectiveDate);
    }

    @Override
    public int hashCode() {
        int result = sell.hashCode();
        result = 31 * result + buy.hashCode();
        result = 31 * result + effectiveDate.hashCode();
        return result;
    }
}
