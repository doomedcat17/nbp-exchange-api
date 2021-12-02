package com.doomedcat17.nbpexchangeapi.task;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.NbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

@Slf4j
public class UpdateTask implements Runnable {

    private final NbpExchangeRateRepository rateRepository;

    private final NbpRatesProvider nbpRatesProvider;

    @Override
    public void run() {
        update();
        log.info("Removing rates older than week...");
        rateRepository.removeAllOlderThanWeek();
        log.info("Removal success!");
    }

    private void update() {
        log.info("Updating...");
        Set<NbpExchangeRate> nbpExchangeRates =
                nbpRatesProvider.getRecent();
        if (nbpExchangeRates.isEmpty()){
            try {
                log.error("Update failed. Api returned empty collection");
                log.info("Sleeping...");
                Thread.sleep(900000L);
                update();
            } catch (InterruptedException e) {
                log.error("Update process interrupted");
                e.printStackTrace();
            }
        } else {
            nbpExchangeRates.forEach(rateRepository::add);
            log.info("Update success!");
        }
    }

    public UpdateTask(NbpExchangeRateRepository rateRepository, NbpRatesProvider nbpRatesProvider) {
        this.rateRepository = rateRepository;
        this.nbpRatesProvider = nbpRatesProvider;
    }
}
