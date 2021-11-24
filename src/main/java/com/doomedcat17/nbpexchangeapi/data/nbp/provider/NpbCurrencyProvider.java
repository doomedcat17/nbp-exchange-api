package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.nbp.NbpExchangeRate;

import java.io.IOException;
import java.util.List;

public interface NpbCurrencyProvider {

    List<NbpExchangeRate> getNbpCurrencies() throws IOException;
}
