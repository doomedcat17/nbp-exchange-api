package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.TestDataProvider;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyTransactionDao;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyTransactionRepositoryTest {

    private final NbpExchangeRateDAO nbpExchangeRateDAO;

    private final CurrencyTransactionDao currencyTransactionDao;

    private final CurrencyTransactionRepository currencyTransactionRepository;


    @BeforeEach
    void init() {
        nbpExchangeRateDAO.saveAll(TestDataProvider.sampleExchangeRates());
    }

    @Test
    void shouldAddTransaction() {
        //given
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(new Date(System.currentTimeMillis()));
        transactionDto.setSellAmount(new BigDecimal("12"));
        transactionDto.setBuyAmount(new BigDecimal("20"));
        transactionDto.setSellCode("PLN");
        transactionDto.setBuyCode("USD");

        //when
        currencyTransactionRepository.addTransaction(transactionDto);

        //then
        assertEquals(1, currencyTransactionDao.count());
    }

    @Autowired
    public CurrencyTransactionRepositoryTest(NbpExchangeRateDAO nbpExchangeRateDAO, CurrencyTransactionDao currencyTransactionDao, CurrencyTransactionRepository currencyTransactionRepository) {
        this.nbpExchangeRateDAO = nbpExchangeRateDAO;
        this.currencyTransactionDao = currencyTransactionDao;
        this.currencyTransactionRepository = currencyTransactionRepository;
    }
}