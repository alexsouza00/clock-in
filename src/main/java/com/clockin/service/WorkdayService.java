package com.clockin.service;

import com.clockin.model.Employee;
import com.clockin.model.Workday;
import com.clockin.repository.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final EmployeeService employeeService;

    public WorkdayService(WorkdayRepository workdayRepository,
                          EmployeeService employeeService) {
        this.workdayRepository = workdayRepository;
        this.employeeService = employeeService;
    }

    public Optional<Workday> findWorkday(Long employeeId, LocalDate localdate) {
        Optional<Workday> workday = workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, localdate);
        return workday;
    }

    public void registerWorkday(Long employeeId) {

        Optional<Workday> workday = findWorkday(employeeId, LocalDate.now());

        if (workday.isEmpty()) {
            Workday newWorkday = new Workday();
            LocalDate workdayDate = LocalDate.now();
            newWorkday.setEmployee(employeeService.getEmployeeById(employeeId));
            newWorkday.setWorkdayDate(workdayDate);
            newWorkday.setDayOfTheWeek(workdayDate.getDayOfWeek().name());
            newWorkday.setMorningCheckIn(LocalTime.now());
            workdayRepository.save(newWorkday);
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() == null) {
            workday.get().setMorningCheckOut(LocalTime.now());
            workdayRepository.save(workday.get());
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() == null && workday.get().getAfternoonCheckOut() == null) {
            workday.get().setAfternoonCheckIn(LocalTime.now());
            workdayRepository.save(workday.get());
        } else if (workday.get().getMorningCheckIn() != null && workday.get().getMorningCheckOut() != null && workday.get().getAfternoonCheckIn() != null && workday.get().getAfternoonCheckOut() == null) {
            workday.get().setAfternoonCheckOut(LocalTime.now());
            workdayRepository.save(workday.get());
        }

    }
}
