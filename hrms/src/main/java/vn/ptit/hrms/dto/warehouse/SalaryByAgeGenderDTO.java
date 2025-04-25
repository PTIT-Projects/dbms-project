package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryByAgeGenderDTO {
    private String gender;
    private String ageGroup;
    private BigDecimal avgSalary;
    private int employeeCount;
    
    // Getters and setters
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getAgeGroup() {
        return ageGroup;
    }
    
    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }
    
    public BigDecimal getAvgSalary() {
        return avgSalary;
    }
    
    public void setAvgSalary(BigDecimal avgSalary) {
        this.avgSalary = avgSalary;
    }
    
    public int getEmployeeCount() {
        return employeeCount;
    }
    
    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }
}