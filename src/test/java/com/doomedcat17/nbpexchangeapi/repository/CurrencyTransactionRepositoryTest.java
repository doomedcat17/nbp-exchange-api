package com.doomedcat17.nbpexchangeapi.repository;

import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.dao.CurrencyTransactionDao;
import com.doomedcat17.nbpexchangeapi.repository.dao.NbpExchangeRateDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class CurrencyTransactionRepositoryTest {

    @Autowired
    private CurrencyTransactionDao currencyTransactionDao;

    @Autowired
    private CurrencyTransactionRepository currencyTransactionRepository;

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

    @Test
    void shouldReturnLatestTransaction() {

        TransactionDto expectedTransactionDto = new TransactionDto();
        expectedTransactionDto.setDate(new Date(System.currentTimeMillis()));
        expectedTransactionDto.setSellAmount(new BigDecimal("12.00"));
        expectedTransactionDto.setBuyAmount(new BigDecimal("20.00"));
        expectedTransactionDto.setSellCode("PLN");
        expectedTransactionDto.setBuyCode("USD");

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(new Date(System.currentTimeMillis()));
        transactionDto.setSellAmount(new BigDecimal("12"));
        transactionDto.setBuyAmount(new BigDecimal("20"));
        transactionDto.setSellCode("JPY");
        transactionDto.setBuyCode("AUD");

        currencyTransactionRepository.addTransaction(expectedTransactionDto);
        currencyTransactionRepository.addTransaction(transactionDto);

        //when
        TransactionDto foundTransaction = currencyTransactionRepository.getLatestTransaction();

        //then
        assertEquals(expectedTransactionDto, foundTransaction);
    }

    @Test
    void shouldReturnTransactionsFromGivenDate() {

        //given
        Date expectedDate = Date.valueOf(LocalDate.parse("2020-07-11"));

        TransactionDto expectedTransactionDto1 = new TransactionDto();
        expectedTransactionDto1.setDate(expectedDate);
        expectedTransactionDto1.setSellAmount(new BigDecimal("12.00"));
        expectedTransactionDto1.setBuyAmount(new BigDecimal("20.00"));
        expectedTransactionDto1.setSellCode("PLN");
        expectedTransactionDto1.setBuyCode("USD");

        TransactionDto expectedTransactionDto2 = new TransactionDto();
        expectedTransactionDto2.setDate(expectedDate);
        expectedTransactionDto2.setSellAmount(new BigDecimal("12"));
        expectedTransactionDto2.setBuyAmount(new BigDecimal("20"));
        expectedTransactionDto2.setSellCode("JPY");
        expectedTransactionDto2.setBuyCode("AUD");

        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setDate(new Date(20000L));
        transactionDto1.setSellAmount(new BigDecimal("12"));
        transactionDto1.setBuyAmount(new BigDecimal("20"));
        transactionDto1.setSellCode("JPY");
        transactionDto1.setBuyCode("AUD");

        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setDate(new Date(20000L));
        transactionDto2.setSellAmount(new BigDecimal("12"));
        transactionDto2.setBuyAmount(new BigDecimal("20"));
        transactionDto2.setSellCode("JPY");
        transactionDto2.setBuyCode("AUD");

        currencyTransactionRepository.addTransaction(expectedTransactionDto1);
        currencyTransactionRepository.addTransaction(expectedTransactionDto2);
        currencyTransactionRepository.addTransaction(transactionDto1);
        currencyTransactionRepository.addTransaction(transactionDto2);

        //when
        List<TransactionDto> foundTransactions = currencyTransactionRepository.getAllByDate(LocalDate.parse("2020-07-11"));

        //then
        assertTrue(foundTransactions.stream()
                .allMatch(transactionDto -> transactionDto.getDate().getTime() == expectedDate.getTime()));
    }

    @Test
    void shouldReturnAllBetweenGivenDates() {

        //given
        LocalDate startDate = LocalDate.parse("2021-12-01");
        LocalDate endDate = LocalDate.parse("2021-12-03");

        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setDate(Date.valueOf(LocalDate.parse("2021-11-30")));
        transactionDto1.setSellAmount(new BigDecimal("12.00"));
        transactionDto1.setBuyAmount(new BigDecimal("20.00"));
        transactionDto1.setSellCode("PLN");
        transactionDto1.setBuyCode("USD");

        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setDate(Date.valueOf(LocalDate.parse("2021-12-01")));
        transactionDto2.setSellAmount(new BigDecimal("12"));
        transactionDto2.setBuyAmount(new BigDecimal("20"));
        transactionDto2.setSellCode("JPY");
        transactionDto2.setBuyCode("AUD");

        TransactionDto transactionDto3 = new TransactionDto();
        transactionDto3.setDate(Date.valueOf(LocalDate.parse("2021-12-02")));
        transactionDto3.setSellAmount(new BigDecimal("12"));
        transactionDto3.setBuyAmount(new BigDecimal("20"));
        transactionDto3.setSellCode("JPY");
        transactionDto3.setBuyCode("AUD");

        TransactionDto transactionDto4 = new TransactionDto();
        transactionDto4.setDate(Date.valueOf(LocalDate.parse("2021-12-03")));
        transactionDto4.setSellAmount(new BigDecimal("12"));
        transactionDto4.setBuyAmount(new BigDecimal("20"));
        transactionDto4.setSellCode("JPY");
        transactionDto4.setBuyCode("AUD");

        TransactionDto transactionDto5 = new TransactionDto();
        transactionDto5.setDate(Date.valueOf(LocalDate.parse("2021-12-04")));
        transactionDto5.setSellAmount(new BigDecimal("12"));
        transactionDto5.setBuyAmount(new BigDecimal("20"));
        transactionDto5.setSellCode("JPY");
        transactionDto5.setBuyCode("AUD");

        currencyTransactionRepository.addTransaction(transactionDto1);
        currencyTransactionRepository.addTransaction(transactionDto2);
        currencyTransactionRepository.addTransaction(transactionDto3);
        currencyTransactionRepository.addTransaction(transactionDto4);
        currencyTransactionRepository.addTransaction(transactionDto5);

        //when
        List<TransactionDto> foundTransactions = currencyTransactionRepository.getAllBetweenDates(startDate ,endDate);

        //then
        assertTrue(foundTransactions
                .stream().allMatch(transactionDto ->
                        !transactionDto.getDate().before(Date.valueOf(startDate))
                                && !transactionDto.getDate().after(Date.valueOf(endDate))));
        assertEquals(3, foundTransactions.size());
    }
}