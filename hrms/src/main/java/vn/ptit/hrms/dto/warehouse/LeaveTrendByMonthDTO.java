package vn.ptit.hrms.dto.warehouse;

public class LeaveTrendByMonthDTO {
    private int year;
    private int month;
    private int totalLeaveDaysUsed;
    private int employeesTookLeave;
    
    // Getters and setters
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getTotalLeaveDaysUsed() {
        return totalLeaveDaysUsed;
    }
    
    public void setTotalLeaveDaysUsed(int totalLeaveDaysUsed) {
        this.totalLeaveDaysUsed = totalLeaveDaysUsed;
    }
    
    public int getEmployeesTookLeave() {
        return employeesTookLeave;
    }
    
    public void setEmployeesTookLeave(int employeesTookLeave) {
        this.employeesTookLeave = employeesTookLeave;
    }
}