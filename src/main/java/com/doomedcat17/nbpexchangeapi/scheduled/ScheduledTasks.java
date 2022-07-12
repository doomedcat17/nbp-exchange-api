package com.doomedcat17.nbpexchangeapi.scheduled;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRateService;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.NbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.update.RatesUpdater;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
public class ScheduledTasks {

    private final RatesUpdater ratesUpdater;

    //12:31 pm everyday
    @Scheduled(cron = "* 31 12 * * *")
    public void update() {
        ratesUpdater.update();
    }
}
