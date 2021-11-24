package com.doomedcat17.nbpexchangeapi.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    private String code;

    private String name;

    @OneToMany(mappedBy="sell")
    private Set<ExchangeRate> sellExchangeRates;

    @OneToMany(mappedBy="buy")
    private Set<ExchangeRate> buyExchangeRates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (!code.equals(currency.code)) return false;
        return name.equals(currency.name);
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
