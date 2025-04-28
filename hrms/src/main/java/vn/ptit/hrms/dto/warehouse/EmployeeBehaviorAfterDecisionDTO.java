package vn.ptit.hrms.dto.warehouse;

public class EmployeeBehaviorAfterDecisionDTO {
    private int employeeSk;
    private String fullName;
    private int disciplinaryCount;
    private double avgLateRateAfterDecision;
    
    public EmployeeBehaviorAfterDecisionDTO() {
    }
    
    public EmployeeBehaviorAfterDecisionDTO(int employeeSk, String fullName, int disciplinaryCount, double avgLateRateAfterDecision) {
        this.employeeSk = employeeSk;
        this.fullName = fullName;
        this.disciplinaryCount = disciplinaryCount;
        this.avgLateRateAfterDecision = avgLateRateAfterDecision;
    }

    public int getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(int employeeSk) {
        this.employeeSk = employeeSk;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getDisciplinaryCount() {
        return disciplinaryCount;
    }

    public void setDisciplinaryCount(int disciplinaryCount) {
        this.disciplinaryCount = disciplinaryCount;
    }

    public double getAvgLateRateAfterDecision() {
        return avgLateRateAfterDecision;
    }

    public void setAvgLateRateAfterDecision(double avgLateRateAfterDecision) {
        this.avgLateRateAfterDecision = avgLateRateAfterDecision;
    }
}