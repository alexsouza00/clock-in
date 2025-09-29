package com.clockin.service;

import com.clockin.model.Workday;
import com.clockin.repository.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final EmployeeService employeeService;

    public WorkdayService(WorkdayRepository workdayRepository,
                          EmployeeService employeeService) {
        this.workdayRepository = workdayRepository;
        this.employeeService = employeeService;
    }

    public List<Workday> getWorkdaySummary(){
        List<Workday> days = workdayRepository.findAll();
        return days;
    }

    public void registerWorkday(Long employeeId){

        Workday workday = new Workday();
        LocalDate workdayDate = LocalDate.now();
        workday.setEmployee(employeeService.getEmployeeById(employeeId));
        workday.setWorkdayDate(workdayDate);
        workday.setDayOfTheWeek(workdayDate.getDayOfWeek().name());
        workday.setMorningCheckIn(OffsetDateTime.now());
        workdayRepository.save(workday);

    }
}
