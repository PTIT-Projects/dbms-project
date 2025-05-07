package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryGrowthRateDTO {
    private int year;
    private BigDecimal currentYearSalary;
    private BigDecimal previousYearSalary;
    private double salaryGrowthRate;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getCurrentYearSalary() {
        return currentYearSalary;
    }

    public void setCurrentYearSalary(BigDecimal currentYearSalary) {
        this.currentYearSalary = currentYearSalary;
    }

    public BigDecimal getPreviousYearSalary() {
        return previousYearSalary;
    }

    public void setPreviousYearSalary(BigDecimal previousYearSalary) {
        this.previousYearSalary = previousYearSalary;
    }

    public double getSalaryGrowthRate() {
        return salaryGrowthRate;
    }

    public void setSalaryGrowthRate(double salaryGrowthRate) {
        this.salaryGrowthRate = salaryGrowthRate;
    }
}