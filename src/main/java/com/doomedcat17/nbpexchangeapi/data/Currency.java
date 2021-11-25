package com.doomedcat17.nbpexchangeapi.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Currency {

    @Id
    private String code;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        return code.equals(currency.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
