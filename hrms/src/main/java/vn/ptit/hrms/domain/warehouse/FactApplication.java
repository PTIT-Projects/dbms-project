package vn.ptit.hrms.domain.warehouse;

import vn.ptit.hrms.constant.ApplicantStatusEnum;

public class FactApplication {
    private int applicationSk;
    private int applicantId;
    private int recruitmentSk;
    private int applicationDateSk;
    private String positionName;
    private String departmentName;
    private ApplicantStatusEnum status;

    public int getApplicationSk() {
        return applicationSk;
    }

    public void setApplicationSk(int applicationSk) {
        this.applicationSk = applicationSk;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getRecruitmentSk() {
        return recruitmentSk;
    }

    public void setRecruitmentSk(int recruitmentSk) {
        this.recruitmentSk = recruitmentSk;
    }

    public int getApplicationDateSk() {
        return applicationDateSk;
    }

    public void setApplicationDateSk(int applicationDateSk) {
        this.applicationDateSk = applicationDateSk;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public ApplicantStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ApplicantStatusEnum status) {
        this.status = status;
    }
}
