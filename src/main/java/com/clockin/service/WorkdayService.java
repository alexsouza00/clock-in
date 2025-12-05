package com.clockin.service;

import com.clockin.dto.response.WorkStats;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.WorkdayException;
import com.clockin.exceptions.WorkdayFullException;
import com.clockin.model.Employee;
import com.clockin.model.Workday;
import com.clockin.repository.EmployeeRepository;
import com.clockin.repository.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public WorkdayService(WorkdayRepository workdayRepository, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.workdayRepository = workdayRepository;
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

    public void registerWorkday(Long employeeId) {

        if (employeeRepository.existsById(employeeId)) {

            Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
            Optional<Workday> workday = findWorkday(employeeId, LocalDate.now());

            if (workday.isEmpty()) {
                Workday newWorkday = new Workday();
                LocalDate workdayDate = LocalDate.now();
                newWorkday.setEmployee(employeeService.getEmployeeById(employeeId));
                newWorkday.setWorkdayDate(workdayDate);
                newWorkday.setDayOfTheWeek(workdayDate.getDayOfWeek().name());
                newWorkday.setMorningCheckIn(LocalTime.now());
                workdayRepository.save(newWorkday);
            } else {
                throw new WorkdayException("workday already recorded!");
            }
        }
    }

    public void clockIn(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        Optional<Workday> workday = findWorkday(employeeId, LocalDate.now());

        if (workday.isEmpty()) {
            throw new WorkdayException("No workdays recorded to be updated!");
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() == null) {
            workday.get().setMorningCheckOut(LocalTime.now());
            workdayRepository.save(workday.get());
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() == null && workday.get().getAfternoonCheckOut() == null) {
            workday.get().setAfternoonCheckIn(LocalTime.now());
            workdayRepository.save(workday.get());
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() != null && workday.get().getAfternoonCheckOut() == null) {
            workday.get().setAfternoonCheckOut(LocalTime.now());
            workdayRepository.save(workday.get());
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() != null && workday.get().getAfternoonCheckOut() != null) {
            throw new WorkdayFullException();
        }
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
