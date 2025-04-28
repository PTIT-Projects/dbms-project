package vn.ptit.hrms.dto.warehouse;

public class DecisionByPositionDTO {
    private String positionName;
    private String decisionType;
    private int decisionCount;
    
    public DecisionByPositionDTO() {
    }
    
    public DecisionByPositionDTO(String positionName, String decisionType, int decisionCount) {
        this.positionName = positionName;
        this.decisionType = decisionType;
        this.decisionCount = decisionCount;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public int getDecisionCount() {
        return decisionCount;
    }

    public void setDecisionCount(int decisionCount) {
        this.decisionCount = decisionCount;
    }
}