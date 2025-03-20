package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.DecisionTypeEnum;

import java.time.LocalDate;

public class Decision {
    private Integer id;
    private Employee employee;
    private DecisionTypeEnum decisionType;
    private LocalDate decisionDate;
    private String details;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DecisionTypeEnum getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(DecisionTypeEnum decisionType) {
        this.decisionType = decisionType;
    }

    public LocalDate getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDate decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
