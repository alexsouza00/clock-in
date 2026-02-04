package com.clockin.service.utils;

import com.clockin.dto.request.enums.WorkShift;
import com.clockin.exceptions.WorkdayException;
import com.clockin.model.Workday;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkdayUtils {

    private static final LocalTime MIDDAY = LocalTime.NOON;

    public static List<LocalDate> getWorkAbsences(List<Workday> workdays) {

        List<LocalDate> absencesDays = new ArrayList<>();
        LocalDate dayOfTheMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();

        while (dayOfTheMonth.isBefore(today)) {

            if (!isWeekend(dayOfTheMonth)) {
                if (!workdays.contains(dayOfTheMonth)) {
                    absencesDays.add(dayOfTheMonth);
                }
            }
            dayOfTheMonth = dayOfTheMonth.plusDays(1);
        }
        return absencesDays;
    }

    public static boolean isWeekend(LocalDate date) {
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return true;
        } else return false;
    }

    public static void validateNewTime(WorkShift shift, LocalTime newTime, Workday workday) {

        switch (shift) {

            case MORNING_CHECK_IN -> {
                if (!newTime.isBefore(MIDDAY)) {
                    throw new WorkdayException("Morning check-in must be before 12:00");
                }
                if (workday.getMorningCheckOut() != null && newTime.isAfter(workday.getMorningCheckOut())) {
                    throw new WorkdayException("Morning check-in cannot be after check-out");
                }
            }

            case MORNING_CHECK_OUT -> {
                if (!newTime.isBefore(MIDDAY)) {
                    throw new WorkdayException("Morning check-out must be before 12:00");
                }
                if (workday.getMorningCheckIn() != null && newTime.isBefore(workday.getMorningCheckIn())) {
                    throw new WorkdayException("Morning check-out cannot be before check-in");
                }
            }

            case AFTERNOON_CHECK_IN -> {
                if (newTime.isBefore(MIDDAY)) {
                    throw new WorkdayException("Afternoon check-in must be after 12:00");
                }
            }

            case AFTERNOON_CHECK_OUT -> {
                if (workday.getAfternoonCheckIn() != null && newTime.isBefore(workday.getAfternoonCheckIn())) {
                    throw new WorkdayException("Afternoon check-out cannot be before check-in");
                }
            }
        }

    }

    public static String formatMinutesToHours(long totalMinutes) {

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return String.format("%02d:%02d", hours, minutes);
    }
}
