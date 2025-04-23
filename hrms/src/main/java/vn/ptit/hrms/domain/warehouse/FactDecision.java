package vn.ptit.hrms.domain.warehouse;

import vn.ptit.hrms.constant.DecisionTypeEnum;

import java.time.LocalDate;

public class FactDecision {
    private int decisionSk;
    private int decisionId;
    private int employeeSk;
    private String employeeName;
    private String departmentName;
    private int decisionDateSk;
    private DecisionTypeEnum decisionType;
    private String decisionDetail;
    private LocalDate decisionEffectiveDate;
    private LocalDate decisionExpiryDate;

    public int getDecisionSk() {
        return decisionSk;
    }

    public void setDecisionSk(int decisionSk) {
        this.decisionSk = decisionSk;
    }

    public int getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(int decisionId) {
        this.decisionId = decisionId;
    }

    public int getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(int employeeSk) {
        this.employeeSk = employeeSk;
    }

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

    public int getDecisionDateSk() {
        return decisionDateSk;
    }

    public void setDecisionDateSk(int decisionDateSk) {
        this.decisionDateSk = decisionDateSk;
    }

    public DecisionTypeEnum getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(DecisionTypeEnum decisionType) {
        this.decisionType = decisionType;
    }

    public String getDecisionDetail() {
        return decisionDetail;
    }

    public void setDecisionDetail(String decisionDetail) {
        this.decisionDetail = decisionDetail;
    }

    public LocalDate getDecisionEffectiveDate() {
        return decisionEffectiveDate;
    }

    public void setDecisionEffectiveDate(LocalDate decisionEffectiveDate) {
        this.decisionEffectiveDate = decisionEffectiveDate;
    }

    public LocalDate getDecisionExpiryDate() {
        return decisionExpiryDate;
    }

    public void setDecisionExpiryDate(LocalDate decisionExpiryDate) {
        this.decisionExpiryDate = decisionExpiryDate;
    }
}
