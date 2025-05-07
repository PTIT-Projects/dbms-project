package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class WorkTripPurposeStatsDTO {
    private String purpose;
    private int tripCount;
    private int distinctDestinations;
    private double avgDuration;
    private BigDecimal totalCost;
    private BigDecimal avgCostPerTrip;
    
    // Getters and setters
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public int getTripCount() {
        return tripCount;
    }
    
    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }
    
    public int getDistinctDestinations() {
        return distinctDestinations;
    }
    
    public void setDistinctDestinations(int distinctDestinations) {
        this.distinctDestinations = distinctDestinations;
    }
    
    public double getAvgDuration() {
        return avgDuration;
    }
    
    public void setAvgDuration(double avgDuration) {
        this.avgDuration = avgDuration;
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
}