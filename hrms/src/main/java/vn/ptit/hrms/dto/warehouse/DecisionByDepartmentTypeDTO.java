package vn.ptit.hrms.dto.warehouse;

public class DecisionByDepartmentTypeDTO {
    private String departmentName;
    private String decisionType;
    private int totalDecisions;
    
    public DecisionByDepartmentTypeDTO() {
    }
    
    public DecisionByDepartmentTypeDTO(String departmentName, String decisionType, int totalDecisions) {
        this.departmentName = departmentName;
        this.decisionType = decisionType;
        this.totalDecisions = totalDecisions;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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