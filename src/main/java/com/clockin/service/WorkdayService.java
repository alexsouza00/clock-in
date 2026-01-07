package com.clockin.service;

import com.clockin.dto.request.WorkdayUpdateDto;
import com.clockin.dto.response.WorkStats;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.WorkdayException;
import com.clockin.exceptions.WorkdayFullException;
import com.clockin.model.Employee;
import com.clockin.model.Workday;
import com.clockin.model.enums.ContractType;
import com.clockin.repository.EmployeeRepository;
import com.clockin.repository.WorkdayRepository;
import com.clockin.service.utils.WorkdayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static com.clockin.service.utils.WorkdayUtils.formatMinutesToHours;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final WorkdayUtils workdayUtils;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public WorkdayService(WorkdayRepository workdayRepository, WorkdayUtils workdayUtils, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.workdayRepository = workdayRepository;
        this.workdayUtils = workdayUtils;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    public List<Workday> getAllWorkdaysByEmployee(Long employeeId) {

        if (employeeRepository.existsById(employeeId)) {
            return workdayRepository.findByEmployeeId(employeeId);
        } else throw new EmployeeNotFoundException();

    }

    public Optional<Workday> findWorkday(Long employeeId, LocalDate localdate) {
        return workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, localdate);
    }

   @Transactional
    public void registerWorkday(Long employeeId) {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);

        Workday workday = workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, today)
                .orElseGet(() -> {
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
            if(now.isBefore(MIDDAY)){
            if (workday.getMorningCheckIn() == null) workday.setMorningCheckIn(now);
            else if (workday.getMorningCheckOut() == null) workday.setMorningCheckOut(now);
            else throw new WorkdayFullException();}
            else if  (workday.getAfternoonCheckIn() == null) workday.setAfternoonCheckIn(now);
            else if (workday.getAfternoonCheckOut() == null) workday.setAfternoonCheckOut(now);
            else throw new WorkdayFullException();
        }

    }

    public void updateWorkday(WorkdayUpdateDto workdayUpdateDto) {

        Employee employee = employeeRepository.findById(workdayUpdateDto.employeeId()).orElseThrow(EmployeeNotFoundException::new);

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

        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException();
        }

        List<Workday> allWorkdays = workdayRepository.findByEmployeeId(employeeId);

        LocalDate today = LocalDate.now();
        Month currentMonth = LocalDate.now().getMonth();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() % 7);

        long minutesWorkedToday = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().equals(today)).mapToLong(this::timeWorked).sum();
        long minutesWorkedInTheWeek = allWorkdays.stream().filter(workday -> !workday.getWorkdayDate().isBefore(startOfWeek)).mapToLong(this::timeWorked).sum();
        long minutesWorkedInTheMonth = allWorkdays.stream().filter(workday -> workday.getWorkdayDate().getMonth().equals(currentMonth)).mapToLong(this::timeWorked).sum();

        return new WorkStats(employeeService.getEmployeeById(employeeId).getName(),
                formatMinutesToHours(minutesWorkedInTheMonth),
                formatMinutesToHours(minutesWorkedInTheWeek),
                formatMinutesToHours(minutesWorkedToday));
    }

    public long timeWorked(Workday day) {

        Duration morningDuration = Duration.ofMinutes(0);
        Duration afternoonDuration = Duration.ofMinutes(0);

        if (day.getMorningCheckIn() != null && day.getMorningCheckOut() != null) {
            morningDuration = Duration.between(day.getMorningCheckIn(), day.getMorningCheckOut());
        }
        if (day.getAfternoonCheckIn() != null && day.getAfternoonCheckOut() != null) {
            afternoonDuration = Duration.between(day.getAfternoonCheckIn(), day.getAfternoonCheckOut());
        }
        return morningDuration.toMinutes() + afternoonDuration.toMinutes();

    }
}

