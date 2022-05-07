package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.SellRequestDto;
import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import com.doomedcat17.nbpexchangeapi.repository.CurrencyTransactionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void shouldBuyCurrency() {
        //given
        String currencyCodeToBuy = "USD";

        String currencyCodeToSell = "PLN";

        BigDecimal amount = new BigDecimal("20.00");

        //when
        MvcResult result = mockMvc
                .perform(get("/api/trade/{currencyCodeToBuy}/{currencyCodeToSell}/{buyAmount}",
                        currencyCodeToBuy, currencyCodeToSell, amount))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        TransactionDto transactionDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                TransactionDto.class);

        assertEquals(currencyCodeToBuy, transactionDto.getBuyCode());
        assertEquals(currencyCodeToSell, transactionDto.getSellCode());
        assertEquals(amount, transactionDto.getBuyAmount());
        assertEquals(new BigDecimal("81.80"), transactionDto.getSellAmount());
    }

    @SneakyThrows
    @Test
    void shouldNOTBuyCurrency() {
        //given
        String currencyCodeToBuy = "USD";

        String currencyCodeToSell = "XDD";

        BigDecimal amount = new BigDecimal("20.00");

        //then
        mockMvc.perform(get("/api/trade/{currencyCodeToBuy}/{currencyCodeToSell}/{buyAmount}",
                        currencyCodeToBuy, currencyCodeToSell, amount))
                .andExpect(result1 -> status().isNotFound());

    }


    @SneakyThrows
    @Test
    void shouldBuyCurrencyPOST() {
        //given
        String currencyCodeToBuy = "USD";

        String currencyCodeToSell = "PLN";

        BigDecimal amount = new BigDecimal("20.00");

        SellRequestDto sellRequestDto = new SellRequestDto();
        sellRequestDto.setBuyAmount(amount.toString());
        sellRequestDto.setBuyCode(currencyCodeToBuy);
        sellRequestDto.setSellCode(currencyCodeToSell);

        //when
        MvcResult result = mockMvc
                .perform(post("/api/trade")
                        .content(objectMapper.writeValueAsString(sellRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        TransactionDto transactionDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                TransactionDto.class);

        assertEquals(currencyCodeToBuy, transactionDto.getBuyCode());
        assertEquals(currencyCodeToSell, transactionDto.getSellCode());
        assertEquals(amount, transactionDto.getBuyAmount());
        assertEquals(new BigDecimal("81.80"), transactionDto.getSellAmount());
    }

    @SneakyThrows
    @Test
    void shouldNOTBuyCurrencyPOST() {
        //given

        String currencyCodeToSell = "PLN";

        BigDecimal amount = new BigDecimal("20.00");

        SellRequestDto sellRequestDto = new SellRequestDto();
        sellRequestDto.setBuyAmount(amount.toString());
        sellRequestDto.setSellCode(currencyCodeToSell);

        //then
        mockMvc.perform(post("/api/trade")
                        .content(objectMapper.writeValueAsString(sellRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result1 -> status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void shouldReturnHistoryFromGivenDate() {
        //given
        LocalDate date = LocalDate.parse("2021-11-29");

        Date dayStart = java.sql.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        Date dayEnd = java.sql.Date.from(date.atStartOfDay()
                .plusHours(23)
                .plusMinutes(59)
                .plusSeconds(59)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        //when
        MvcResult result = mockMvc
                .perform(get("/api/trade/history/{date}", date))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        List<TransactionDto> transactions = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {});

        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().allMatch(
                transactionDto -> transactionDto.getDate().after(dayStart) && transactionDto.getDate().before(dayEnd)
        ));
    }

    @SneakyThrows
    @Test
    void shouldNOTReturnHistoryFromGivenDateDueToFormatError() {

        //given
        String date = "21-12-2021";

        //when
        mockMvc.perform(get("/api/trade/history/{date}", date))
                .andExpect(result1 -> status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void shouldNOTReturnHistoryFromGivenDatesDueToFormatError() {

        //given
        String startDate = "21-12-2021";

        String endDate = "23-12-2021";

        //when
        mockMvc.perform(get("/api/trade/history/{startDate}/{endDate}", startDate, endDate))
                .andExpect(result1 -> status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void shouldReturnHistoryFromGivenDates() {
        //given
        LocalDate startDate = LocalDate.parse("2021-11-29");

        LocalDate endDate = LocalDate.parse("2021-12-01");

        Date start = java.sql.Date.from(startDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        Date end = java.sql.Date.from(endDate.atStartOfDay()
                .plusHours(23)
                .plusMinutes(59)
                .plusSeconds(59)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        //when
        MvcResult result = mockMvc
                .perform(get("/api/trade/history/{startDate}/{endDate}", startDate, endDate))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        List<TransactionDto> transactions = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {});

        assertEquals(4, transactions.size());
        assertTrue(transactions.stream().allMatch(
                transactionDto -> transactionDto.getDate().after(start) && transactionDto.getDate().before(end)
        ));
    }


    public TradeControllerTest() {
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }


}