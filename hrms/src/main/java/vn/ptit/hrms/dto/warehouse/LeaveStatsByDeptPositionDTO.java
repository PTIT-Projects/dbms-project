package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class LeaveStatsByDeptPositionDTO {
    private String departmentName;
    private String positionName;
    private int employeeCount;
    private double avgTotalLeave;
    private double avgUsedLeave;
    private double avgRemainingLeave;
    private double usagePercentage;
    
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
    
    public double getAvgTotalLeave() {
        return avgTotalLeave;
    }
    
    public void setAvgTotalLeave(double avgTotalLeave) {
        this.avgTotalLeave = avgTotalLeave;
    }
    
    public double getAvgUsedLeave() {
        return avgUsedLeave;
    }
    
    public void setAvgUsedLeave(double avgUsedLeave) {
        this.avgUsedLeave = avgUsedLeave;
    }
    
    public double getAvgRemainingLeave() {
        return avgRemainingLeave;
    }
    
    public void setAvgRemainingLeave(double avgRemainingLeave) {
        this.avgRemainingLeave = avgRemainingLeave;
    }
    
    public double getUsagePercentage() {
        return usagePercentage;
    }
    
    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }
}