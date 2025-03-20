package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.DecisionTypeEnum;

import java.time.LocalDate;

public class Decision {
    private Integer id;
    private Employee employee;
    private DecisionTypeEnum decisionType;
    private LocalDate decisionDate;
    private String details;
}
