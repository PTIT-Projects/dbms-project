package vn.ptit.hrms.constant;

public enum ApplicantStatusEnum {
    REJECTED("Từ chối"), HIRED("Đã tuyển"), INTERVIEWED("Đã phỏng vấn"), APPLIED("Đã ứng tuyển");
    private final String value;

    ApplicantStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
