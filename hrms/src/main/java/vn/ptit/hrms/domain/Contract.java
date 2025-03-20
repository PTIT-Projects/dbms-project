package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.ContractStatusEnum;

import java.time.LocalDate;

public class Contract {
    private Integer id;
    private Employee employee;
    private String contractType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double salary;
    private ContractStatusEnum status;
}
