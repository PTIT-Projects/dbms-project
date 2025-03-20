package vn.ptit.hrms.constant;

public enum ApplicantStatusEnum {
    REJECTED("Rejected"), HIRED("Hired"), INTERVIEWED("Interviewed"), APPLIED("Applied");
    private final String value;

    ApplicantStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
