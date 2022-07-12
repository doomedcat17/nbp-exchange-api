package com.doomedcat17.nbpexchangeapi.mapper;

import com.doomedcat17.nbpexchangeapi.data.domain.Currency;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CurrencyMapper {

    @Mapping(target = "code", source = "jsonNode", qualifiedByName = "code")
    @Mapping(target = "name", source = "jsonNode", qualifiedByName = "name")
    Currency fromJson(JsonNode jsonNode);

    @Named("code")
    default String mapCode(JsonNode jsonNode) {
        return jsonNode.get("code").asText();
    }

    @Named("name")
    default String mapName(JsonNode jsonNode) {
        return jsonNode.get("currency").asText();
    }


}
