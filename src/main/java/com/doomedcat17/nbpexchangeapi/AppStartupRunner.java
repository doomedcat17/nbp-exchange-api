package com.doomedcat17.nbpexchangeapi;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.NbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Set;

@Profile({"!test"})
@Component
@Slf4j
public class AppStartupRunner implements ApplicationRunner {

    private final NbpExchangeRateRepository rateRepository;

    private final NbpRatesProvider nbpRatesProvider;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Initializing...");
        init();
        log.info("Removing rates older than week...");
        rateRepository.removeAllOlderThanWeek();
        log.info("Removal success!");
        log.info("Initialization complete");
    }

    private void init() {
        Set<NbpExchangeRate> nbpExchangeRates =
                nbpRatesProvider.getNbpExchangeRatesFromLastWeek(LocalDate.now());
        if (nbpExchangeRates.isEmpty()) {
            try {
                log.error("Initialization failed. Nbp returned empty collection");
                log.info("Sleeping...");
                Thread.sleep(900000L);
                init();
            } catch (InterruptedException e) {
                log.error("Init process interrupted");
                e.printStackTrace();
            }
        } else {
            nbpExchangeRates.forEach(rateRepository::add);
            log.info("NbpExchangeRates added");
        }
    }

    public AppStartupRunner(NbpExchangeRateRepository rateRepository, NbpRatesProvider nbpRatesProvider) {
        this.rateRepository = rateRepository;
        this.nbpRatesProvider = nbpRatesProvider;
    }
}
