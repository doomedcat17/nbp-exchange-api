package com.doomedcat17.nbpexchangeapi.services;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class StartWorkDateProvider {

    public LocalDate get(LocalDate now, int workdaysNumber) {
        int remainingDays = workdaysNumber;
        LocalDate startDate;
        LocalDate temp = now;
        do {
            startDate = temp;
            if (isNotHoliday(startDate)) {
                remainingDays--;
            }
            temp = temp.minusDays(1);
        } while (remainingDays != 0);
        return startDate;
    }

    private boolean isNotHoliday(LocalDate date) {
        return !date.getDayOfWeek().equals(DayOfWeek.SUNDAY) &&
                !date.getDayOfWeek().equals(DayOfWeek.SATURDAY);
    }
}
