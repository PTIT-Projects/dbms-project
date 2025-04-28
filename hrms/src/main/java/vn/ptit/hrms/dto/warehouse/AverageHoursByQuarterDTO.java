package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class AverageHoursByQuarterDTO {
    private int year;
    private int quarter;
    private BigDecimal avgHoursWorked;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getAvgHoursWorked() {
        return avgHoursWorked;
    }

    public void setAvgHoursWorked(BigDecimal avgHoursWorked) {
        this.avgHoursWorked = avgHoursWorked;
    }
}