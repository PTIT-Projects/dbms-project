package vn.ptit.hrms.domain.warehouse;

import java.time.LocalDate;

public class DimDate {
    private Integer dateSk;
    private LocalDate date;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer week;
    private Integer quarter;

    public Integer getDateSk() {
        return dateSk;
    }

    public void setDateSk(Integer dateSk) {
        this.dateSk = dateSk;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }
}