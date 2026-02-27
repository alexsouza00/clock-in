package com.clockin.service.utils;

import com.clockin.dto.request.enums.WorkShift;
import com.clockin.exceptions.WorkdayException;
import com.clockin.model.Employee;
import com.clockin.model.Workday;
import com.clockin.model.enums.ContractType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

class WorkdayUtilsTest {

    /*
    //O teste precisa ser alterado para funcionar, O metodo roda pegando o dia atual e verificando cada dia do mês.
    @Test
    void getWorkAbsences() {
        Employee employee = new Employee("Alex", ContractType.CLT);
        Workday workday = new Workday(employee, LocalDate.of(2026, 2, 2), "THURSDAY",
                LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(17, 0));
        List<Workday> workdays = new ArrayList<>();
        workdays.add(workday);

        List<LocalDate> workAbsences = WorkdayUtils.getWorkAbsences(workdays);


        Assertions.assertEquals(18, workAbsences.size());
    }
    */

    @Test
    @DisplayName("Is a Weekend")
    void isWeekend() {
        boolean weekend = WorkdayUtils.isWeekend(LocalDate.of(2026, 2, 7));
        Assertions.assertTrue(weekend);
    }

    @Test
    @DisplayName("Isn't a Weekend")
    void isWeekend2() {
        boolean noWeekend = WorkdayUtils.isWeekend(LocalDate.of(2026, 2, 6));
        Assertions.assertFalse(noWeekend);
    }

    @Test
    @DisplayName("Throws Exceptions")
    void validateNewTime() {
        Employee employee = new Employee("Alex", ContractType.CLT);
        Workday workday = new Workday(employee, LocalDate.of(2026, 2, 2), "THURSDAY",
                LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(17, 0));

        Assertions.assertThrows(WorkdayException.class, () -> WorkdayUtils.validateNewTime(WorkShift.MORNING_CHECK_IN, LocalTime.of(13, 0), workday));
        Assertions.assertThrows(WorkdayException.class, () -> WorkdayUtils.validateNewTime(WorkShift.MORNING_CHECK_OUT, LocalTime.of(7, 0), workday));
        Assertions.assertThrows(WorkdayException.class, () -> WorkdayUtils.validateNewTime(WorkShift.AFTERNOON_CHECK_IN, LocalTime.of(7, 0), workday));
        Assertions.assertThrows(WorkdayException.class, () -> WorkdayUtils.validateNewTime(WorkShift.AFTERNOON_CHECK_OUT, LocalTime.of(12, 30), workday));

    }

    @Test
    @DisplayName("Minutes to Hours")
    void formatMinutesToHoursTest() {
        String hours = WorkdayUtils.formatMinutesToHours(500);
        Assertions.assertEquals("08:20", hours);
    }
}