package com.doomedcat17.nbpexchangeapi.services;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkWeekStartDateProviderTest {

    private final WorkWeekStartDateProvider workWeekStartDateProvider = new WorkWeekStartDateProvider();

    @Test
    void shouldProvideCorrectDates() {
        //given
        LocalDate presentDate1 = LocalDate.parse("2021-11-29");
        LocalDate presentDate2 = LocalDate.parse("2021-11-30");
        LocalDate presentDate3 = LocalDate.parse("2021-11-10");
        LocalDate presentDate4 = LocalDate.parse("2021-11-15");
        LocalDate presentDate5 = LocalDate.parse("2021-12-04");


        //then
        assertAll(
                () -> assertEquals(LocalDate.parse("2021-11-19"),
                        workWeekStartDateProvider.get(presentDate1)),
                () -> assertEquals(LocalDate.parse("2021-11-22"),
                        workWeekStartDateProvider.get(presentDate2)),
                () -> assertEquals(LocalDate.parse("2021-11-02"),
                        workWeekStartDateProvider.get(presentDate3)),
                () -> assertEquals(LocalDate.parse("2021-11-05"),
                        workWeekStartDateProvider.get(presentDate4)),
                () -> assertEquals(LocalDate.parse("2021-11-25"),
                        workWeekStartDateProvider.get(presentDate5))
                );
    }

}