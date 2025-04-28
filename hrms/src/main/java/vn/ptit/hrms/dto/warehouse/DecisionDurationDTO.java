package vn.ptit.hrms.dto.warehouse;

public class DecisionDurationDTO {
    private String decisionType;
    private double avgDurationDays;
    
    public DecisionDurationDTO() {
    }
    
    public DecisionDurationDTO(String decisionType, double avgDurationDays) {
        this.decisionType = decisionType;
        this.avgDurationDays = avgDurationDays;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public double getAvgDurationDays() {
        return avgDurationDays;
    }

    public void setAvgDurationDays(double avgDurationDays) {
        this.avgDurationDays = avgDurationDays;
    }
}