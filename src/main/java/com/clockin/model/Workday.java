package com.clockin.model;

import jakarta.persistence.*;

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
