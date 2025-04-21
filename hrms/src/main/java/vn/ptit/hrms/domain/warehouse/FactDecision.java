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
}
