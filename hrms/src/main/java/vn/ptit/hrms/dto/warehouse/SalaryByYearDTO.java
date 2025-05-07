package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryByYearDTO {
    private int year;
    private BigDecimal averageSalary;

    public int getYear() {
        return year;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }
}
