package com.clockin.dto;


public class WorkStatistic {

    private String name;
    private String hoursWorkedInTheMonth;
    private String hoursWorkedInTheWeek;
    private String hoursWorkedInTheDay;


    public WorkStatistic(String name, String hoursWorkedInTheMonth, String hoursWorkedInTheWeek, String hoursWorkedInTheDay) {
        this.name = name;
        this.hoursWorkedInTheMonth = hoursWorkedInTheMonth;
        this.hoursWorkedInTheWeek = hoursWorkedInTheWeek;
        this.hoursWorkedInTheDay = hoursWorkedInTheDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHoursWorkedInTheMonth() {
        return hoursWorkedInTheMonth;
    }

    public void setHoursWorkedInTheMonth(String hoursWorkedInTheMonth) {
        this.hoursWorkedInTheMonth = hoursWorkedInTheMonth;
    }

    public String getHoursWorkedInTheWeek() {
        return hoursWorkedInTheWeek;
    }

    public void setHoursWorkedInTheWeek(String hoursWorkedInTheWeek) {
        this.hoursWorkedInTheWeek = hoursWorkedInTheWeek;
    }

    public String getHoursWorkedInTheDay() {
        return hoursWorkedInTheDay;
    }

    public void setHoursWorkedInTheDay(String hoursWorkedInTheDay) {
        this.hoursWorkedInTheDay = hoursWorkedInTheDay;
    }
}
