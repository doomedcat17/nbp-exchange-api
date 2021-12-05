package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.ExchangeRateDTO;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class RatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void shouldReturnRecentRates() {
        //given
        String currencyCode = "USD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{currencyCode}/recent", currencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        ExchangeRateDTO exchangeRateDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                ExchangeRateDTO.class);

        assertEquals(currencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();

        assertEquals(4, rates.size());
        assertTrue(rates.stream().allMatch(
                rateDTO ->
                        rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-30"))
                                || (rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-25")) && rateDTO.getCode().equals("AFN"))));
        assertTrue(rates.stream().noneMatch(rate -> rate.getCode().equals("USD")));
    }


    @SneakyThrows
    @Test
    void shouldReturnNoRates() {
        //given
        String currencyCode = "XDD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{currencyCode}/recent", currencyCode))
                .andExpect(result1 -> status().isNotFound()).andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @SneakyThrows
    @Test
    void shouldReturnNoRates2() {
        objectMapper.findAndRegisterModules();
        //given
        String currencyCode = "XDD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{currencyCode}/all", currencyCode))
                .andExpect(result1 -> status().isNotFound()).andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @SneakyThrows
    @Test
    void shouldReturnAllRatesForGivenCode() {
        //given
        String currencyCode = "USD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{currencyCode}/all", currencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        ExchangeRateDTO exchangeRateDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                ExchangeRateDTO.class);

        assertEquals(currencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();

        assertEquals(26, rates.size());
        assertTrue(rates.stream().noneMatch(rate -> rate.getCode().equals("USD")));
    }

    @SneakyThrows
    @Test
    void shouldReturnRecentExchangeRatesForGivenCodes() {
        //given
        String sourceCurrencyCode = "USD";

        String targetCurrencyCode = "PLN";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/recent",
                        sourceCurrencyCode, targetCurrencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        ExchangeRateDTO exchangeRateDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                ExchangeRateDTO.class);

        assertEquals(sourceCurrencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();

        assertEquals(1, rates.size());

        RateDTO rateDTO = rates.get(0);
        assertEquals("PLN", rateDTO.getCode());
        assertEquals(LocalDate.parse("2021-11-30"), rateDTO.getEffectiveDate());
    }

    @SneakyThrows
    @Test
    void shouldReturnAllForGivenCodes() {
        //given
        String sourceCurrencyCode = "USD";

        String targetCurrencyCode = "PLN";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/all",
                        sourceCurrencyCode, targetCurrencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        ExchangeRateDTO exchangeRateDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                ExchangeRateDTO.class);

        assertEquals(sourceCurrencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();

        assertEquals(8, rates.size());

        assertTrue(rates.stream().allMatch(rateDTO -> rateDTO.getCode().equals(targetCurrencyCode)));
    }

    @SneakyThrows
    @Test
    void shouldReturnAllForGivenCodesAndDate() {
        //given
        String sourceCurrencyCode = "USD";

        String targetCurrencyCode = "PLN";

        LocalDate date = LocalDate.parse("2021-11-26");

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/{date}",
                        sourceCurrencyCode, targetCurrencyCode, date))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        ExchangeRateDTO exchangeRateDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                ExchangeRateDTO.class);

        assertEquals(sourceCurrencyCode, exchangeRateDTO.getCode());
        List<RateDTO> rates = exchangeRateDTO.getRates();

        assertEquals(1, rates.size());

        RateDTO rateDTO = rates.get(0);
        assertEquals("PLN", rateDTO.getCode());
        assertEquals(date, rateDTO.getEffectiveDate());
    }


    @SneakyThrows
    @Test
    void shouldNOTReturnRecentExchangeRatesForGivenCodes() {
        //given
        String sourceCurrencyCode = "XDD";

        String targetCurrencyCode = "CO";

        //when
        MvcResult result1 = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/recent",
                        sourceCurrencyCode, targetCurrencyCode))
                .andExpect(result -> status().isNotFound()).andReturn();

        MvcResult result2 = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/recent",
                        sourceCurrencyCode, sourceCurrencyCode))
                .andExpect(result -> status().isNotFound()).andReturn();

        MvcResult result3 = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/2021-11-30",
                        sourceCurrencyCode, targetCurrencyCode))
                .andExpect(result -> status().isNotFound()).andReturn();

        MvcResult result4 = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/2021-11-30",
                        sourceCurrencyCode, sourceCurrencyCode))
                .andExpect(result -> status().isNotFound()).andReturn();

        MvcResult result5 = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/all",
                        sourceCurrencyCode, targetCurrencyCode))
                .andExpect(result -> status().isNotFound()).andReturn();

        MvcResult result6 = mockMvc
                .perform(get("/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/all",
                        sourceCurrencyCode, sourceCurrencyCode))
                .andExpect(result -> status().isNotFound()).andReturn();

        //then
        assertTrue(result1.getResponse().getContentAsString().isEmpty());
        assertTrue(result2.getResponse().getContentAsString().isEmpty());
        assertTrue(result3.getResponse().getContentAsString().isEmpty());
        assertTrue(result4.getResponse().getContentAsString().isEmpty());
        assertTrue(result5.getResponse().getContentAsString().isEmpty());
        assertTrue(result6.getResponse().getContentAsString().isEmpty());
    }


    public RatesControllerTest() {
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }
}