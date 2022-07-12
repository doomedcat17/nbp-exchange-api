package com.doomedcat17.nbpexchangeapi.mapper;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.doomedcat17.nbpexchangeapi.data.dto.RateDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {CurrencyMapper.class})
public interface NbpExchangeRateMapper {


    NbpExchangeRateMapper INSTANCE = Mappers.getMapper(NbpExchangeRateMapper.class);

    @Mapping(target = "midRateInPLN", source = "jsonNode", qualifiedByName = "midRateInPLN")
    @Mapping(target = "effectiveDate", source = "effectiveDate")
    @Mapping(target = "currency", source = "jsonNode")
    NbpExchangeRate fromJson(JsonNode jsonNode, LocalDate effectiveDate);

    @Mapping(target = "rate", source = "exchangeRateToMap", qualifiedByName = "rate")
    @Mapping(target = "code", source = "exchangeRateToMap.currency.code")
    RateDTO toRateDto(NbpExchangeRate exchangeRateToMap, @Context NbpExchangeRate baseExchangeRate);

    @Named("rate")
    default BigDecimal mapRate(NbpExchangeRate exchangeRateToMap, @Context NbpExchangeRate baseExchangeRate) {
        return baseExchangeRate.getMidRateInPLN()
                .divide(exchangeRateToMap.getMidRateInPLN(), RoundingMode.HALF_EVEN);
    }

    @Named("midRateInPLN")
    default BigDecimal mapMid(JsonNode jsonNode) {
        return new BigDecimal(jsonNode.get("mid").asText());
    }
}
