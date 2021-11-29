package com.doomedcat17.nbpexchangeapi.services;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkWeekStartDateProviderTest {

    private WorkWeekStartDateProvider workWeekStartDateProvider = new WorkWeekStartDateProvider();

    @Test
    void shouldProvideCorrectDate() {
        //given
        LocalDate presentDate = LocalDate.parse("2021-11-29");

        //when
        LocalDate workWeekStartDate = workWeekStartDateProvider.get(presentDate);

        //then
        assertEquals(LocalDate.parse("2021-11-18"), workWeekStartDate);
    }

}