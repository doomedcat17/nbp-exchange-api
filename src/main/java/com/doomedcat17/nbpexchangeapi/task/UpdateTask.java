package com.doomedcat17.nbpexchangeapi.task;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.nbp.provider.NbpRatesProvider;
import com.doomedcat17.nbpexchangeapi.repository.NbpExchangeRateRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class UpdateTask implements Runnable {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final NbpExchangeRateRepository rateRepository;

    private final NbpRatesProvider nbpRatesProvider;

    @Override
    public void run() {
        Set<NbpExchangeRate> nbpExchangeRates =
                nbpRatesProvider.getNbpExchangeRatesFromLastWeek(LocalDate.now());
        if (nbpExchangeRates.isEmpty()){
            threadPoolTaskScheduler.schedule(this, Instant.ofEpochMilli(System.currentTimeMillis()+900000L));
        } else {
            NbpExchangeRate plnExchangeRate = new NbpExchangeRate();
            plnExchangeRate.setCurrency(new Currency("PLN", "Polski złoty"));
            plnExchangeRate.setMidRateInPLN(new BigDecimal("1"));
            plnExchangeRate.setEffectiveDate(LocalDate.now());
            nbpExchangeRates.add(plnExchangeRate);
            rateRepository.addAll(nbpExchangeRates);
        }
        rateRepository.removeAllOlderThanWeek();
    }

    public UpdateTask(ThreadPoolTaskScheduler threadPoolTaskScheduler, NbpExchangeRateRepository rateRepository, NbpRatesProvider nbpRatesProvider) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.rateRepository = rateRepository;
        this.nbpRatesProvider = nbpRatesProvider;
    }
}
