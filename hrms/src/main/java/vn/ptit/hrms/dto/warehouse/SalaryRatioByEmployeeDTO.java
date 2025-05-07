package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryRatioByEmployeeDTO {
    private String employeeName;
    private String departmentName;
    private String positionName;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal deductions;
    private BigDecimal totalSalary;
    private double basicSalaryPercentage;
    private double allowancePercentage;
    private double deductionsPercentage;
    
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
    
    public BigDecimal getTotalSalary() {
        return totalSalary;
    }
    
    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }
    
    public double getBasicSalaryPercentage() {
        return basicSalaryPercentage;
    }
    
    public void setBasicSalaryPercentage(double basicSalaryPercentage) {
        this.basicSalaryPercentage = basicSalaryPercentage;
    }
    
    public double getAllowancePercentage() {
        return allowancePercentage;
    }
    
    public void setAllowancePercentage(double allowancePercentage) {
        this.allowancePercentage = allowancePercentage;
    }
    
    public double getDeductionsPercentage() {
        return deductionsPercentage;
    }
    
    public void setDeductionsPercentage(double deductionsPercentage) {
        this.deductionsPercentage = deductionsPercentage;
    }
}