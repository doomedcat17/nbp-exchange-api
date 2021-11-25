package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class NbpExchangeRateRepository {

    private NbpExchangeRateDAO nbpExchangeRateDAO;

    public void addExchangeRate(NbpExchangeRate nbpExchangeRate) {
        NbpExchangeRate prestentExchangeRate =
                getByCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (prestentExchangeRate == null) nbpExchangeRateDAO.save(nbpExchangeRate);
    }

    public NbpExchangeRate getByCodeAndEffectiveDate(String code, LocalDate effectiveDate) {
        return nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate(code, effectiveDate);
    }

    public NbpExchangeRateRepository(NbpExchangeRateDAO nbpExchangeRateDAO) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
    }
}
