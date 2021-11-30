package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDAO;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

@Repository
@Transactional
public class NbpExchangeRateRepository {

    private final NbpExchangeRateDAO nbpExchangeRateDAO;

    private final WorkWeekStartDateProvider workWeekStartDateProvider;

    public synchronized void add(NbpExchangeRate nbpExchangeRate) {
        NbpExchangeRate prestentExchangeRate =
                getByCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (prestentExchangeRate == null) {
            nbpExchangeRateDAO.save(nbpExchangeRate);
        }
    }

    public synchronized void addAll(Iterable<NbpExchangeRate> exchangeRates) {
        exchangeRates.forEach(this::add);
    }

    public synchronized void removeAllOlderThanWeek() {
        nbpExchangeRateDAO.deleteAllByEffectiveDateBefore(workWeekStartDateProvider.get(LocalDate.now()));
    }

    public synchronized long getSize() {
        return nbpExchangeRateDAO.count();
    }

    public synchronized Set<NbpExchangeRate> getMostRecent() {
        return nbpExchangeRateDAO.getRecent();
    }

    public synchronized NbpExchangeRate getByCodeAndEffectiveDate(String code, LocalDate effectiveDate) {
        return nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate(code, effectiveDate);
    }

    public synchronized NbpExchangeRate getMostRecentByCode(String code) {
        return nbpExchangeRateDAO.getMostRecentByCode(code);
    }

    public NbpExchangeRateRepository(NbpExchangeRateDAO nbpExchangeRateDAO, WorkWeekStartDateProvider workWeekStartDateProvider) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
        this.workWeekStartDateProvider = workWeekStartDateProvider;
    }
}
