package com.clockin.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private Instant workdayDate;
    private String dayOfTheWeek;
    private Instant morningCheckIn;
    private Instant morningCheckOut;
    private Instant afternoonCheckIn;
    private Instant afternoonCheckOut;

    public Workday(){}

    public Workday(Employee employee, Instant workdayDate, String dayOfTheWeek, Instant morningCheckIn, Instant morningCheckOut, Instant afternoonCheckIn, Instant afternoonCheckOut) {
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

    public Instant getWorkdayDate() {
        return workdayDate;
    }

    public void setWorkdayDate(Instant workdayDate) {
        this.workdayDate = workdayDate;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public Instant getMorningCheckIn() {
        return morningCheckIn;
    }

    public void setMorningCheckIn(Instant morningCheckIn) {
        this.morningCheckIn = morningCheckIn;
    }

    public Instant getMorningCheckOut() {
        return morningCheckOut;
    }

    public void setMorningCheckOut(Instant morningCheckOut) {
        this.morningCheckOut = morningCheckOut;
    }

    public Instant getAfternoonCheckIn() {
        return afternoonCheckIn;
    }

    public void setAfternoonCheckIn(Instant afternoonCheckIn) {
        this.afternoonCheckIn = afternoonCheckIn;
    }

    public Instant getAfternoonCheckOut() {
        return afternoonCheckOut;
    }

    public void setAfternoonCheckOut(Instant afternoonCheckOut) {
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
