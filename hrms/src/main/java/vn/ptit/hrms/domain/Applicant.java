package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.ApplicantStatusEnum;

public class Applicant {
    private Integer id;
    private RecruitmentPlan plan;
    private String fullName;
    private String email;
    private String phone;
    private ApplicantStatusEnum status;
}
