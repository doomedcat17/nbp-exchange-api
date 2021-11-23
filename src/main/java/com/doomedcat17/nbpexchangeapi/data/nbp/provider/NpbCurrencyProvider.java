package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.nbp.NbpCurrency;

import java.util.List;

public interface NpbCurrencyProvider {

    List<NbpCurrency> provideCurrencies();
}
