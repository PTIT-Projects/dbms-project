package vn.ptit.hrms.dto.warehouse;

public class LeaveUsageEmployeeDTO {
    private String employeeName;
    private String departmentName;
    private String positionName;
    private int totalLeaveDays;
    private int usedLeaveDays;
    private int remainingLeaveDays;
    private double usagePercentage;
    
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
    
    public int getTotalLeaveDays() {
        return totalLeaveDays;
    }
    
    public void setTotalLeaveDays(int totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }
    
    public int getUsedLeaveDays() {
        return usedLeaveDays;
    }
    
    public void setUsedLeaveDays(int usedLeaveDays) {
        this.usedLeaveDays = usedLeaveDays;
    }
    
    public int getRemainingLeaveDays() {
        return remainingLeaveDays;
    }
    
    public void setRemainingLeaveDays(int remainingLeaveDays) {
        this.remainingLeaveDays = remainingLeaveDays;
    }
    
    public double getUsagePercentage() {
        return usagePercentage;
    }
    
    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }
}