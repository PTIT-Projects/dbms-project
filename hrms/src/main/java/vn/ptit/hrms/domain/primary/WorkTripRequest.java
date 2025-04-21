package vn.ptit.hrms.domain.primary;

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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public RegistrationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatusEnum status) {
        this.status = status;
    }
}
