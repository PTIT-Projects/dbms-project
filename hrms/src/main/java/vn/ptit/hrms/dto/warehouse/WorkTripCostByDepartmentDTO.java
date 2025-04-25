package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class WorkTripCostByDepartmentDTO {
    private String departmentName;
    private int totalTrips;
    private int totalDays;
    private BigDecimal totalCost;
    private BigDecimal costPerDay;
    private BigDecimal costPerEmployee;
    
    // Getters and setters
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public int getTotalTrips() {
        return totalTrips;
    }
    
    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }
    
    public int getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public BigDecimal getCostPerDay() {
        return costPerDay;
    }
    
    public void setCostPerDay(BigDecimal costPerDay) {
        this.costPerDay = costPerDay;
    }
    
    public BigDecimal getCostPerEmployee() {
        return costPerEmployee;
    }
    
    public void setCostPerEmployee(BigDecimal costPerEmployee) {
        this.costPerEmployee = costPerEmployee;
    }
}