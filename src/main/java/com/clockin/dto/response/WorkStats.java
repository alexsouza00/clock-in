package com.clockin.dto.response;


public class WorkStats {

    private String name;
    private String hoursWorkedInTheMonth;
    private String hoursWorkedInTheWeek;
    private String hoursWorkedInTheDay;
    private String lateHoursInTheMonth;
    private int missingDays;

    public WorkStats(String name, String hoursWorkedInTheMonth, String hoursWorkedInTheWeek, String hoursWorkedInTheDay, String lateHoursInTheMonth, int missingDays) {
        this.name = name;
        this.hoursWorkedInTheMonth = hoursWorkedInTheMonth;
        this.hoursWorkedInTheWeek = hoursWorkedInTheWeek;
        this.hoursWorkedInTheDay = hoursWorkedInTheDay;
        this.lateHoursInTheMonth = lateHoursInTheMonth;
        this.missingDays = missingDays;
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

    public String getLateHoursInTheMonth() {
        return lateHoursInTheMonth;
    }

    public void setLateHoursInTheMonth(String lateHoursInTheMonth) {
        this.lateHoursInTheMonth = lateHoursInTheMonth;
    }

    public int getMissingDays() {
        return missingDays;
    }

    public void setMissingDays(int missingDays) {
        this.missingDays = missingDays;
    }
}
