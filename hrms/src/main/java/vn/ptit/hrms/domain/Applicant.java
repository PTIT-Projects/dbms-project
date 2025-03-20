package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.ApplicantStatusEnum;

public class Applicant {
    private Integer id;
    private RecruitmentPlan plan;
    private String fullName;
    private String email;
    private String phone;
    private ApplicantStatusEnum status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RecruitmentPlan getPlan() {
        return plan;
    }

    public void setPlan(RecruitmentPlan plan) {
        this.plan = plan;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ApplicantStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ApplicantStatusEnum status) {
        this.status = status;
    }
}
