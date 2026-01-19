package com.clockin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "workdays")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate workdayDate;
    private String dayOfTheWeek;
    private LocalTime morningCheckIn;
    private LocalTime morningCheckOut;
    private LocalTime afternoonCheckIn;
    private LocalTime afternoonCheckOut;

    public Workday() {
    }

    public Workday(Employee employee, LocalDate workdayDate, String dayOfTheWeek, LocalTime morningCheckIn, LocalTime morningCheckOut, LocalTime afternoonCheckIn, LocalTime afternoonCheckOut) {
        this.employee = employee;
        this.workdayDate = workdayDate;
        this.dayOfTheWeek = dayOfTheWeek;
        this.morningCheckIn = morningCheckIn;
        this.morningCheckOut = morningCheckOut;
        this.afternoonCheckIn = afternoonCheckIn;
        this.afternoonCheckOut = afternoonCheckOut;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getWorkdayDate() {
        return workdayDate;
    }

    public void setWorkdayDate(LocalDate workdayDate) {
        this.workdayDate = workdayDate;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public LocalTime getMorningCheckIn() {
        return morningCheckIn;
    }

    public void setMorningCheckIn(LocalTime morningCheckIn) {
        this.morningCheckIn = morningCheckIn;
    }

    public LocalTime getMorningCheckOut() {
        return morningCheckOut;
    }

    public void setMorningCheckOut(LocalTime morningCheckOut) {
        this.morningCheckOut = morningCheckOut;
    }

    public LocalTime getAfternoonCheckIn() {
        return afternoonCheckIn;
    }

    public void setAfternoonCheckIn(LocalTime afternoonCheckIn) {
        this.afternoonCheckIn = afternoonCheckIn;
    }

    public LocalTime getAfternoonCheckOut() {
        return afternoonCheckOut;
    }

    public void setAfternoonCheckOut(LocalTime afternoonCheckOut) {
        this.afternoonCheckOut = afternoonCheckOut;
    }

    public long timeWorked() {

        long totalMinutes = 0;

        if (this.getMorningCheckIn() != null && this.getMorningCheckOut() != null) {
            long minutes = Duration.between(this.getMorningCheckIn(), this.getMorningCheckOut()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }
        if (this.getAfternoonCheckIn() != null && this.getAfternoonCheckOut() != null) {
            long minutes = Duration.between(this.getAfternoonCheckIn(), this.getAfternoonCheckOut()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }

        return totalMinutes;
    }

    public long lateHours() {

        LocalTime morningCheckIn = LocalTime.of(8, 0);
        LocalTime afternoonCheckIn = LocalTime.of(13, 0);
        long totalMinutes = 0;

        if (this.getMorningCheckIn() != null) {
            long minutes = Duration.between(morningCheckIn, this.getMorningCheckIn()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }
        if (this.getAfternoonCheckIn() != null) {
            long minutes = Duration.between(afternoonCheckIn, this.getAfternoonCheckIn()).toMinutes();
            totalMinutes += Math.max(0, minutes);
        }

        return totalMinutes;
    }

    @Override
    public String toString() {
        return "WorkdaySummary{" +
                "id=" + id +
                ", employee=" + employee +
                ", workdayDate=" + workdayDate +
                ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                ", morningCheckIn=" + morningCheckIn +
                ", morningCheckOut=" + morningCheckOut +
                ", afternoonCheckIn=" + afternoonCheckIn +
                ", afternoonCheckOut=" + afternoonCheckOut +
                '}';
    }
}
