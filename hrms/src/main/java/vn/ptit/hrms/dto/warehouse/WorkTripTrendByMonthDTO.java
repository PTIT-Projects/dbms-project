package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class WorkTripTrendByMonthDTO {
    private int year;
    private int month;
    private int tripCount;
    private BigDecimal monthlyCost;
    private int totalDays;
    
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
    
    public int getTripCount() {
        return tripCount;
    }
    
    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }
    
    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }
    
    public void setMonthlyCost(BigDecimal monthlyCost) {
        this.monthlyCost = monthlyCost;
    }
    
    public int getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
}