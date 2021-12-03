package com.doomedcat17.nbpexchangeapi.scheduled;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.NbpRatesProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Profile({"dev", "prod"})
@Component
@Slf4j
public class ScheduledTasks {

    private final NbpExchangeRateRepository rateRepository;

    private final NbpRatesProvider nbpRatesProvider;

    //12:31 pm everyday
    @Scheduled(cron = "* 31 12 * * *")
    public void update() {
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
        log.info("Removing rates older than week...");
        rateRepository.removeAllOlderThanWeek();
        log.info("Removal success!");
    }

    public ScheduledTasks(NbpExchangeRateRepository rateRepository, NbpRatesProvider nbpRatesProvider) {
        this.rateRepository = rateRepository;
        this.nbpRatesProvider = nbpRatesProvider;
    }
}
