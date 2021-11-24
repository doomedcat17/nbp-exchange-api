package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.nbp.NbpCurrency;

import java.io.IOException;
import java.util.List;

public interface NpbCurrencyProvider {

    List<NbpCurrency> getNbpCurrencies() throws IOException;
}
