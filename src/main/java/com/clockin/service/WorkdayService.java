package com.clockin.service;

import com.clockin.dto.request.WorkdayUpdateDto;
import com.clockin.dto.response.WorkStats;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.WorkdayException;
import com.clockin.exceptions.WorkdayFullException;
import com.clockin.model.Employee;
import com.clockin.model.Workday;
import com.clockin.model.enums.ContractType;
import com.clockin.repository.WorkdayRepository;
import com.clockin.service.utils.WorkdayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

import static com.clockin.service.utils.WorkdayUtils.formatMinutesToHours;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final EmployeeService employeeService;
    private final WorkdayUtils workdayUtils;

    public WorkdayService(WorkdayRepository workdayRepository, WorkdayUtils workdayUtils, EmployeeService employeeService) {
        this.workdayRepository = workdayRepository;
        this.workdayUtils = workdayUtils;
        this.employeeService = employeeService;
    }

    public List<Workday> getAllWorkdaysByEmployee(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return workdayRepository.findByEmployeeId(employeeId);
    }

    public Optional<Workday> findWorkday(Long employeeId, LocalDate localdate) {
        return workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, localdate);
    }

    @Transactional
    public void registerWorkday(Long employeeId) {

        LocalDate today = LocalDate.now();

        if (today.getDayOfWeek().equals(DayOfWeek.SATURDAY) || today.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new WorkdayException("You Can't create a workday on weekends!");
        }

        LocalTime now = LocalTime.now();
        Employee employee = employeeService.getEmployeeById(employeeId);

        Workday workday = workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, today).orElseGet(() -> {
            Workday newDay = new Workday();
            newDay.setEmployee(employee);
            newDay.setWorkdayDate(today);
            newDay.setDayOfTheWeek(today.getDayOfWeek().name());
            return workdayRepository.save(newDay);
        });

        fillHoursWorked(workday, employee.getContractType(), now);
        workdayRepository.save(workday);

    }

    private void fillHoursWorked(Workday workday, ContractType type, LocalTime now) {

        LocalTime MIDDAY = LocalTime.NOON;

        if (type == ContractType.CLT) {
            if (workday.getMorningCheckIn() == null) workday.setMorningCheckIn(now);
            else if (workday.getMorningCheckOut() == null) workday.setMorningCheckOut(now);
            else if (workday.getAfternoonCheckIn() == null) workday.setAfternoonCheckIn(now);
            else if (workday.getAfternoonCheckOut() == null) workday.setAfternoonCheckOut(now);
            else throw new WorkdayFullException();
        } else if (type == ContractType.ESTAGIO) {
            if (now.isBefore(MIDDAY)) {
                if (workday.getMorningCheckIn() == null) workday.setMorningCheckIn(now);
                else if (workday.getMorningCheckOut() == null) workday.setMorningCheckOut(now);
                else throw new WorkdayFullException();
            } else if (workday.getAfternoonCheckIn() == null) workday.setAfternoonCheckIn(now);
            else if (workday.getAfternoonCheckOut() == null) workday.setAfternoonCheckOut(now);
            else throw new WorkdayFullException();
        }

    }

    public void updateWorkday(WorkdayUpdateDto workdayUpdateDto) {

        Employee employee = employeeService.getEmployeeById(workdayUpdateDto.employeeId());
        Workday workday = workdayRepository.findByEmployeeIdAndWorkdayDate(workdayUpdateDto.employeeId(), workdayUpdateDto.workdayDate()).orElseThrow(() -> new WorkdayException("Workday not found"));

        workdayUtils.validateNewTime(workdayUpdateDto.workShift(), workdayUpdateDto.newTime(), workday);

        switch (workdayUpdateDto.workShift()) {
            case MORNING_CHECK_IN -> workday.setMorningCheckIn(workdayUpdateDto.newTime());

            case MORNING_CHECK_OUT -> workday.setMorningCheckOut(workdayUpdateDto.newTime());

            case AFTERNOON_CHECK_IN -> workday.setAfternoonCheckIn(workdayUpdateDto.newTime());

            case AFTERNOON_CHECK_OUT -> workday.setAfternoonCheckOut(workdayUpdateDto.newTime());
        }

        workdayRepository.save(workday);

    }

    public WorkStats getWorkStatsByEmployee(Long employeeId) {

        if (employeeService.getEmployeeById(employeeId) == null) {
            throw new EmployeeNotFoundException();
        }

        List<Workday> allWorkdays = workdayRepository.findByEmployeeId(employeeId);

        LocalDate today = LocalDate.now();
        Month currentMonth = LocalDate.now().getMonth();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() % 7);

        long minutesWorkedToday = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().equals(today)).mapToLong(this::timeWorked).sum();
        long minutesWorkedInTheWeek = allWorkdays.stream().filter(workday -> !workday.getWorkdayDate().isBefore(startOfWeek)).mapToLong(this::timeWorked).sum();
        long minutesWorkedInTheMonth = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().getMonth().equals(currentMonth)).mapToLong(this::timeWorked).sum();
        long extraMinutesWorkedInTheMonth = extraHours(minutesWorkedInTheMonth);
        long minutesLateInTheMonth = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().getMonth().equals(currentMonth)).mapToLong(this::lateHours).sum();

        List<LocalDate> missingDays = missingDays(allWorkdays);


        return new WorkStats(employeeService.getEmployeeById(employeeId).getName(),
                formatMinutesToHours(minutesWorkedInTheMonth),
                formatMinutesToHours(minutesWorkedInTheWeek),
                formatMinutesToHours(minutesWorkedToday),
                formatMinutesToHours(extraMinutesWorkedInTheMonth),
                formatMinutesToHours(minutesLateInTheMonth),
                missingDays.size());
    }

    public long timeWorked(Workday day) {

        long totalMinutes = 0;

        if (day.getMorningCheckIn() != null && day.getMorningCheckOut() != null) {
            long minutes = Duration.between(day.getMorningCheckIn(), day.getMorningCheckOut()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }
        if (day.getAfternoonCheckIn() != null && day.getAfternoonCheckOut() != null) {
            long minutes = Duration.between(day.getAfternoonCheckIn(), day.getAfternoonCheckOut()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }

        return totalMinutes;
    }

    public long lateHours(Workday day) {

        LocalTime morningCheckIn = LocalTime.of(8, 0);
        LocalTime afternoonCheckIn = LocalTime.of(13, 0);
        long totalMinutes = 0;

        if (day.getMorningCheckIn() != null) {
            long minutes = Duration.between(morningCheckIn, day.getMorningCheckIn()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }
        if (day.getAfternoonCheckIn() != null) {
            long minutes = Duration.between(afternoonCheckIn, day.getAfternoonCheckIn()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }

        return totalMinutes;
    }

    public long extraHours(long minutesWorkedInTheMonth) {
        return Math.max(0, minutesWorkedInTheMonth - 9600);
    }

    public List<LocalDate> missingDays(List<Workday> workdays) {

        List<LocalDate> missingDays = new ArrayList<>();
        Set<LocalDate> workedDates = new HashSet<>();

        for (Workday wd : workdays) {
            workedDates.add(wd.getWorkdayDate());
        }

        LocalDate date = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();

        while (date.isBefore(today)) {

            if (!date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                if (!workedDates.contains(date)) {
                    missingDays.add(date);
                }
            }
            date = date.plusDays(1);
        }
        return missingDays;
    }
}

