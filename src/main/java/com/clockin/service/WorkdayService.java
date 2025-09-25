package com.clockin.service;

import com.clockin.model.Workday;
import com.clockin.repository.WorkdayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkdayService {

    @Autowired
    private WorkdayRepository repository;

    public List<Workday> getWorkdaySummary(){
        List<Workday> days = repository.findAll();
        return days;
    }

    public void clockIn(){
        Workday workday = new Workday();
        LocalDate workdayDate = LocalDate.now();
        workday.setWorkdayDate(workdayDate);
        workday.setDayOfTheWeek(workdayDate.getDayOfWeek().name());
        repository.save(workday);
    }
}
