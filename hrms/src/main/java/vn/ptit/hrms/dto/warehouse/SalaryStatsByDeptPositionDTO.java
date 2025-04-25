package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryStatsByDeptPositionDTO {
    private String departmentName;
    private String positionName;
    private int employeeCount;
    private BigDecimal avgBasicSalary;
    private BigDecimal avgAllowance;
    private BigDecimal avgDeductions;
    private BigDecimal avgTotalSalary;

    // Getters and setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public BigDecimal getAvgBasicSalary() {
        return avgBasicSalary;
    }

    public void setAvgBasicSalary(BigDecimal avgBasicSalary) {
        this.avgBasicSalary = avgBasicSalary;
    }

    public BigDecimal getAvgAllowance() {
        return avgAllowance;
    }

    public void setAvgAllowance(BigDecimal avgAllowance) {
        this.avgAllowance = avgAllowance;
    }

    public BigDecimal getAvgDeductions() {
        return avgDeductions;
    }

    public void setAvgDeductions(BigDecimal avgDeductions) {
        this.avgDeductions = avgDeductions;
    }

    public BigDecimal getAvgTotalSalary() {
        return avgTotalSalary;
    }

    public void setAvgTotalSalary(BigDecimal avgTotalSalary) {
        this.avgTotalSalary = avgTotalSalary;
    }
}