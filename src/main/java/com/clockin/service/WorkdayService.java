package com.clockin.service;

import com.clockin.dto.request.WorkdayUpdateDto;
import com.clockin.dto.response.EmployeeResponse;
import com.clockin.dto.response.WorkStats;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.WorkdayException;
import com.clockin.model.Employee;
import com.clockin.model.Workday;
import com.clockin.repository.EmployeeRepository;
import com.clockin.repository.WorkdayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

import static com.clockin.service.utils.WorkdayUtils.*;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final EmployeeRepository employeeRepository;

    public WorkdayService(WorkdayRepository workdayRepository, EmployeeRepository employeeRepository) {
        this.workdayRepository = workdayRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Workday> getAllWorkdaysByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        return workdayRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public void registerWorkday(Long employeeId) {

        LocalDate today = LocalDate.now();

        if (isWeekend(today)) {
            throw new WorkdayException("You Can't create a workday on weekends!");
        }

        LocalTime now = LocalTime.now();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);

        Workday workday = workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, today).orElseGet(() -> {
            Workday newDay = new Workday();
            newDay.setEmployee(employee);
            newDay.setWorkdayDate(today);
            newDay.setDayOfTheWeek(today.getDayOfWeek().name());
            return newDay;
        });

        workday.processPunch(now);
        workdayRepository.save(workday);
    }

    public WorkStats getWorkStatsByEmployeeId(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        List<Workday> allWorkdays = workdayRepository.findByEmployeeId(employee.getId());

        LocalDate today = LocalDate.now();
        Month currentMonth = LocalDate.now().getMonth();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() % 7);

        long minutesWorkedToday = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().equals(today)).mapToLong(Workday::getMinutesWorked).sum();
        long minutesWorkedInTheWeek = allWorkdays.stream().filter(workday -> !workday.getWorkdayDate().isBefore(startOfWeek)).mapToLong(Workday::getMinutesWorked).sum();
        long minutesWorkedInTheMonth = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().getMonth().equals(currentMonth)).mapToLong(Workday::getMinutesWorked).sum();
        long extraMinutesWorkedInTheMonth = allWorkdays.stream().filter((workday -> workday.getWorkdayDate().getMonth().equals(currentMonth))).mapToLong(Workday::getExtraMinutes).sum();
        long minutesLateInTheMonth = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().getMonth().equals(currentMonth)).mapToLong(Workday::getLateMinutes).sum();

        List<Workday> daysWorkedInTheMonth = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().getMonth().equals(currentMonth)).toList();

        return new WorkStats(employee.getName()
                , formatMinutesToHours(minutesWorkedInTheMonth)
                , formatMinutesToHours(minutesWorkedInTheWeek)
                , formatMinutesToHours(minutesWorkedToday)
                , formatMinutesToHours(extraMinutesWorkedInTheMonth)
                , formatMinutesToHours(minutesLateInTheMonth)
                , getWorkAbsences(daysWorkedInTheMonth).size());
    }

    public void updateWorkday(WorkdayUpdateDto workdayUpdateDto) {

        Employee employee = employeeRepository.findById(workdayUpdateDto.employeeId()).orElseThrow(EmployeeNotFoundException::new);
        Workday workday = workdayRepository.findByEmployeeIdAndWorkdayDate(employee.getId(), workdayUpdateDto.workdayDate()).orElseThrow(() -> new WorkdayException("Workday not found"));

        validateNewTime(workdayUpdateDto.workShift(), workdayUpdateDto.newTime(), workday);

        switch (workdayUpdateDto.workShift()) {
            case MORNING_CHECK_IN -> workday.setMorningCheckIn(workdayUpdateDto.newTime());

            case MORNING_CHECK_OUT -> workday.setMorningCheckOut(workdayUpdateDto.newTime());

            case AFTERNOON_CHECK_IN -> workday.setAfternoonCheckIn(workdayUpdateDto.newTime());

            case AFTERNOON_CHECK_OUT -> workday.setAfternoonCheckOut(workdayUpdateDto.newTime());
        }

        workdayRepository.save(workday);

    }
}

