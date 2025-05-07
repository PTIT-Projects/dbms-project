package vn.ptit.hrms.domain.warehouse;

import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;

public class FactRegistration {
    private int registrationSk;
    private int registrationId;
    private int employeeSk;
    private String employeeName;
    private String departmentName;
    private String approvedByName;
    private RegistrationTypeEnum registrationType;
    private int requestDateSk;
    private int registrationDuration;
    private RegistrationStatusEnum status;
    private int approvedBySk;

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }

    public RegistrationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatusEnum status) {
        this.status = status;
    }

    public int getRegistrationSk() {
        return registrationSk;
    }

    public void setRegistrationSk(int registrationSk) {
        this.registrationSk = registrationSk;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
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

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }


    public int getRequestDateSk() {
        return requestDateSk;
    }

    public void setRequestDateSk(int requestDateSk) {
        this.requestDateSk = requestDateSk;
    }

    public int getRegistrationDuration() {
        return registrationDuration;
    }

    public void setRegistrationDuration(int registrationDuration) {
        this.registrationDuration = registrationDuration;
    }


    public int getApprovedBySk() {
        return approvedBySk;
    }

    public void setApprovedBySk(int approvedBySk) {
        this.approvedBySk = approvedBySk;
    }
}
