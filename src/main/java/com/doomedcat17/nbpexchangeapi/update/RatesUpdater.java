package com.doomedcat17.nbpexchangeapi.update;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRateService;
import com.doomedcat17.nbpexchangeapi.services.StartWorkDateProvider;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.NbpRatesProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatesUpdater {

    private final ExchangeRateService rateService;
    private final NbpRatesProvider nbpRatesProvider;

    private final StartWorkDateProvider startWorkDateProvider;

    @Value("${doomedcat17.nbp-exchange-api.updater.sleep-time-ms-on-failure:900000}")
    private long updaterSleepTimeMsOnUpdateFailure;

    @Value("${doomedcat17.nbp-exchange-api.rates-ttl-in-workdays:0}")
    private int ratesTTLInWorkdays;


    public void update() {
        log.info("Updating...");
        Set<NbpExchangeRate> nbpExchangeRates =
                nbpRatesProvider.getRecent();
        if (nbpExchangeRates.isEmpty()) {
            try {
                log.error("Update failed. Api returned empty collection");
                log.info("Sleeping...");
                Thread.sleep(updaterSleepTimeMsOnUpdateFailure);
                update();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Update process interrupted");
                e.printStackTrace();
            }
        } else {
            nbpExchangeRates.forEach(rateService::add);
            log.info("Update success!");
        }
        if (ratesTTLInWorkdays > 0) {
            LocalDate startDate = startWorkDateProvider.get(LocalDate.now(), ratesTTLInWorkdays);
            log.info("Removing rates older than "+startDate);
            rateService.removeAllOlderThanGivenDate(startDate);
            log.info("Removal success!");
        }
    }
}
