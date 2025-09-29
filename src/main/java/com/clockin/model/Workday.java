package com.clockin.model;

import jakarta.persistence.*;
import org.aspectj.lang.annotation.RequiredTypes;

import java.time.Instant;
import java.time.LocalDate;

@Entity
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private LocalDate workdayDate;
    private String dayOfTheWeek;
    private LocalDate morningCheckIn;
    private LocalDate morningCheckOut;
    private LocalDate afternoonCheckIn;
    private LocalDate afternoonCheckOut;

    public Workday(){}

    public Workday(Employee employee, LocalDate workdayDate, String dayOfTheWeek, LocalDate morningCheckIn, LocalDate morningCheckOut, LocalDate afternoonCheckIn, LocalDate afternoonCheckOut) {
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

    public LocalDate getMorningCheckIn() {
        return morningCheckIn;
    }

    public void setMorningCheckIn(LocalDate morningCheckIn) {
        this.morningCheckIn = morningCheckIn;
    }

    public LocalDate getMorningCheckOut() {
        return morningCheckOut;
    }

    public void setMorningCheckOut(LocalDate morningCheckOut) {
        this.morningCheckOut = morningCheckOut;
    }

    public LocalDate getAfternoonCheckIn() {
        return afternoonCheckIn;
    }

    public void setAfternoonCheckIn(LocalDate afternoonCheckIn) {
        this.afternoonCheckIn = afternoonCheckIn;
    }

    public LocalDate getAfternoonCheckOut() {
        return afternoonCheckOut;
    }

    public void setAfternoonCheckOut(LocalDate afternoonCheckOut) {
        this.afternoonCheckOut = afternoonCheckOut;
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
