package com.doomedcat17.nbpexchangeapi.data.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface NbpRatesProvider {


    List<NbpExchangeRate> getNbpExchangeRatesFromLastWeek(LocalDate now) throws IOException;

    List<NbpExchangeRate> getNbpExchangeRatesFromDate(LocalDate date) throws IOException;
}
