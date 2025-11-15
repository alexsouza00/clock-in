package com.clockin.service;

import com.clockin.dto.response.WorkStats;
import com.clockin.model.Workday;
import com.clockin.repository.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkdayService {

    private final WorkdayRepository workdayRepository;
    private final EmployeeService employeeService;

    public WorkdayService(WorkdayRepository workdayRepository, EmployeeService employeeService) {
        this.workdayRepository = workdayRepository;
        this.employeeService = employeeService;
    }

    public List<Workday> getAllWorkdaysByEmployee(Long employeeId){
        List<Workday> workdays = workdayRepository.findByEmployeeId(employeeId);
        return workdays;
    }

    public Optional<Workday> findWorkday(Long employeeId, LocalDate localdate) {
        Optional<Workday> workday = workdayRepository.findByEmployeeIdAndWorkdayDate(employeeId, localdate);
        if (workday.isEmpty()){
        }
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

    public WorkStats workStats(Long id) {

        List<Workday> allWorkdays = workdayRepository.findByEmployeeId(id);

        int hoursWorkedInTheMonth = 0;
        int minutesWorkedInTheMonth = 0;
        int hoursWorkedInTheWeek = 0;
        int minutesWorkedInTheWeek = 0;
        int hoursWorkedInTheDay = 0;
        int minutesWorkedInTheDay = 0;
        double totalTime = 0.0;
        double totalTimeDay = 0.0;
        double totalTimeWeek = 0.0;


        for (Workday day : allWorkdays) {

            if (day.getWorkdayDate().getMonth() == LocalDate.now().getMonth()) {
                Duration morningDuration = Duration.between(day.getMorningCheckIn(), day.getMorningCheckOut());
                totalTime += morningDuration.toMinutes();
                Duration afternoonDuration = Duration.between(day.getAfternoonCheckIn(), day.getAfternoonCheckOut());
                totalTime += afternoonDuration.toMinutes();
            }

            hoursWorkedInTheMonth = (int) (totalTime / 60);
            minutesWorkedInTheMonth = (int) (totalTime % 60);

            if (day.getWorkdayDate().getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
                for (int i = 0; i < 7; i++) {
                    Optional<Workday> weekDays = findWorkday(day.getEmployee().getId(), day.getWorkdayDate().minusDays(i));
                    if (weekDays.isEmpty()) {
                    } else {
                        Duration morningDuration = Duration.between(weekDays.get().getMorningCheckIn(), weekDays.get().getMorningCheckOut());
                        totalTimeWeek += morningDuration.toMinutes();
                        Duration afternoonDuration = Duration.between(weekDays.get().getAfternoonCheckIn(), weekDays.get().getAfternoonCheckOut());
                        totalTimeWeek += afternoonDuration.toMinutes();
                    }
                }
                hoursWorkedInTheWeek = (int) (totalTimeWeek / 60);
                minutesWorkedInTheWeek = (int) (totalTimeWeek % 60);
            }

            if (day.getWorkdayDate().getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
                Duration morningDuration = Duration.between(day.getMorningCheckIn(), day.getMorningCheckOut());
                totalTimeDay += morningDuration.toMinutes();
                Duration afternoonDuration = Duration.between(day.getAfternoonCheckIn(), day.getAfternoonCheckOut());
                totalTimeDay += afternoonDuration.toMinutes();
            }

            hoursWorkedInTheDay = (int) (totalTimeDay / 60);
            minutesWorkedInTheDay = (int) (totalTimeDay % 60);

        }


        WorkStats ws = new WorkStats(employeeService.getEmployeeById(id).getName(),
                String.format("%02d:%02d", hoursWorkedInTheMonth, minutesWorkedInTheMonth),
                String.format("%02d:%02d", hoursWorkedInTheWeek, minutesWorkedInTheWeek),
                String.format("%02d:%02d", hoursWorkedInTheDay, minutesWorkedInTheDay));

        return ws;
    }

}
