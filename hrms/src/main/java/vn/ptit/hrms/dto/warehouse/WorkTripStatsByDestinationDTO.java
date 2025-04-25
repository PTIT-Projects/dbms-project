package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class WorkTripStatsByDestinationDTO {
    private String destination;
    private int tripCount;
    private int distinctEmployees;
    private BigDecimal totalCost;
    private BigDecimal avgCostPerTrip;
    private double avgDuration;
    
    // Getters and setters
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public int getTripCount() {
        return tripCount;
    }
    
    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }
    
    public int getDistinctEmployees() {
        return distinctEmployees;
    }
    
    public void setDistinctEmployees(int distinctEmployees) {
        this.distinctEmployees = distinctEmployees;
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