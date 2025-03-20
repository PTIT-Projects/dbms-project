package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;

import java.time.LocalDate;

public class Registration {
    private Integer id;
    private Employee employee;
    private RegistrationTypeEnum registrationType;
    private LocalDate requestDate;
    private String details;
    private RegistrationStatusEnum status;
    private Employee approvedBy;
}
