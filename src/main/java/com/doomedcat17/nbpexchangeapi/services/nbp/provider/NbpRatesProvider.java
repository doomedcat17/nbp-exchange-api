package com.doomedcat17.nbpexchangeapi.services.nbp.provider;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import java.time.LocalDate;
import java.util.Set;

public interface NbpRatesProvider {


    Set<NbpExchangeRate> getNbpExchangeRatesFromLastWeek(LocalDate now);

    Set<NbpExchangeRate> getRecent();
}
