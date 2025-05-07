package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class DepartmentPerformanceDTO {
    private String departmentName;
    private int employeeCount;
    private BigDecimal avgHoursPerDay;
    private double latePercentage;
    private BigDecimal totalOvertime;

    // Getters and setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public BigDecimal getAvgHoursPerDay() {
        return avgHoursPerDay;
    }

    public void setAvgHoursPerDay(BigDecimal avgHoursPerDay) {
        this.avgHoursPerDay = avgHoursPerDay;
    }

    public double getLatePercentage() {
        return latePercentage;
    }

    public void setLatePercentage(double latePercentage) {
        this.latePercentage = latePercentage;
    }

    public BigDecimal getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(BigDecimal totalOvertime) {
        this.totalOvertime = totalOvertime;
    }
}