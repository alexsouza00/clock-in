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
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        Optional<Workday> workday = findWorkday(employeeId, today);

        if (workday.isPresent()) {
            throw new WorkdayException("Workday already recorded!");
        }

        Workday newWorkday = new Workday();
        newWorkday.setEmployee(employeeService.getEmployeeById(employeeId));
        newWorkday.setWorkdayDate(today);
        newWorkday.setDayOfTheWeek(today.getDayOfWeek().name());

        if (LocalTime.now().isAfter((LocalTime.of(12, 00)))) {
            newWorkday.setAfternoonCheckIn(LocalTime.now());
        } else {
            newWorkday.setMorningCheckIn(LocalTime.now());
        }

        workdayRepository.save(newWorkday);

    }

    @Transactional
    public void clockIn(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        Optional<Workday> workday = findWorkday(employeeId, LocalDate.now());

        if (workday.isEmpty()) {
            throw new WorkdayException("No workdays recorded to be updated!");
        }

        if ((workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null) || (workday.get().getAfternoonCheckIn() != null && workday.get().getAfternoonCheckOut() != null)) {
            throw new WorkdayFullException();
        }

        if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() != null && workday.get().getAfternoonCheckOut() != null) {
            throw new WorkdayFullException();
        }

        if (employee.getContractType().equals(ContractType.CLT)) {

            if (workday.get().getMorningCheckIn() == null) {
                workday.get().setAfternoonCheckOut(LocalTime.now());
                workdayRepository.save(workday.get());
            }

            if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() == null && workday.get().getAfternoonCheckIn() == null) {
                workday.get().setMorningCheckOut(LocalTime.now());
                workdayRepository.save(workday.get());
            }

            if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() == null && workday.get().getAfternoonCheckOut() == null) {
                workday.get().setAfternoonCheckIn(LocalTime.now());
                workdayRepository.save(workday.get());
            }

            if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() != null && workday.get().getAfternoonCheckOut() == null) {
                workday.get().setAfternoonCheckOut(LocalTime.now());
                workdayRepository.save(workday.get());
            }
        }

        if (employee.getContractType().equals(ContractType.ESTAGIO)) {
            if (workday.get().getMorningCheckIn() == null) {
                workday.get().setAfternoonCheckOut(LocalTime.now());
                workdayRepository.save(workday.get());
            } else {
                workday.get().setMorningCheckOut(LocalTime.now());
                workdayRepository.save(workday.get());
            }
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

    public WorkStats workStats(Long employeeId) {

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

        return new WorkStats(employeeService.getEmployeeById(employeeId).getName(), timeFormatter(minutesWorkedInTheMonth), timeFormatter(minutesWorkedInTheWeek), timeFormatter(minutesWorkedToday));

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

    public String timeFormatter(long totalMinutes) {

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return String.format("%02d:%02d", hours, minutes);
    }
}

