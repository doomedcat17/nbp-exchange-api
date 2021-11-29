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
            if (!startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !startDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                remainingDays--;
            }
            startDate = startDate.minusDays(1);
        } while (remainingDays != 0);
        return startDate;
    }
}
