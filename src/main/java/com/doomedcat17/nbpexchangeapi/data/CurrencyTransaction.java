package com.doomedcat17.nbpexchangeapi.data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class CurrencyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="sell_currency", nullable=false)
    private Currency sellCurrency;

    @Column(scale = 6, precision = 10)
    private BigDecimal soldAmount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="buy_currency", nullable=false)
    private Currency buyCurrency;

    @Column(scale = 6, precision = 10)
    private BigDecimal boughtAmount;

    private Date date;
}
