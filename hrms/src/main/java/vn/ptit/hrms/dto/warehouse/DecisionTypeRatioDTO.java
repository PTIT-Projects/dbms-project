package vn.ptit.hrms.dto.warehouse;

public class DecisionTypeRatioDTO {
    private String decisionType;
    private int totalDecisions;
    private double percentage;
    
    public DecisionTypeRatioDTO() {
    }
    
    public DecisionTypeRatioDTO(String decisionType, int totalDecisions, double percentage) {
        this.decisionType = decisionType;
        this.totalDecisions = totalDecisions;
        this.percentage = percentage;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public int getTotalDecisions() {
        return totalDecisions;
    }

    public void setTotalDecisions(int totalDecisions) {
        this.totalDecisions = totalDecisions;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}