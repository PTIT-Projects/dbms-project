package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class WorkTripStatsByEmployeeDTO {
    private String employeeName;
    private String departmentName;
    private String positionName;
    private int totalTrips;
    private int totalDaysOnTrip;
    private BigDecimal totalTripCost;
    
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
    
    public int getTotalTrips() {
        return totalTrips;
    }
    
    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }
    
    public int getTotalDaysOnTrip() {
        return totalDaysOnTrip;
    }
    
    public void setTotalDaysOnTrip(int totalDaysOnTrip) {
        this.totalDaysOnTrip = totalDaysOnTrip;
    }
    
    public BigDecimal getTotalTripCost() {
        return totalTripCost;
    }
    
    public void setTotalTripCost(BigDecimal totalTripCost) {
        this.totalTripCost = totalTripCost;
    }
}