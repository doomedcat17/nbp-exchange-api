package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Set;

@Repository
public class NbpExchangeRateRepository {

    private final NbpExchangeRateDAO nbpExchangeRateDAO;

    public void addExchangeRate(NbpExchangeRate nbpExchangeRate) {
        NbpExchangeRate prestentExchangeRate =
                getByCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (prestentExchangeRate == null) nbpExchangeRateDAO.save(nbpExchangeRate);
    }

    public void addAllExchangeRates(Iterable<NbpExchangeRate> exchangeRates) {
        exchangeRates.forEach(this::addExchangeRate);
    }

    public long getSize() {
        return nbpExchangeRateDAO.count();
    }

    public Set<NbpExchangeRate> getMostRecent() {
        return nbpExchangeRateDAO.getRecent();
    }

    public NbpExchangeRate getByCodeAndEffectiveDate(String code, LocalDate effectiveDate) {
        return nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate(code, effectiveDate);
    }

    public NbpExchangeRate getMostRecentByCode(String code) {
        return nbpExchangeRateDAO.getMostRecentByCode(code);
    }


    public NbpExchangeRateRepository(NbpExchangeRateDAO nbpExchangeRateDAO) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
    }
}
