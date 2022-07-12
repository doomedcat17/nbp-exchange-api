package com.doomedcat17.nbpexchangeapi.mapper;

import com.doomedcat17.nbpexchangeapi.data.domain.CurrencyTransaction;
import com.doomedcat17.nbpexchangeapi.data.dto.TransactionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CurrencyTransactionMapper {

    @Mapping(target = "buyCode", source = "currencyTransaction.buyCurrency.code")
    @Mapping(target = "sellCode", source = "currencyTransaction.sellCurrency.code")
    @Mapping(target = "buyAmount", source = "currencyTransaction.boughtAmount")
    @Mapping(target = "sellAmount", source = "currencyTransaction.soldAmount")
    TransactionDto toDto(CurrencyTransaction currencyTransaction);
}
