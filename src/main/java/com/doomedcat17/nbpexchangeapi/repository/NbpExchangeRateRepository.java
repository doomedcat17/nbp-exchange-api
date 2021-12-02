package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDAO;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class NbpExchangeRateRepository {

    private final NbpExchangeRateDAO nbpExchangeRateDAO;

    private final WorkWeekStartDateProvider workWeekStartDateProvider;

    private final CurrencyDAO currencyDAO;

    public synchronized void add(NbpExchangeRate nbpExchangeRate) {
        NbpExchangeRate prestentExchangeRate =
                getByCurrencyCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (prestentExchangeRate == null) {
            Optional<Currency> currency = currencyDAO.findById(nbpExchangeRate.getCurrency().getCode());
            currency.ifPresent(nbpExchangeRate::setCurrency);
            nbpExchangeRateDAO.save(nbpExchangeRate);
        }
    }

    public List<NbpExchangeRate> getAllByCurrencyCode(String currencyCode) {
        return nbpExchangeRateDAO.getAllByCurrencyCode(currencyCode);
    }


    public void removeAllOlderThanWeek() {
        nbpExchangeRateDAO.deleteAllByEffectiveDateBefore(workWeekStartDateProvider.get(LocalDate.now()));
    }

    public long getSize() {
        return nbpExchangeRateDAO.count();
    }

    public List<NbpExchangeRate> getMostRecent() {
        return nbpExchangeRateDAO.getRecent();
    }

    public NbpExchangeRate getByCurrencyCodeAndEffectiveDate(String currencyCode, LocalDate effectiveDate) {
        return nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
    }

    public List<NbpExchangeRate> getAllByEffectiveDate(LocalDate effectiveDate) {
        return nbpExchangeRateDAO.getAllByEffectiveDate(effectiveDate);
    }

    public NbpExchangeRate getMostRecentByCurrencyCode(String currencyCode) {
        return nbpExchangeRateDAO.getMostRecentByCode(currencyCode);
    }

    public NbpExchangeRateRepository(NbpExchangeRateDAO nbpExchangeRateDAO, WorkWeekStartDateProvider workWeekStartDateProvider, CurrencyDAO currencyDAO) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
        this.workWeekStartDateProvider = workWeekStartDateProvider;
        this.currencyDAO = currencyDAO;
    }
}
