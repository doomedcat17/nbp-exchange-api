package com.doomedcat17.nbpexchangeapi.controllers;

import com.doomedcat17.nbpexchangeapi.data.dto.PageDto;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
                .perform(get("/api/rates/recent").param("currency", currencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        PageDto<RateDto> pageDto = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageDto<RateDto>>() {
        });
        //then

        List<RateDto> rates = pageDto.getResults();

        assertTrue(rates.stream().allMatch(rateDto -> rateDto.getCode().equals(currencyCode)));
        assertEquals(4, rates.size());
        assertTrue(rates.stream().allMatch(
                rateDTO ->
                        rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-30"))
                                || (rateDTO.getEffectiveDate().equals(LocalDate.parse("2021-11-25")) && rateDTO.getTargetCode().equals("AFN"))));
        assertTrue(rates.stream().noneMatch(rate -> rate.getTargetCode().equals("USD")));
    }


    @SneakyThrows
    @Test
    void shouldReturnNoRates() {
        //given
        String currencyCode = "XDD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/recent").param("currency", currencyCode))
                .andExpect(result1 -> status().isNotFound()).andReturn();

        //then
        JsonNode arrayNode = objectMapper.readTree(result.getResponse().getContentAsString()).get("results");
        assertTrue(arrayNode.isEmpty());
    }

    @SneakyThrows
    @Test
    void shouldReturnNoRates2() {
        objectMapper.findAndRegisterModules();
        //given
        String currencyCode = "XDD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates").param("currency", currencyCode))
                .andExpect(result1 -> status().isNotFound()).andReturn();

        //then
        JsonNode arrayNode = objectMapper.readTree(result.getResponse().getContentAsString()).get("results");
        assertTrue(arrayNode.isEmpty());
    }

    @SneakyThrows
    @Test
    void shouldReturnAllRatesForGivenCode() {
        //given
        String currencyCode = "USD";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates").param("currency", currencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        PageDto<RateDto> pageDto = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageDto<RateDto>>() {
        });

        List<RateDto> rates = pageDto.getResults();

        assertEquals(26, rates.size());
        assertTrue(rates.stream().allMatch(rate -> rate.getCode().equals("USD")));
        assertTrue(rates.stream().noneMatch(rate -> rate.getTargetCode().equals("USD")));
    }

    @SneakyThrows
    @Test
    void shouldReturnRecentExchangeRatesForGivenCodes() {
        //given
        String sourceCurrencyCode = "USD";

        String targetCurrencyCode = "PLN";

        //when
        MvcResult result = mockMvc
                .perform(get("/api/rates/recent",
                        sourceCurrencyCode, targetCurrencyCode)
                        .param("currency", sourceCurrencyCode)
                        .param("targetCurrency", targetCurrencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        PageDto<RateDto> pageDto = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageDto<RateDto>>() {
        });

        assertTrue(pageDto.getResults().stream().noneMatch(rate -> rate.getTargetCode().equals(sourceCurrencyCode)));
        List<RateDto> rates = pageDto.getResults();

        assertEquals(1, rates.size());

        RateDto rateDTO = rates.get(0);
        assertEquals(targetCurrencyCode, rateDTO.getTargetCode());
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
                .perform(get("/api/rates",
                        sourceCurrencyCode, targetCurrencyCode)
                        .param("currency", sourceCurrencyCode)
                        .param("targetCurrency", targetCurrencyCode))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        PageDto<RateDto> pageDto = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageDto<RateDto>>() {
        });

        assertTrue(pageDto.getResults().stream().noneMatch(rate -> rate.getTargetCode().equals(sourceCurrencyCode)));
        List<RateDto> rates = pageDto.getResults();

        assertEquals(8, rates.size());
        assertTrue(rates.stream().allMatch(rateDTO -> rateDTO.getTargetCode().equals(targetCurrencyCode)));
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
                .perform(get("/api/rates",
                        sourceCurrencyCode, targetCurrencyCode)
                        .param("currency", sourceCurrencyCode)
                        .param("targetCurrency", targetCurrencyCode)
                        .param("effectiveDate", date.toString()))
                .andExpect(result1 -> status().isOk()).andReturn();

        //then
        PageDto<RateDto> pageDto = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageDto<RateDto>>() {
        });

        assertTrue(pageDto.getResults().stream().noneMatch(rate -> rate.getTargetCode().equals(sourceCurrencyCode)));
        List<RateDto> rates = pageDto.getResults();

        assertEquals(1, rates.size());

        assertTrue(rates.stream().allMatch(rateDTO -> rateDTO.getTargetCode().equals(targetCurrencyCode)));
        assertTrue(rates.stream().allMatch(rateDto -> rateDto.getEffectiveDate().equals(date)));
    }
}