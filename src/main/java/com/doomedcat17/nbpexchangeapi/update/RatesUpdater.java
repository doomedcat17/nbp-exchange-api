package com.doomedcat17.nbpexchangeapi.update;

import com.doomedcat17.nbpexchangeapi.data.domain.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.services.CurrencyTransactionService;
import com.doomedcat17.nbpexchangeapi.services.ExchangeRateService;
import com.doomedcat17.nbpexchangeapi.services.StartWorkDateProvider;
import com.doomedcat17.nbpexchangeapi.services.nbp.provider.NbpRatesProvider;
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
    private final CurrencyTransactionService currencyTransactionService;

    private final StartWorkDateProvider startWorkDateProvider;

    @Value("${doomedcat17.nbp-exchange-api.updater.sleep-time-on-failure:900}")
    private long updaterSleepTimeOnUpdateFailure;

    @Value("${doomedcat17.nbp-exchange-api.rates-ttl-in-workdays:0}")
    private int ratesTTLInWorkdays;

    @Value("${doomedcat17.nbp-exchange-api.transactions-ttl-in-workdays:0}")
    private int transactionsTTLInWorkdays;


    public void update(boolean isFirstUpdate) {
        log.info("Updating...");
        Set<NbpExchangeRate> nbpExchangeRates;
        if (isFirstUpdate) nbpExchangeRates = nbpRatesProvider.getNbpExchangeRatesFromLastWeek(LocalDate.now());
        else nbpExchangeRates = nbpRatesProvider.getRecent();
        if (nbpExchangeRates.isEmpty()) {
            try {
                log.error("Update failed. Api returned empty collection");
                log.info("Sleeping...");
                Thread.sleep(updaterSleepTimeOnUpdateFailure*1000);
                update(isFirstUpdate);
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
            LocalDate ratesStartDate = startWorkDateProvider.get(LocalDate.now(), ratesTTLInWorkdays);
            log.info("Removing rates older than "+ratesStartDate);
            rateService.removeAllOlderThanGivenDate(ratesStartDate);
        }
        if (transactionsTTLInWorkdays > 0) {
            LocalDate transactionsStartDate = startWorkDateProvider.get(LocalDate.now(), transactionsTTLInWorkdays);
            log.info("Removing transactions older than "+transactionsStartDate);
            currencyTransactionService.removeAllOlderThanGivenDate(transactionsStartDate);
        }
    }
}
