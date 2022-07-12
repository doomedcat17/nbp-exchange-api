package com.doomedcat17.nbpexchangeapi.data.domain;

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

    @ManyToOne()
    @JoinColumn(name="currency_code", nullable=false)
    private Currency currency;

    @Column(name = "mid_rate_in_pln",scale = 6, precision = 10)
    private BigDecimal midRateInPLN;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;


    public NbpExchangeRate(Currency currency, BigDecimal midRateInPLN, LocalDate effectiveDate) {
        this.currency = currency;
        this.midRateInPLN = midRateInPLN;
        this.effectiveDate = effectiveDate;
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
