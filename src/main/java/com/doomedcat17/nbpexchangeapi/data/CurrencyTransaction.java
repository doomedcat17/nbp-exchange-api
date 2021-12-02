package com.doomedcat17.nbpexchangeapi.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class CurrencyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date date;

    @ManyToOne
    @JoinColumn(name="sell_currency", nullable=false)
    private Currency sellCurrency;

    @Column(scale = 2, precision = 20)
    private BigDecimal soldAmount;

    @ManyToOne
    @JoinColumn(name="buy_currency", nullable=false)
    private Currency buyCurrency;

    @Column(scale = 2, precision = 20)
    private BigDecimal boughtAmount;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyTransaction that = (CurrencyTransaction) o;

        if (!sellCurrency.equals(that.sellCurrency)) return false;
        if (!soldAmount.equals(that.soldAmount)) return false;
        if (!buyCurrency.equals(that.buyCurrency)) return false;
        if (!boughtAmount.equals(that.boughtAmount)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = sellCurrency.hashCode();
        result = 31 * result + soldAmount.hashCode();
        result = 31 * result + buyCurrency.hashCode();
        result = 31 * result + boughtAmount.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
