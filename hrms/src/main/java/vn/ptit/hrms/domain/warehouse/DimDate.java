package vn.ptit.hrms.domain.warehouse;

import java.time.LocalDate;

public class DimDate {
    private int dateSk;
    private LocalDate date;
    private int year;
    private int month;
    private int day;
    private int week;
    private int quarter;

    public int getDateSk() {
        return dateSk;
    }

    public void setDateSk(int dateSk) {
        this.dateSk = dateSk;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
}
