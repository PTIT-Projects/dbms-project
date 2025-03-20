package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.RegistrationStatusEnum;

import java.time.LocalDate;

public class WorkTripRequest {
    private Integer id;
    private Employee employee;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;
    private RegistrationStatusEnum status;
}
