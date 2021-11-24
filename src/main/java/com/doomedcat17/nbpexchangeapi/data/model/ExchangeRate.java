package com.doomedcat17.nbpexchangeapi.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sell_currency", nullable = false)
    private Currency sell;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buy_currency", nullable = false)
    private Currency buy;

    private LocalDate effectiveDate;



}
