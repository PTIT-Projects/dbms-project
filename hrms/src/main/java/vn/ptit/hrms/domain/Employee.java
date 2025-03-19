package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.EmployeeStatusEnum;
import vn.ptit.hrms.constant.GenderEnum;

import java.time.LocalDate;

public class Employee {
    private Integer id;
    private String fullName;
    private LocalDate dateOfBirth;
    private GenderEnum gender;
    private String address;
    private String phone;
    private String email;
    private Department department;
    private String position;
    private LocalDate hireDate;
    private EmployeeStatusEnum status;
}
