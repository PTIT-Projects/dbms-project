package vn.ptit.hrms.dto.warehouse;

public class LeaveTrendByQuarterDTO {
    private int year;
    private int quarter;
    private int totalLeaveDaysUsed;
    private double avgLeavePerEmployee;
    
    // Getters and setters
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getQuarter() {
        return quarter;
    }
    
    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
    
    public int getTotalLeaveDaysUsed() {
        return totalLeaveDaysUsed;
    }
    
    public void setTotalLeaveDaysUsed(int totalLeaveDaysUsed) {
        this.totalLeaveDaysUsed = totalLeaveDaysUsed;
    }
    
    public double getAvgLeavePerEmployee() {
        return avgLeavePerEmployee;
    }
    
    public void setAvgLeavePerEmployee(double avgLeavePerEmployee) {
        this.avgLeavePerEmployee = avgLeavePerEmployee;
    }
}