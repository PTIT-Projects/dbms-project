package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class YearlyAverageSalaryDTO {
    private int year;
    private BigDecimal avgSalary;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getAvgSalary() {
        return avgSalary;
    }

    public void setAvgSalary(BigDecimal avgSalary) {
        this.avgSalary = avgSalary;
    }
}