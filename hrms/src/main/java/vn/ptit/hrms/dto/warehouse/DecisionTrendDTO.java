package vn.ptit.hrms.dto.warehouse;

public class DecisionTrendDTO {
    private int year;
    private int month;
    private String decisionType;
    private int totalDecisions;
    
    public DecisionTrendDTO() {
    }
    
    public DecisionTrendDTO(int year, int month, String decisionType, int totalDecisions) {
        this.year = year;
        this.month = month;
        this.decisionType = decisionType;
        this.totalDecisions = totalDecisions;
    }

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
}