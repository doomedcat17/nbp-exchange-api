package com.doomedcat17.nbpexchangeapi.mapper;

import com.doomedcat17.nbpexchangeapi.data.NbpExchangeRate;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {CurrencyMapper.class})
public interface NbpExchangeRateMapper {

    @Mapping(target = "midRateInPLN", source = "jsonNode", qualifiedByName = "midRateInPLN")
    @Mapping(target = "effectiveDate", source = "effectiveDate")
    @Mapping(target = "currency", source = "jsonNode")
    NbpExchangeRate fromJson(JsonNode jsonNode, LocalDate effectiveDate);

    @Named("midRateInPLN")
    default BigDecimal mapMid(JsonNode jsonNode) {
        return new BigDecimal(jsonNode.get("mid").asText());
    }
}
