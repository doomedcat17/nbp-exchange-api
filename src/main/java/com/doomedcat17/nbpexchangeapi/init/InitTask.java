package com.doomedcat17.nbpexchangeapi.init;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.NbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDAO;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class InitTask {

    private final CurrencyDAO currencyDAO;

    private final NbpExchangeRateRepository nbpExchangeRateRepository;

    private final NbpRatesProvider nbpRatesProvider;

    public void execute() throws IOException {
        if (currencyDAO.count() < 150 || nbpExchangeRateRepository.getSize() == 0) {
            List<NbpExchangeRate> nbpExchangeRates =
                    nbpRatesProvider.getNbpExchangeRatesFromLastWeek(LocalDate.now());
            nbpExchangeRateRepository.addAllExchangeRates(nbpExchangeRates);
        }
    }

    public InitTask(CurrencyDAO currencyDAO, NbpExchangeRateRepository nbpExchangeRateRepository, NbpRatesProvider nbpRatesProvider) {
        this.currencyDAO = currencyDAO;
        this.nbpExchangeRateRepository = nbpExchangeRateRepository;
        this.nbpRatesProvider = nbpRatesProvider;
    }
}
