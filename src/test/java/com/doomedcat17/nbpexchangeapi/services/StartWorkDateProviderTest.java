package com.doomedcat17.nbpexchangeapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StartWorkDateProviderTest {

    private final StartWorkDateProvider startWorkDateProvider = new StartWorkDateProvider();

    @ParameterizedTest
    @CsvSource(value ={
            "2021-11-29,7,2021-11-19", "2021-11-30,7,2021-11-22",
            "2021-11-10,7,2021-11-02", "2021-11-15,7,2021-11-05",
            "2021-12-04,7,2021-11-25"
    })
    void shouldProvideCorrectDates(String textPresentDate, int numberOfWorkDays, String textExpectedStartDate) {
        //given
        LocalDate presentDate = LocalDate.parse(textPresentDate);

        //when
        LocalDate startDate = startWorkDateProvider.get(presentDate, numberOfWorkDays);

        //then
        assertEquals(LocalDate.parse(textExpectedStartDate), startDate);
    }

}