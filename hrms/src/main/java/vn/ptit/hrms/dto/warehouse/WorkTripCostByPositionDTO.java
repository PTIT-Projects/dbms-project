package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class WorkTripCostByPositionDTO {
    private String positionName;
    private int tripCount;
    private BigDecimal totalCost;
    private BigDecimal avgCostPerTrip;
    private double avgDuration;
    
    // Getters and setters
    public String getPositionName() {
        return positionName;
    }
    
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    
    public int getTripCount() {
        return tripCount;
    }
    
    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public BigDecimal getAvgCostPerTrip() {
        return avgCostPerTrip;
    }
    
    public void setAvgCostPerTrip(BigDecimal avgCostPerTrip) {
        this.avgCostPerTrip = avgCostPerTrip;
    }
    
    public double getAvgDuration() {
        return avgDuration;
    }
    
    public void setAvgDuration(double avgDuration) {
        this.avgDuration = avgDuration;
    }
}