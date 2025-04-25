package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class TopSalaryEmployeeDTO {
    private String employeeName;
    private String departmentName;
    private String positionName;
    private BigDecimal totalSalary;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal deductions;
    private double salaryPercentage; // For top 10 only - percent of max salary

    // Getters and setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

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

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public double getSalaryPercentage() {
        return salaryPercentage;
    }

    public void setSalaryPercentage(double salaryPercentage) {
        this.salaryPercentage = salaryPercentage;
    }
}