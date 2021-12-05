package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDao;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDao;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class NbpExchangeRateRepository {

    private final NbpExchangeRateDao nbpExchangeRateDAO;

    private final WorkWeekStartDateProvider workWeekStartDateProvider;

    private final CurrencyDao currencyDAO;

    public synchronized void add(NbpExchangeRate nbpExchangeRate) {
        Optional<NbpExchangeRate> prestentExchangeRate =
                getByCurrencyCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (prestentExchangeRate.isEmpty()) {
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

    public Optional<NbpExchangeRate> getByCurrencyCodeAndEffectiveDate(String currencyCode, LocalDate effectiveDate) {
        NbpExchangeRate foundNbpExchangeRate = nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate);
        if (foundNbpExchangeRate == null) return Optional.empty();
        return Optional.of(foundNbpExchangeRate);
    }

    public List<NbpExchangeRate> getAllByEffectiveDate(LocalDate effectiveDate) {
        return nbpExchangeRateDAO.getAllByEffectiveDate(effectiveDate);
    }

    public Optional<NbpExchangeRate> getMostRecentByCurrencyCode(String currencyCode) {
        List<NbpExchangeRate> foundExchangeRates = nbpExchangeRateDAO.getMostRecentByCode(currencyCode, PageRequest.of(0, 1));
        if (!foundExchangeRates.isEmpty()) return Optional.of(foundExchangeRates.get(0));
        else return Optional.empty();
    }

    public NbpExchangeRateRepository(NbpExchangeRateDao nbpExchangeRateDAO, WorkWeekStartDateProvider workWeekStartDateProvider, CurrencyDao currencyDAO) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
        this.workWeekStartDateProvider = workWeekStartDateProvider;
        this.currencyDAO = currencyDAO;
    }
}
