package com.microsoft.band.monitor;

public class PeriodCalendarEntry {

    String startDate, endDate;
    int days;

    public PeriodCalendarEntry(String startDate, String endDate, int days) {
        this.startDate = new String(startDate);
        this.endDate = new String(endDate);
        this.days = days;
    }

    @Override
    public String toString() {
        return "StartDate = " + startDate +
                "\nEndDate = " + endDate +
                "\nNumber of Days = " + days;
    }
}
