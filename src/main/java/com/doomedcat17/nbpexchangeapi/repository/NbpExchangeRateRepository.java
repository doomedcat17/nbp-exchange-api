package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.Currency;
import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyDAO;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import com.doomedcat17.nbpexchangeapi.services.WorkWeekStartDateProvider;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    private final EntityManager entityManager;

    public synchronized void add(NbpExchangeRate nbpExchangeRate) {
        NbpExchangeRate prestentExchangeRate =
                getByCodeAndEffectiveDate(
                        nbpExchangeRate.getCurrency().getCode(),
                        nbpExchangeRate.getEffectiveDate());
        if (prestentExchangeRate == null) {
            Optional<Currency> currency = currencyDAO.findById(nbpExchangeRate.getCurrency().getCode());
            currency.ifPresent(nbpExchangeRate::setCurrency);
            nbpExchangeRateDAO.save(nbpExchangeRate);
        }
    }


    public List<NbpExchangeRate> getNbpExchangeRates(String currencyCode, String dateString) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NbpExchangeRate> criteriaQuery =
                criteriaBuilder.createQuery(NbpExchangeRate.class);
        Root<NbpExchangeRate> exchangeRateRoot = criteriaQuery.from(NbpExchangeRate.class);

        Predicate currencyPredicate = null;
        if (!currencyCode.isBlank()) {
            currencyPredicate =
                    criteriaBuilder.equal(exchangeRateRoot.get("currency").get("code"), currencyCode);
            criteriaQuery.where(currencyPredicate);
        }

        Predicate datePredicate;
        if (!dateString.isBlank()) {
            datePredicate =
                    criteriaBuilder.equal(
                            exchangeRateRoot.get("effectiveDate"),
                            LocalDate.parse(dateString));
        } else {
            Subquery<LocalDate> subQuery = criteriaQuery.subquery(LocalDate.class);
            Root<NbpExchangeRate> subExchangeRateRoot = subQuery.from(NbpExchangeRate.class);

            Expression<LocalDate> effectiveDate = subExchangeRateRoot.get("effectiveDate");
            subQuery.select(criteriaBuilder.greatest(effectiveDate));

            subQuery.where(criteriaBuilder.equal(subExchangeRateRoot.get("currency"),
                    exchangeRateRoot.get("currency")));

            datePredicate = criteriaBuilder.equal(exchangeRateRoot.get("effectiveDate"), subQuery);
        }

        Predicate condition;

        if (currencyPredicate != null) condition = criteriaBuilder.and(currencyPredicate, datePredicate);
        else condition = datePredicate;

        criteriaQuery.where(condition);


        TypedQuery<NbpExchangeRate> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();

    }


    public List<NbpExchangeRate> getAll(String currencyCode, String textDate) {
        if (textDate.isBlank()) {
            return nbpExchangeRateDAO.getAllByCurrencyCode(currencyCode);
        } else {
            LocalDate effectiveDate = LocalDate.parse(textDate);
            return List.of(nbpExchangeRateDAO.getByCurrencyCodeAndEffectiveDate(currencyCode, effectiveDate));
        }
    }


    public synchronized void removeAllOlderThanWeek() {
        nbpExchangeRateDAO.deleteAllByEffectiveDateBefore(workWeekStartDateProvider.get(LocalDate.now()));
    }

    public synchronized long getSize() {
        return nbpExchangeRateDAO.count();
    }

    public synchronized List<NbpExchangeRate> getMostRecent() {
        return nbpExchangeRateDAO.getRecent();
    }

    public synchronized NbpExchangeRate getByCodeAndEffectiveDate(String code, LocalDate effectiveDate) {
        return nbpExchangeRateDAO
                .getByCurrencyCodeAndEffectiveDate(code, effectiveDate);
    }

    public synchronized List<NbpExchangeRate> getByAllByEffectiveDate(LocalDate effectiveDate) {
        return nbpExchangeRateDAO.getAllByEffectiveDate(effectiveDate);
    }

    public synchronized NbpExchangeRate getMostRecentByCode(String code) {
        return nbpExchangeRateDAO.getMostRecentByCode(code);
    }

    public NbpExchangeRateRepository(NbpExchangeRateDAO nbpExchangeRateDAO, WorkWeekStartDateProvider workWeekStartDateProvider, CurrencyDAO currencyDAO, EntityManager entityManager) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
        this.workWeekStartDateProvider = workWeekStartDateProvider;
        this.currencyDAO = currencyDAO;
        this.entityManager = entityManager;
    }
}
