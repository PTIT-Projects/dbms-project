package vn.ptit.hrms.domain;

import java.time.LocalDate;

public class Insurance {
    private Integer id;
    private Employee employee;
    private String insuranceNumber;
    private String insuranceType;
    private LocalDate startDate;
    private LocalDate endDate;
}
