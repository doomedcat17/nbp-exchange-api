package com.doomedcat17.nbpexchangeapi.services;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class WorkWeekStartDateProvider {

    public LocalDate get(LocalDate now) {
        LocalDate startDate = now;
        int remainingDays = 7;
        do {
            if (remainingDays != 7) startDate = startDate.minusDays(1);
            if (isNotHoliday(startDate)) {
                remainingDays--;
            }
        } while (remainingDays != 0);
        return startDate;
    }

    private boolean isNotHoliday(LocalDate date) {
        return !date.getDayOfWeek().equals(DayOfWeek.SUNDAY) &&
                !date.getDayOfWeek().equals(DayOfWeek.SATURDAY);
    }
}
